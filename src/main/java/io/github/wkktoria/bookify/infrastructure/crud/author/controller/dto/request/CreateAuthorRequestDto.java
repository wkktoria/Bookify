package io.github.wkktoria.bookify.infrastructure.crud.author.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAuthorRequestDto(
        @NotNull
        @NotBlank
        String firstname,

        @NotNull
        @NotBlank
        String lastname
) {
}
