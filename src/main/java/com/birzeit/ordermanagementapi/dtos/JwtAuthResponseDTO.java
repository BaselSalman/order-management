package com.birzeit.ordermanagementapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtAuthResponseDTO {

    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
