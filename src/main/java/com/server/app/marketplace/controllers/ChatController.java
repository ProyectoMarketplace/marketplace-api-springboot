package com.server.app.marketplace.controllers;

import com.server.app.marketplace.domain.dto.request.CreateConversationRequest;
import com.server.app.marketplace.domain.dto.request.CreateMessageRequest;
import com.server.app.marketplace.domain.dto.response.GeneralResponse;
import com.server.app.marketplace.services.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/api/conversations")
    public ResponseEntity<GeneralResponse> createConversation(
            @Valid @RequestBody CreateConversationRequest request
    ) {
        return buildResponse(
                "Conversation created successfully.",
                HttpStatus.CREATED,
                chatService.createConversation(request)
        );
    }

    @GetMapping("/api/conversations/user/{userId}")
    public ResponseEntity<GeneralResponse> getConversationsByUser(@PathVariable Long userId) {
        return buildResponse(
                "Conversations found.",
                HttpStatus.OK,
                chatService.getConversationsByUser(userId)
        );
    }

    @PostMapping("/api/messages")
    public ResponseEntity<GeneralResponse> sendMessage(
            @Valid @RequestBody CreateMessageRequest request
    ) {
        return buildResponse(
                "Message sent successfully.",
                HttpStatus.CREATED,
                chatService.sendMessage(request)
        );
    }

    @GetMapping("/api/messages/conversation/{conversationId}")
    public ResponseEntity<GeneralResponse> getMessagesByConversation(@PathVariable Long conversationId) {
        return buildResponse(
                "Messages found.",
                HttpStatus.OK,
                chatService.getMessagesByConversation(conversationId)
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