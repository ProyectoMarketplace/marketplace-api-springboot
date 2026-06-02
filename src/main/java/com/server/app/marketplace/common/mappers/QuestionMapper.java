package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.request.CreateQuestionRequest;
import com.server.app.marketplace.domain.dto.response.question.QuestionResponse;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.Question;
import com.server.app.marketplace.domain.entities.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class QuestionMapper {

    public Question toEntity(CreateQuestionRequest request, User buyer, Product product) {
        return Question.builder()
                .buyer(buyer)
                .product(product)
                .questionText(request.getQuestionText())
                .answerText(null)
                .answered(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public QuestionResponse toDto(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .buyerId(question.getBuyer().getId())
                .buyerName(question.getBuyer().getFullName())
                .productId(question.getProduct().getId())
                .productTitle(question.getProduct().getTitle())
                .questionText(question.getQuestionText())
                .answerText(question.getAnswerText())
                .answered(question.getAnswered())
                .createdAt(question.getCreatedAt())
                .build();
    }
}