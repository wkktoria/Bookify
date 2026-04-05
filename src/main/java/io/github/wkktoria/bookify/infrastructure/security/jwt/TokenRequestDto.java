package io.github.wkktoria.bookify.infrastructure.security.jwt;

import jakarta.validation.constraints.NotBlank;

record TokenRequestDto(
        @NotBlank
        String username,

        @NotBlank
        String password) {
}
