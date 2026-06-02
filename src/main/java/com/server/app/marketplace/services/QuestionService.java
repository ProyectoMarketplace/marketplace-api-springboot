package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.AnswerQuestionRequest;
import com.server.app.marketplace.domain.dto.request.CreateQuestionRequest;
import com.server.app.marketplace.domain.dto.response.question.QuestionResponse;

import java.util.List;

public interface QuestionService {

    QuestionResponse createQuestion(CreateQuestionRequest request);

    List<QuestionResponse> getQuestionsByProductId(Long productId);

    QuestionResponse answerQuestion(Long questionId, AnswerQuestionRequest request);
}