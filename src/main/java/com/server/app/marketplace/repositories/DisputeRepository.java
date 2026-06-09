package com.server.app.marketplace.repositories;

import com.server.app.marketplace.common.enums.DisputeStatus;
import com.server.app.marketplace.domain.entities.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisputeRepository extends JpaRepository<Dispute, Long> {

    List<Dispute> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);

    List<Dispute> findBySellerIdOrderByCreatedAtDesc(Long sellerId);

    List<Dispute> findByStatusInOrderByCreatedAtDesc(List<DisputeStatus> statuses);

    boolean existsByOrderItemIdAndStatusIn(Long orderItemId, List<DisputeStatus> statuses);
}
