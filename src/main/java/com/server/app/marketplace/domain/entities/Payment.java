package com.server.app.marketplace.domain.entities;

import com.server.app.marketplace.common.enums.PaymentMethod;
import com.server.app.marketplace.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "transaction_reference", nullable = false)
    private String transactionReference;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;
}