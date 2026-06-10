package com.server.app.marketplace.domain.entities;

import com.server.app.marketplace.common.enums.FeaturedPromotionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "featured_promotions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeaturedPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "visibility_fee", nullable = false)
    private Double visibilityFee;

    @Column(name = "paid", nullable = false)
    private Boolean paid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FeaturedPromotionStatus status;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
