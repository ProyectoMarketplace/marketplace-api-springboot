package com.server.app.marketplace.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageRequest {

    @NotNull(message = "Conversation id is required.")
    private Long conversationId;

    @NotNull(message = "Sender id is required.")
    private Long senderId;

    @NotBlank(message = "Message content is required.")
    private String content;
}