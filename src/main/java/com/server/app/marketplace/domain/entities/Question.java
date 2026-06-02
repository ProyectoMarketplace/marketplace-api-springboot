package com.server.app.marketplace.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "question_text", nullable = false, length = 1000)
    private String questionText;

    @Column(name = "answer_text", length = 1000)
    private String answerText;

    @Column(name = "answered", nullable = false)
    private Boolean answered;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}