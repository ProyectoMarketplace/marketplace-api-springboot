package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.QuestionMapper;
import com.server.app.marketplace.domain.dto.request.AnswerQuestionRequest;
import com.server.app.marketplace.domain.dto.request.CreateQuestionRequest;
import com.server.app.marketplace.domain.dto.response.question.QuestionResponse;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.Question;
import com.server.app.marketplace.domain.entities.User;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.ProductRepository;
import com.server.app.marketplace.repositories.QuestionRepository;
import com.server.app.marketplace.repositories.UserRepository;
import com.server.app.marketplace.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final QuestionMapper questionMapper;

    @Override
    @Transactional
    public QuestionResponse createQuestion(CreateQuestionRequest request) {
        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        if (buyer.getRole() != UserRole.BUYER) {
            throw new BusinessRuleException("Only BUYER users can ask questions.");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new BusinessRuleException("Only approved products can receive questions.");
        }

        Question question = questionMapper.toEntity(request, buyer, product);
        Question savedQuestion = questionRepository.save(question);

        return questionMapper.toDto(savedQuestion);
    }

    @Override
    public List<QuestionResponse> getQuestionsByProductId(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        return questionRepository.findByProductId(productId)
                .stream()
                .map(questionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public QuestionResponse answerQuestion(Long questionId, AnswerQuestionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found."));

        User seller = userRepository.findById(request.getSellerUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found."));

        if (seller.getRole() != UserRole.SELLER) {
            throw new BusinessRuleException("Only SELLER users can answer questions.");
        }

        Long productOwnerUserId = question.getProduct()
                .getSellerProfile()
                .getUser()
                .getId();

        if (!productOwnerUserId.equals(seller.getId())) {
            throw new BusinessRuleException("This seller cannot answer questions for this product.");
        }

        if (question.getAnswered()) {
            throw new BusinessRuleException("This question has already been answered.");
        }

        question.setAnswerText(request.getAnswerText());
        question.setAnswered(true);

        Question answeredQuestion = questionRepository.save(question);

        return questionMapper.toDto(answeredQuestion);
    }
}