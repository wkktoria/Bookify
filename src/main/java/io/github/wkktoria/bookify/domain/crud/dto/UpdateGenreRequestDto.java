package io.github.wkktoria.bookify.domain.crud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateGenreRequestDto(
        @NotNull
        @NotBlank
        String name
) {
}
