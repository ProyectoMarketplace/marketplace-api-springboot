package com.server.app.marketplace.domain.dto.response.chat;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private Long id;

    private Long conversationId;

    private Long senderId;

    private String senderName;

    private String content;

    private LocalDateTime sentAt;
}