package com.server.app.marketplace.domain.entities;

import com.server.app.marketplace.common.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    private Boolean prohibited;

    @Column(nullable = false)
    private Integer views;

    @ManyToOne
    @JoinColumn(name = "seller_profile_id")
    private SellerProfile sellerProfile;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}