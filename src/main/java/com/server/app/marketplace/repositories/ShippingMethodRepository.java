package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {

    List<ShippingMethod> findByActiveTrue();

    boolean existsByNameIgnoreCase(String name);
}