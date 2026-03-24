package io.github.wkktoria.bookify.infrastructure.crud.genre.controller.dto.response;

import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;

import java.util.Set;

public record GetAllGenresResponsesDto(Set<GenreDto> genres) {
}
