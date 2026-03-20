package io.github.wkktoria.bookify.domain.crud.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BookRequestDto(
        String title,
        LocalDate publicationDate,
        String isbn,
        Integer pages,
        Long authorId,
        BookLanguageDto language
) {
}
