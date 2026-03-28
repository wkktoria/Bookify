package io.github.wkktoria.bookify.domain.crud.dto;

import java.util.Set;

public record AuthorWithBooksDto(AuthorDto author, Set<BookSimpleDto> books) {
}
