package io.github.wkktoria.bookify.domain.crud.dto;

import java.util.Set;

public record GenreWithBooksDto(GenreDto genre, Set<BookSimpleDto> books) {
}
