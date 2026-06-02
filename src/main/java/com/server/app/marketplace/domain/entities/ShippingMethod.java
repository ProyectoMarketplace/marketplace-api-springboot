package com.server.app.marketplace.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipping_methods")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "base_cost", nullable = false)
    private Double baseCost;

    @Column(name = "active", nullable = false)
    private Boolean active;
}