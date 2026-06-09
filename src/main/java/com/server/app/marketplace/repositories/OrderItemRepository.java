package com.server.app.marketplace.repositories;

import com.server.app.marketplace.common.enums.OrderStatus;
import com.server.app.marketplace.domain.entities.OrderItem;
import com.server.app.marketplace.repositories.projection.ProductSalesAggregate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
            SELECT p.id AS productId,
                   p.title AS productTitle,
                   p.views AS views,
                   SUM(oi.quantity) AS totalQuantitySold,
                   SUM(oi.subtotal) AS totalRevenue
            FROM OrderItem oi
            JOIN oi.product p
            JOIN oi.order o
            WHERE o.status IN :statuses
            GROUP BY p.id, p.title, p.views
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<ProductSalesAggregate> findTopSoldProducts(
            @Param("statuses") List<OrderStatus> statuses,
            Pageable pageable
    );

    @Query("""
            SELECT p.id AS productId,
                   p.title AS productTitle,
                   p.views AS views,
                   SUM(oi.quantity) AS totalQuantitySold,
                   SUM(oi.subtotal) AS totalRevenue
            FROM OrderItem oi
            JOIN oi.product p
            JOIN oi.order o
            WHERE o.status IN :statuses
              AND p.sellerProfile.user.id = :sellerUserId
            GROUP BY p.id, p.title, p.views
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<ProductSalesAggregate> findTopSoldProductsBySeller(
            @Param("statuses") List<OrderStatus> statuses,
            @Param("sellerUserId") Long sellerUserId,
            Pageable pageable
    );

    @Query("""
            SELECT COALESCE(SUM(oi.subtotal), 0)
            FROM OrderItem oi
            JOIN oi.order o
            JOIN oi.product p
            WHERE o.status IN :statuses
              AND p.sellerProfile.user.id = :sellerUserId
            """)
    Double sumRevenueBySeller(
            @Param("statuses") List<OrderStatus> statuses,
            @Param("sellerUserId") Long sellerUserId
    );

    @Query("""
            SELECT COALESCE(SUM(oi.quantity), 0)
            FROM OrderItem oi
            JOIN oi.order o
            JOIN oi.product p
            WHERE o.status IN :statuses
              AND p.sellerProfile.user.id = :sellerUserId
            """)
    Long sumUnitsSoldBySeller(
            @Param("statuses") List<OrderStatus> statuses,
            @Param("sellerUserId") Long sellerUserId
    );
}
