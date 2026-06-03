package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.CreateConversationRequest;
import com.server.app.marketplace.domain.dto.request.CreateMessageRequest;
import com.server.app.marketplace.domain.dto.response.chat.ConversationResponse;
import com.server.app.marketplace.domain.dto.response.chat.MessageResponse;

import java.util.List;

public interface ChatService {

    ConversationResponse createConversation(CreateConversationRequest request);

    List<ConversationResponse> getConversationsByUser(Long userId);

    MessageResponse sendMessage(CreateMessageRequest request);

    List<MessageResponse> getMessagesByConversation(Long conversationId);
}