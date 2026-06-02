package com.server.app.marketplace.domain.entities;

import com.server.app.marketplace.common.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "shipping_method_id", nullable = false)
    private ShippingMethod shippingMethod;

    @Column(name = "cost", nullable = false)
    private Double cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ShipmentStatus status;

    @Column(name = "tracking_code", nullable = false)
    private String trackingCode;
}