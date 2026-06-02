package com.server.app.marketplace.domain.dto.response.category;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

    private Boolean active;
}