package io.github.wkktoria.bookify.infrastructure.usercrud.controller;

import jakarta.validation.constraints.NotBlank;

record RegisterUserRequestDto(
        @NotBlank
        String username,

        @NotBlank
        String password) {
}
