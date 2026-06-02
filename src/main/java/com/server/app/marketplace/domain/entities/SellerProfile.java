package com.server.app.marketplace.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seller_profiles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name", nullable = false, unique = true)
    private String storeName;

    @Column(name = "identity_verified", nullable = false)
    private Boolean identityVerified;

    @Column(name = "commission_rate", nullable = false)
    private Double commissionRate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}