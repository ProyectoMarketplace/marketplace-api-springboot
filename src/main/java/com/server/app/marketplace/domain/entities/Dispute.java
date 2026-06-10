package com.server.app.marketplace.domain.entities;

import com.server.app.marketplace.common.enums.DisputeResolution;
import com.server.app.marketplace.common.enums.DisputeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "disputes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dispute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "reason", nullable = false, length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DisputeStatus status;

    @Column(name = "seller_response", length = 1000)
    private String sellerResponse;

    @Enumerated(EnumType.STRING)
    @Column(name = "resolution")
    private DisputeResolution resolution;

    @Column(name = "admin_notes", length = 1000)
    private String adminNotes;

    @ManyToOne
    @JoinColumn(name = "resolved_by_admin_id")
    private User resolvedByAdmin;

    @Column(name = "refund_amount")
    private Double refundAmount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
