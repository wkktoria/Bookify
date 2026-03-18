package io.github.wkktoria.bookify.infrastructure.crud.author.controller.dto.response;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;

import java.util.List;

public record AllAuthorsResponseDto(List<AuthorDto> authors) {
}
