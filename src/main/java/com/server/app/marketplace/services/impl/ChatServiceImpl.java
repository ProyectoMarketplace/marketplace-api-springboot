package com.server.app.marketplace.services.impl;

import com.server.app.marketplace.common.enums.ConversationStatus;
import com.server.app.marketplace.common.enums.ProductStatus;
import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.common.mappers.ChatMapper;
import com.server.app.marketplace.domain.dto.request.CreateConversationRequest;
import com.server.app.marketplace.domain.dto.request.CreateMessageRequest;
import com.server.app.marketplace.domain.dto.response.chat.ConversationResponse;
import com.server.app.marketplace.domain.dto.response.chat.MessageResponse;
import com.server.app.marketplace.domain.entities.Conversation;
import com.server.app.marketplace.domain.entities.Message;
import com.server.app.marketplace.domain.entities.Product;
import com.server.app.marketplace.domain.entities.User;
import com.server.app.marketplace.exceptions.BusinessRuleException;
import com.server.app.marketplace.exceptions.ResourceNotFoundException;
import com.server.app.marketplace.repositories.ConversationRepository;
import com.server.app.marketplace.repositories.MessageRepository;
import com.server.app.marketplace.repositories.ProductRepository;
import com.server.app.marketplace.repositories.UserRepository;
import com.server.app.marketplace.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConversationRepository conversationRepository;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ChatMapper chatMapper;

    @Override
    @Transactional
    public ConversationResponse createConversation(CreateConversationRequest request) {
        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));

        if (buyer.getRole() != UserRole.BUYER) {
            throw new BusinessRuleException("Only BUYER users can start conversations.");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new BusinessRuleException("Only approved products can start conversations.");
        }

        User seller = product.getSellerProfile().getUser();

        if (seller.getId().equals(buyer.getId())) {
            throw new BusinessRuleException("Buyer cannot start a conversation with himself.");
        }

        if (conversationRepository.findByBuyerIdAndProductId(buyer.getId(), product.getId()).isPresent()) {
            throw new BusinessRuleException("Conversation already exists for this buyer and product.");
        }

        Conversation conversation = Conversation.builder()
                .buyer(buyer)
                .seller(seller)
                .product(product)
                .status(ConversationStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .messages(new ArrayList<>())
                .build();

        Conversation savedConversation = conversationRepository.save(conversation);

        return chatMapper.toConversationDto(savedConversation);
    }

    @Override
    public List<ConversationResponse> getConversationsByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return conversationRepository.findByBuyerIdOrSellerId(userId, userId)
                .stream()
                .map(chatMapper::toConversationDto)
                .toList();
    }

    @Override
    @Transactional
    public MessageResponse sendMessage(CreateMessageRequest request) {
        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found."));

        if (conversation.getStatus() != ConversationStatus.OPEN) {
            throw new BusinessRuleException("Only open conversations can receive messages.");
        }

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found."));

        boolean isBuyer = conversation.getBuyer().getId().equals(sender.getId());
        boolean isSeller = conversation.getSeller().getId().equals(sender.getId());

        if (!isBuyer && !isSeller) {
            throw new BusinessRuleException("Sender does not belong to this conversation.");
        }

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(request.getContent())
                .sentAt(LocalDateTime.now())
                .build();

        Message savedMessage = messageRepository.save(message);

        return chatMapper.toMessageDto(savedMessage);
    }

    @Override
    public List<MessageResponse> getMessagesByConversation(Long conversationId) {
        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found."));

        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId)
                .stream()
                .map(chatMapper::toMessageDto)
                .toList();
    }
}