package io.github.wkktoria.bookify.book.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateBookRequestDto(
        @NotNull(message = "bookTitle must not be null")
        @NotEmpty(message = "bookTitle must not be empty")
        String bookTitle,

        @NotNull(message = "author must not be null")
        @NotEmpty(message = "author must not be empty")
        String author
) {
}
