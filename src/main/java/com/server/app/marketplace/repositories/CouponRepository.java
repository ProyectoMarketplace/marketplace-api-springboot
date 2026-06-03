package com.server.app.marketplace.repositories;

import com.server.app.marketplace.domain.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsByCodeIgnoreCase(String code);

    Optional<Coupon> findByCodeIgnoreCase(String code);

    List<Coupon> findByActiveTrue();

    List<Coupon> findByActiveTrueAndEndDateGreaterThanEqual(LocalDate date);
}