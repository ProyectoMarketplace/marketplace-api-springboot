package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerQuestionRequest {

    @NotNull(message = "Seller user id is required.")
    private Long sellerUserId;

    @NotBlank(message = "Answer text is required.")
    private String answerText;
}