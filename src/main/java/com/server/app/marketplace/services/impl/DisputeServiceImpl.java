package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.DisputeResolution;
import com.server.app.marketplace.common.enums.DisputeStatus;
import com.server.app.marketplace.common.enums.OrderStatus;
import com.server.app.marketplace.common.enums.PaymentStatus;
import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.DisputeMapper;
import com.server.app.marketplace.domain.dto.request.CreateDisputeRequest;
import com.server.app.marketplace.domain.dto.request.ResolveDisputeRequest;
import com.server.app.marketplace.domain.dto.request.RespondDisputeRequest;
import com.server.app.marketplace.domain.dto.response.dispute.DisputeResponse;
import com.server.app.marketplace.domain.entities.*;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.*;
import com.server.app.marketplace.services.DisputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisputeServiceImpl implements DisputeService {

    private static final List<DisputeStatus> ACTIVE_DISPUTE_STATUSES = List.of(
            DisputeStatus.OPEN,
            DisputeStatus.SELLER_RESPONDED
    );

    private static final List<OrderStatus> ELIGIBLE_ORDER_STATUSES = List.of(
            OrderStatus.PAID,
            OrderStatus.SHIPPED,
            OrderStatus.DELIVERED
    );

    private final DisputeRepository disputeRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final DisputeMapper disputeMapper;

    @Override
    @Transactional
    public DisputeResponse createDispute(CreateDisputeRequest request) {
        User buyer = findActiveUser(request.getBuyerId());
        requireRole(buyer, UserRole.BUYER, "Only BUYER users can open disputes.");

        OrderItem orderItem = orderItemRepository.findById(request.getOrderItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found."));

        Order order = orderItem.getOrder();

        if (!order.getBuyer().getId().equals(buyer.getId())) {
            throw new BusinessRuleException("This order does not belong to the buyer.");
        }

        if (!ELIGIBLE_ORDER_STATUSES.contains(order.getStatus())) {
            throw new BusinessRuleException(
                    "Disputes can only be opened for PAID, SHIPPED or DELIVERED orders."
            );
        }

        if (request.getQuantity() > orderItem.getQuantity()) {
            throw new BusinessRuleException("Dispute quantity cannot exceed purchased quantity.");
        }

        if (disputeRepository.existsByOrderItemIdAndStatusIn(
                orderItem.getId(),
                ACTIVE_DISPUTE_STATUSES
        )) {
            throw new BusinessRuleException("This order item already has an active dispute.");
        }

        User seller = orderItem.getProduct().getSellerProfile().getUser();
        LocalDateTime now = LocalDateTime.now();

        Dispute dispute = Dispute.builder()
                .orderItem(orderItem)
                .buyer(buyer)
                .seller(seller)
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .status(DisputeStatus.OPEN)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return disputeMapper.toDto(disputeRepository.save(dispute));
    }

    @Override
    @Transactional(readOnly = true)
    public DisputeResponse getDisputeById(Long id) {
        return disputeMapper.toDto(findDispute(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisputeResponse> getDisputesByBuyer(Long buyerId) {
        findActiveUser(buyerId);
        return disputeRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId)
                .stream()
                .map(disputeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisputeResponse> getDisputesBySeller(Long sellerUserId) {
        User seller = findActiveUser(sellerUserId);
        requireRole(seller, UserRole.SELLER, "Only SELLER users can view seller disputes.");

        return disputeRepository.findBySellerIdOrderByCreatedAtDesc(sellerUserId)
                .stream()
                .map(disputeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisputeResponse> getPendingDisputesForAdmin(Long adminUserId) {
        User admin = findActiveUser(adminUserId);
        requireRole(admin, UserRole.ADMIN, "Only ADMIN users can view pending disputes.");

        return disputeRepository.findByStatusInOrderByCreatedAtDesc(ACTIVE_DISPUTE_STATUSES)
                .stream()
                .map(disputeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public DisputeResponse respondToDispute(Long disputeId, RespondDisputeRequest request) {
        User seller = findActiveUser(request.getSellerUserId());
        requireRole(seller, UserRole.SELLER, "Only SELLER users can respond to disputes.");

        Dispute dispute = findDispute(disputeId);

        if (!dispute.getSeller().getId().equals(seller.getId())) {
            throw new BusinessRuleException("This seller cannot respond to this dispute.");
        }

        if (dispute.getStatus() != DisputeStatus.OPEN) {
            throw new BusinessRuleException("Only OPEN disputes can receive a seller response.");
        }

        dispute.setSellerResponse(request.getSellerResponse());
        dispute.setStatus(DisputeStatus.SELLER_RESPONDED);
        dispute.setUpdatedAt(LocalDateTime.now());

        return disputeMapper.toDto(disputeRepository.save(dispute));
    }

    @Override
    @Transactional
    public DisputeResponse resolveDispute(Long disputeId, ResolveDisputeRequest request) {
        User admin = findActiveUser(request.getAdminUserId());
        requireRole(admin, UserRole.ADMIN, "Only ADMIN users can resolve disputes.");

        Dispute dispute = findDispute(disputeId);

        if (dispute.getStatus() != DisputeStatus.OPEN
                && dispute.getStatus() != DisputeStatus.SELLER_RESPONDED) {
            throw new BusinessRuleException(
                    "Only OPEN or SELLER_RESPONDED disputes can be resolved by admin."
            );
        }

        dispute.setResolution(request.getResolution());
        dispute.setAdminNotes(request.getAdminNotes());
        dispute.setResolvedByAdmin(admin);
        dispute.setStatus(DisputeStatus.RESOLVED);
        dispute.setUpdatedAt(LocalDateTime.now());

        if (request.getResolution() == DisputeResolution.BUYER_FAVOR) {
            double refundAmount = applyBuyerFavorRefund(dispute);
            dispute.setRefundAmount(refundAmount);
        }

        return disputeMapper.toDto(disputeRepository.save(dispute));
    }

    private double applyBuyerFavorRefund(Dispute dispute) {
        OrderItem orderItem = dispute.getOrderItem();
        Product product = orderItem.getProduct();
        Order order = orderItem.getOrder();

        double refundAmount = orderItem.getUnitPrice() * dispute.getQuantity();

        product.setStock(product.getStock() + dispute.getQuantity());
        if (product.getStatus() == ProductStatus.SOLD_OUT) {
            product.setStatus(ProductStatus.APPROVED);
        }
        productRepository.save(product);

        Payment payment = paymentRepository.findByOrderId(order.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found."));

        if (refundAmount >= payment.getAmount()) {
            payment.setStatus(PaymentStatus.REFUNDED);
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        } else {
            payment.setStatus(PaymentStatus.PARTIALLY_REFUNDED);
        }
        paymentRepository.save(payment);

        return refundAmount;
    }

    private Dispute findDispute(Long id) {
        return disputeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispute not found."));
    }

    private User findActiveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessRuleException("User is inactive.");
        }

        return user;
    }

    private void requireRole(User user, UserRole role, String message) {
        if (user.getRole() != role) {
            throw new BusinessRuleException(message);
        }
    }
}
