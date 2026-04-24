package io.github.wkktoria.bookify.infrastructure.crud.author.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAuthorRequestDto(
        @NotNull(message = "firstname must not be null")
        @NotEmpty(message = "firstname must not be empty")
        String firstname,

        @NotNull(message = "lastname must not be null")
        @NotEmpty(message = "lastname must not be empty")
        String lastname
) {
}
