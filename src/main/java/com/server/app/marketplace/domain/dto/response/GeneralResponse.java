package com.server.app.marketplace.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse {

    private String uri;

    private String message;

    private int status;

    private LocalDateTime time;

    private Object data;
}