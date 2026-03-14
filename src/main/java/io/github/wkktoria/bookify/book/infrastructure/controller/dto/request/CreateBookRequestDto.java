package io.github.wkktoria.bookify.book.infrastructure.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateBookRequestDto(
        @NotNull(message = "bookTitle must not be null")
        @NotEmpty(message = "bookTitle must not be empty")
        String bookTitle,

        @NotNull(message = "author must not be null")
        @NotEmpty(message = "author must not be empty")
        String author,

        @NotNull(message = "publicationDate must not be null")
        LocalDate publicationDate,

        @NotNull(message = "isbn must not be null")
        @NotEmpty(message = "isbn must not be empty")
        String isbn,

        @NotNull(message = "pages must not be null")
        @Min(1)
        Integer pages,

        String language
) {
}
