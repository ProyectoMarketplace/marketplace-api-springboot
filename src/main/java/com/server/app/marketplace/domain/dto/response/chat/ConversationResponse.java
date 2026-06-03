package com.server.app.marketplace.domain.dto.response.chat;

import com.server.app.marketplace.common.enums.ConversationStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponse {

    private Long id;

    private Long buyerId;

    private String buyerName;

    private Long sellerId;

    private String sellerName;

    private Long productId;

    private String productTitle;

    private ConversationStatus status;

    private LocalDateTime createdAt;

    private List<MessageResponse> messages;
}