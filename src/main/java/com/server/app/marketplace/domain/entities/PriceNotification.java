package com.server.app.marketplace.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "price_notifications")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "previous_price", nullable = false)
    private Double previousPrice;

    @Column(name = "new_price", nullable = false)
    private Double newPrice;

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Column(name = "read", nullable = false)
    private Boolean read;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
