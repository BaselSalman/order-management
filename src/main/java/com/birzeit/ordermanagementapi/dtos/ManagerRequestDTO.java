package com.birzeit.ordermanagementapi.dtos;

import jakarta.validation.constraints.NotBlank;

public record ManagerRequestDTO(@NotBlank(message = "Email must not be blank") String email,
                                @NotBlank(message = "Username must not be blank") String username,
                                @NotBlank(message = "Password must not be blank") String password) {
}
