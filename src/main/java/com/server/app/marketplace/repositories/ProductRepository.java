package com.server.app.marketplace.repositories;

import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(ProductStatus status);

    List<Product> findAllByOrderByViewsDesc(Pageable pageable);

    List<Product> findBySellerProfile_User_IdOrderByViewsDesc(Long sellerUserId, Pageable pageable);

    long countBySellerProfile_User_Id(Long sellerUserId);

    long countBySellerProfile_User_IdAndStatus(Long sellerUserId, ProductStatus status);
}