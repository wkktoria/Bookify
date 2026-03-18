package io.github.wkktoria.bookify.domain.crud.dto;

import java.time.LocalDate;

public record BookRequestDto(
        String title,
        LocalDate publicationDate,
        String isbn,
        Integer pages,
        Long authorId,
        BookLanguageDto language
) {
}
