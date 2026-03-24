package io.github.wkktoria.bookify.domain.crud.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record BookDto(
        Long id,
        String title,
        LocalDate publicationDate,
        String isbn,
        Integer pages,
        GenreDto genre,
        Set<AuthorDto> authors) {
}
