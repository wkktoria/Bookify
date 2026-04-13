package io.github.wkktoria.bookify.infrastructure.usercrud.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

record RegisterUserRequestDto(
        @Email
        @NotBlank
        String username,

        @NotBlank
        String password) {
}
