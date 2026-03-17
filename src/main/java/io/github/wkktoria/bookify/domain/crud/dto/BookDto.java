package io.github.wkktoria.bookify.domain.crud.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BookDto(
        Long id,
        String title,
        String author,
        LocalDate publicationDate,
        String isbn,
        Integer pages) {
}
