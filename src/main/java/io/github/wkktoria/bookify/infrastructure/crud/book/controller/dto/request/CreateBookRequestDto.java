package io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.request;

import io.github.wkktoria.bookify.domain.crud.dto.BookLanguageDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateBookRequestDto(
        @NotNull(message = "bookTitle must not be null")
        @NotEmpty(message = "bookTitle must not be empty")
        String bookTitle,

        @NotNull(message = "publicationDate must not be null")
        LocalDate publicationDate,

        @NotNull(message = "isbn must not be null")
        @NotEmpty(message = "isbn must not be empty")
        String isbn,

        @NotNull(message = "pages must not be null")
        @Min(1)
        Integer pages,

        @NotNull(message = "authorId must not be null")
        Long authorId,

        @NotNull(message = "language must not be null")
        BookLanguageDto language
) {
}
