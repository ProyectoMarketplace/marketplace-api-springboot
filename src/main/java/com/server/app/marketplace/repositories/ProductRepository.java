package com.server.app.marketplace.repositories;

import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(ProductStatus status);
}