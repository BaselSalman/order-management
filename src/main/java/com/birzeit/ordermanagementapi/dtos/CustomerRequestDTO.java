package com.birzeit.ordermanagementapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

public record CustomerRequestDTO (@NotBlank(message = "First Name must not be blank") String firstName,
                                  @NotBlank(message = "Last Name must not be blank") String lastName,
                                  @NotBlank(message = "Born Date must not be blank") String bornAt,
                                  @NotBlank(message = "Username must not be blank") String username,
                                  @NotBlank(message = "Password must not be blank") String password) {

}
