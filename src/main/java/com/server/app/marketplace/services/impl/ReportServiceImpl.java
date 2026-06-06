package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.OrderStatus;
import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.ReportMapper;
import com.server.app.marketplace.domain.dto.response.report.*;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.SellerProfile;
import com.server.app.marketplace.domain.entities.User;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.*;
import com.server.app.marketplace.repositories.projection.ProductSalesAggregate;
import com.server.app.marketplace.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 50;

    private static final List<OrderStatus> COMPLETED_SALE_STATUSES = List.of(
            OrderStatus.PAID,
            OrderStatus.SHIPPED,
            OrderStatus.DELIVERED
    );

    private final UserRepository userRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReportMapper reportMapper;

    @Override
    @Transactional(readOnly = true)
    public MarketplaceDashboardReport getAdminDashboard(
            Long requestingUserId,
            LocalDate from,
            LocalDate to,
            int limit
    ) {
        User requester = findActiveUser(requestingUserId);
        requireAdmin(requester);

        int resolvedLimit = resolveLimit(limit);
        DateRange range = resolveDateRange(from, to);

        return MarketplaceDashboardReport.builder()
                .salesOverview(buildSalesOverview(range))
                .mostViewedProducts(fetchMostViewed(null, resolvedLimit))
                .topSoldProducts(fetchTopSold(null, resolvedLimit))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SalesOverviewReport getSalesOverview(Long requestingUserId, LocalDate from, LocalDate to) {
        User requester = findActiveUser(requestingUserId);
        requireAdmin(requester);
        return buildSalesOverview(resolveDateRange(from, to));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductViewReportItem> getMostViewedProducts(
            Long requestingUserId,
            Long sellerUserId,
            int limit
    ) {
        User requester = findActiveUser(requestingUserId);
        validateReportAccess(requester, sellerUserId);
        return fetchMostViewed(sellerUserId, resolveLimit(limit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSalesReportItem> getTopSoldProducts(
            Long requestingUserId,
            Long sellerUserId,
            int limit
    ) {
        User requester = findActiveUser(requestingUserId);
        validateReportAccess(requester, sellerUserId);
        return fetchTopSold(sellerUserId, resolveLimit(limit));
    }

    @Override
    @Transactional(readOnly = true)
    public SellerReportSummary getSellerSummary(Long requestingUserId, Long sellerUserId, int limit) {
        User requester = findActiveUser(requestingUserId);
        validateReportAccess(requester, sellerUserId);

        SellerProfile profile = sellerProfileRepository.findByUserId(sellerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found."));

        int resolvedLimit = resolveLimit(limit);

        return SellerReportSummary.builder()
                .sellerUserId(sellerUserId)
                .storeName(profile.getStoreName())
                .totalProducts(productRepository.countBySellerProfile_User_Id(sellerUserId))
                .approvedProducts(productRepository.countBySellerProfile_User_IdAndStatus(
                        sellerUserId,
                        ProductStatus.APPROVED
                ))
                .totalRevenue(orderItemRepository.sumRevenueBySeller(COMPLETED_SALE_STATUSES, sellerUserId))
                .totalUnitsSold(orderItemRepository.sumUnitsSoldBySeller(COMPLETED_SALE_STATUSES, sellerUserId))
                .mostViewedProducts(fetchMostViewed(sellerUserId, resolvedLimit))
                .topSoldProducts(fetchTopSold(sellerUserId, resolvedLimit))
                .build();
    }

    private SalesOverviewReport buildSalesOverview(DateRange range) {
        Map<String, Long> usersByRole = new LinkedHashMap<>();
        for (UserRole role : UserRole.values()) {
            usersByRole.put(role.name(), userRepository.countByRole(role));
        }

        Map<String, Long> productsByStatus = new LinkedHashMap<>();
        for (ProductStatus status : ProductStatus.values()) {
            productsByStatus.put(status.name(), (long) productRepository.findByStatus(status).size());
        }

        return SalesOverviewReport.builder()
                .periodFrom(range.from())
                .periodTo(range.toExclusive())
                .totalOrders(orderRepository.countByCreatedAtBetween(range.from(), range.toExclusive()))
                .paidOrders(orderRepository.countByStatus(OrderStatus.PAID))
                .shippedOrders(orderRepository.countByStatus(OrderStatus.SHIPPED))
                .deliveredOrders(orderRepository.countByStatus(OrderStatus.DELIVERED))
                .cancelledOrders(orderRepository.countByStatus(OrderStatus.CANCELLED))
                .totalRevenue(orderRepository.sumFinalTotalByStatusInAndCreatedAtBetween(
                        COMPLETED_SALE_STATUSES,
                        range.from(),
                        range.toExclusive()
                ))
                .usersByRole(usersByRole)
                .productsByStatus(productsByStatus)
                .build();
    }

    private List<ProductViewReportItem> fetchMostViewed(Long sellerUserId, int limit) {
        PageRequest page = PageRequest.of(0, limit);
        List<Product> products = sellerUserId == null
                ? productRepository.findAllByOrderByViewsDesc(page)
                : productRepository.findBySellerProfile_User_IdOrderByViewsDesc(sellerUserId, page);

        return products.stream()
                .map(reportMapper::toViewReportItem)
                .toList();
    }

    private List<ProductSalesReportItem> fetchTopSold(Long sellerUserId, int limit) {
        PageRequest page = PageRequest.of(0, limit);
        List<ProductSalesAggregate> aggregates = sellerUserId == null
                ? orderItemRepository.findTopSoldProducts(COMPLETED_SALE_STATUSES, page)
                : orderItemRepository.findTopSoldProductsBySeller(
                        COMPLETED_SALE_STATUSES,
                        sellerUserId,
                        page
                );

        return aggregates.stream()
                .map(reportMapper::toSalesReportItem)
                .toList();
    }

    private User findActiveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessRuleException("User is inactive.");
        }

        return user;
    }

    private void requireAdmin(User user) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new BusinessRuleException("Only ADMIN users can access platform reports.");
        }
    }

    private void validateReportAccess(User requester, Long sellerUserId) {
        if (requester.getRole() == UserRole.ADMIN) {
            return;
        }

        if (requester.getRole() == UserRole.SELLER) {
            if (sellerUserId == null) {
                throw new BusinessRuleException("Seller reports require a seller user id.");
            }
            if (!requester.getId().equals(sellerUserId)) {
                throw new BusinessRuleException("Sellers can only access their own reports.");
            }
            return;
        }

        throw new BusinessRuleException("Only ADMIN or SELLER users can access reports.");
    }

    private int resolveLimit(int limit) {
        if (limit <= 0) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }

    private DateRange resolveDateRange(LocalDate from, LocalDate to) {
        LocalDate end = to != null ? to : LocalDate.now();
        LocalDate start = from != null ? from : end.minusMonths(1);

        if (start.isAfter(end)) {
            throw new BusinessRuleException("Invalid date range: 'from' must be before or equal to 'to'.");
        }

        return new DateRange(
                start.atStartOfDay(),
                end.plusDays(1).atStartOfDay()
        );
    }

    private record DateRange(LocalDateTime from, LocalDateTime toExclusive) {}
}
