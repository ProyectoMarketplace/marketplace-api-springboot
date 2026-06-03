package com.server.app.marketplace.common.mappers;

import com.server.app.marketplace.domain.dto.response.chat.ConversationResponse;
import com.server.app.marketplace.domain.dto.response.chat.MessageResponse;
import com.server.app.marketplace.domain.entities.Conversation;
import com.server.app.marketplace.domain.entities.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMapper {

    public MessageResponse toMessageDto(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getFullName())
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .build();
    }

    public ConversationResponse toConversationDto(Conversation conversation) {
        List<MessageResponse> messages = conversation.getMessages() == null
                ? List.of()
                : conversation.getMessages()
                .stream()
                .map(m -> this.toMessageDto((Message) m))
                .toList();

        return ConversationResponse.builder()
                .id(conversation.getId())
                .buyerId(conversation.getBuyer().getId())
                .buyerName(conversation.getBuyer().getFullName())
                .sellerId(conversation.getSeller().getId())
                .sellerName(conversation.getSeller().getFullName())
                .productId(conversation.getProduct().getId())
                .productTitle(conversation.getProduct().getTitle())
                .status(conversation.getStatus())
                .createdAt(conversation.getCreatedAt())
                .messages(messages)
                .build();
    }
}