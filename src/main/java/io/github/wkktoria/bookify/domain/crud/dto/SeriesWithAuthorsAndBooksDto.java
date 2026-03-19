package io.github.wkktoria.bookify.domain.crud.dto;

import java.util.Set;

public record SeriesWithAuthorsAndBooksDto(
        SeriesDto series,
        Set<AuthorDto> authors,
        Set<BookDto> books
) {
}
