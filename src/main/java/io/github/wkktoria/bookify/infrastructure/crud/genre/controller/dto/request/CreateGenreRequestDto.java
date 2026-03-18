package io.github.wkktoria.bookify.infrastructure.crud.genre.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateGenreRequestDto(
        @NotNull
        @NotBlank
        String name
) {
}
