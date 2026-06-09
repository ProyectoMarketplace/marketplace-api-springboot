package com.server.app.marketplace.repositories;

import com.server.app.marketplace.common.enums.ReturnStatus;
import com.server.app.marketplace.domain.entities.ProductReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReturnRepository extends JpaRepository<ProductReturn, Long> {

    List<ProductReturn> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);

    List<ProductReturn> findByOrderItem_Product_SellerProfile_User_IdOrderByCreatedAtDesc(Long sellerUserId);

    boolean existsByOrderItemIdAndStatusIn(Long orderItemId, List<ReturnStatus> statuses);
}
