package io.github.wkktoria.bookify.domain.crud.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BookSimpleDto(
        Long id,
        String title,
        LocalDate publicationDate,
        String isbn,
        Integer pages
) {
}
