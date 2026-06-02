package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.AnswerQuestionRequest;
import com.server.app.marketplace.domain.dto.request.CreateQuestionRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/api/questions")
    public ResponseEntity<GeneralResponse> createQuestion(
            @Valid @RequestBody CreateQuestionRequest request
    ) {
        return buildResponse(
                "Question created successfully.",
                HttpStatus.CREATED,
                questionService.createQuestion(request)
        );
    }

    @GetMapping("/api/products/{productId}/questions")
    public ResponseEntity<GeneralResponse> getQuestionsByProductId(@PathVariable Long productId) {
        return buildResponse(
                "Questions found.",
                HttpStatus.OK,
                questionService.getQuestionsByProductId(productId)
        );
    }

    @PatchMapping("/api/questions/{id}/answer")
    public ResponseEntity<GeneralResponse> answerQuestion(
            @PathVariable Long id,
            @Valid @RequestBody AnswerQuestionRequest request
    ) {
        return buildResponse(
                "Question answered successfully.",
                HttpStatus.OK,
                questionService.answerQuestion(id, request)
        );
    }

    public ResponseEntity<GeneralResponse> buildResponse(
            String message,
            HttpStatus status,
            Object data
    ) {
        String uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .getPath();

        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .data(data)
                        .build());
    }
}