package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.OrderStatus;
import com.server.app.marketplace.common.enums.PaymentStatus;
import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.enums.ReturnStatus;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.ReturnMapper;
import com.server.app.marketplace.domain.dto.request.CreateReturnRequest;
import com.server.app.marketplace.domain.dto.request.ProcessReturnRefundRequest;
import com.server.app.marketplace.domain.dto.request.RejectReturnRequest;
import com.server.app.marketplace.domain.dto.response.return_.ReturnResponse;
import com.server.app.marketplace.domain.entities.*;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.*;
import com.server.app.marketplace.services.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReturnServiceImpl implements ReturnService {

    private static final List<ReturnStatus> ACTIVE_RETURN_STATUSES = List.of(
            ReturnStatus.REQUESTED,
            ReturnStatus.APPROVED
    );

    private final ProductReturnRepository productReturnRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final ReturnMapper returnMapper;

    @Override
    @Transactional
    public ReturnResponse createReturn(CreateReturnRequest request) {
        User buyer = findActiveUser(request.getBuyerId());
        requireRole(buyer, UserRole.BUYER, "Only BUYER users can request returns.");

        OrderItem orderItem = orderItemRepository.findById(request.getOrderItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found."));

        Order order = orderItem.getOrder();

        if (!order.getBuyer().getId().equals(buyer.getId())) {
            throw new BusinessRuleException("This order does not belong to the buyer.");
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new BusinessRuleException("Only DELIVERED orders can be returned.");
        }

        if (request.getQuantity() > orderItem.getQuantity()) {
            throw new BusinessRuleException("Return quantity cannot exceed purchased quantity.");
        }

        if (productReturnRepository.existsByOrderItemIdAndStatusIn(
                orderItem.getId(),
                ACTIVE_RETURN_STATUSES
        )) {
            throw new BusinessRuleException("This order item already has an active return request.");
        }

        LocalDateTime now = LocalDateTime.now();

        ProductReturn productReturn = ProductReturn.builder()
                .orderItem(orderItem)
                .buyer(buyer)
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .status(ReturnStatus.REQUESTED)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return returnMapper.toDto(productReturnRepository.save(productReturn));
    }

    @Override
    @Transactional(readOnly = true)
    public ReturnResponse getReturnById(Long id) {
        return returnMapper.toDto(findReturn(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReturnResponse> getReturnsByBuyer(Long buyerId) {
        findActiveUser(buyerId);
        return productReturnRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId)
                .stream()
                .map(returnMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReturnResponse> getReturnsBySeller(Long sellerUserId) {
        User seller = findActiveUser(sellerUserId);
        requireRole(seller, UserRole.SELLER, "Only SELLER users can view seller returns.");

        return productReturnRepository
                .findByOrderItem_Product_SellerProfile_User_IdOrderByCreatedAtDesc(sellerUserId)
                .stream()
                .map(returnMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ReturnResponse approveReturn(Long returnId, Long sellerUserId) {
        User seller = findActiveUser(sellerUserId);
        requireRole(seller, UserRole.SELLER, "Only SELLER users can approve returns.");

        ProductReturn productReturn = findReturn(returnId);
        validateSellerOwnership(productReturn, seller.getId());

        if (productReturn.getStatus() != ReturnStatus.REQUESTED) {
            throw new BusinessRuleException("Only REQUESTED returns can be approved.");
        }

        productReturn.setStatus(ReturnStatus.APPROVED);
        productReturn.setUpdatedAt(LocalDateTime.now());

        return returnMapper.toDto(productReturnRepository.save(productReturn));
    }

    @Override
    @Transactional
    public ReturnResponse rejectReturn(Long returnId, RejectReturnRequest request) {
        User seller = findActiveUser(request.getSellerUserId());
        requireRole(seller, UserRole.SELLER, "Only SELLER users can reject returns.");

        ProductReturn productReturn = findReturn(returnId);
        validateSellerOwnership(productReturn, seller.getId());

        if (productReturn.getStatus() != ReturnStatus.REQUESTED) {
            throw new BusinessRuleException("Only REQUESTED returns can be rejected.");
        }

        productReturn.setStatus(ReturnStatus.REJECTED);
        productReturn.setSellerResponse(request.getSellerResponse());
        productReturn.setUpdatedAt(LocalDateTime.now());

        return returnMapper.toDto(productReturnRepository.save(productReturn));
    }

    @Override
    @Transactional
    public ReturnResponse processRefund(Long returnId, ProcessReturnRefundRequest request) {
        User seller = findActiveUser(request.getSellerUserId());
        requireRole(seller, UserRole.SELLER, "Only SELLER users can process refunds.");

        ProductReturn productReturn = findReturn(returnId);
        validateSellerOwnership(productReturn, seller.getId());

        if (productReturn.getStatus() != ReturnStatus.APPROVED) {
            throw new BusinessRuleException("Only APPROVED returns can be refunded.");
        }

        OrderItem orderItem = productReturn.getOrderItem();
        Product product = orderItem.getProduct();
        Order order = orderItem.getOrder();

        double refundAmount = orderItem.getUnitPrice() * productReturn.getQuantity();
        productReturn.setRefundAmount(refundAmount);
        productReturn.setStatus(ReturnStatus.REFUNDED);
        productReturn.setUpdatedAt(LocalDateTime.now());

        product.setStock(product.getStock() + productReturn.getQuantity());
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

        ProductReturn savedReturn = productReturnRepository.save(productReturn);
        return returnMapper.toDto(savedReturn);
    }

    @Override
    @Transactional
    public ReturnResponse cancelReturn(Long returnId, Long buyerId) {
        User buyer = findActiveUser(buyerId);
        requireRole(buyer, UserRole.BUYER, "Only BUYER users can cancel returns.");

        ProductReturn productReturn = findReturn(returnId);

        if (!productReturn.getBuyer().getId().equals(buyer.getId())) {
            throw new BusinessRuleException("This return does not belong to the buyer.");
        }

        if (productReturn.getStatus() != ReturnStatus.REQUESTED) {
            throw new BusinessRuleException("Only REQUESTED returns can be cancelled.");
        }

        productReturn.setStatus(ReturnStatus.CANCELLED);
        productReturn.setUpdatedAt(LocalDateTime.now());

        return returnMapper.toDto(productReturnRepository.save(productReturn));
    }

    private ProductReturn findReturn(Long id) {
        return productReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Return not found."));
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

    private void validateSellerOwnership(ProductReturn productReturn, Long sellerUserId) {
        Long productOwnerUserId = productReturn.getOrderItem()
                .getProduct()
                .getSellerProfile()
                .getUser()
                .getId();

        if (!productOwnerUserId.equals(sellerUserId)) {
            throw new BusinessRuleException("This seller cannot manage returns for this product.");
        }
    }
}
