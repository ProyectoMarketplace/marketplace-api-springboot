package com.server.app.marketplace.domain.dto.response.question;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {

    private Long id;

    private Long buyerId;

    private String buyerName;

    private Long productId;

    private String productTitle;

    private String questionText;

    private String answerText;

    private Boolean answered;

    private LocalDateTime createdAt;
}