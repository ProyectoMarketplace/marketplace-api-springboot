package com.server.app.marketplace.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private String uri;

    private Object message;

    private int status;

    private LocalDateTime time;
}