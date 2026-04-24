package io.github.wkktoria.bookify.infrastructure.crud.series.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateSeriesWithBookRequestDto(
        @NotNull(message = "name must not be null")
        @NotEmpty(message = "name must not be empty")
        String name,

        @NotNull(message = "bookId must not be null")
        Long bookId
) {
}
