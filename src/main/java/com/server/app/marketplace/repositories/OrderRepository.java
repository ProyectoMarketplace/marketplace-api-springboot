package com.server.app.marketplace.repositories;

import com.server.app.marketplace.common.enums.OrderStatus;
import com.server.app.marketplace.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerId(Long buyerId);

    long countByStatus(OrderStatus status);

    @Query("""
            SELECT COALESCE(SUM(o.finalTotal), 0)
            FROM Order o
            WHERE o.status IN :statuses
            """)
    Double sumFinalTotalByStatusIn(@Param("statuses") List<OrderStatus> statuses);

    @Query("""
            SELECT COALESCE(SUM(o.finalTotal), 0)
            FROM Order o
            WHERE o.status IN :statuses
              AND o.createdAt >= :from
              AND o.createdAt < :to
            """)
    Double sumFinalTotalByStatusInAndCreatedAtBetween(
            @Param("statuses") List<OrderStatus> statuses,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
            SELECT COUNT(o)
            FROM Order o
            WHERE o.createdAt >= :from
              AND o.createdAt < :to
            """)
    long countByCreatedAtBetween(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
