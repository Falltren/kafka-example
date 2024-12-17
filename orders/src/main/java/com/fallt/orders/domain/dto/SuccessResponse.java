package com.fallt.orders.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponse {

    private String message;

    @Builder.Default
    private Long timestamp = System.currentTimeMillis();
}
