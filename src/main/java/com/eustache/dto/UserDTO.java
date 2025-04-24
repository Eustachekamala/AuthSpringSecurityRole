package com.eustache.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDTO(
        @NotEmpty(message = "The username should not be empty")
        String username,
        @NotEmpty(message = "The email should not be empty")
        String email,
        @NotEmpty(message = "The password should not be empty")
        String password
) {
}
