package io.github.wkktoria.bookify.book.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequestDto(
        @NotNull(message = "bookTitle must not be null")
        @NotEmpty(message = "bookTitle must not be empty")
        String bookTitle
) {
}
