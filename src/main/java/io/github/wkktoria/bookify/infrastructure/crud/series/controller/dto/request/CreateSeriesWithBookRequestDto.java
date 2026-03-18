package io.github.wkktoria.bookify.infrastructure.crud.series.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSeriesWithBookRequestDto(
        @NotNull
        @NotBlank
        String name,

        @NotNull
        Long bookId
) {
}
