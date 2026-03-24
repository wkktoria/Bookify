package io.github.wkktoria.bookify.infrastructure.crud.genre.controller;

import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import io.github.wkktoria.bookify.infrastructure.crud.genre.controller.dto.response.GetAllGenresResponsesDto;

import java.util.Set;

public class GenreControllerMapper {

    public static GetAllGenresResponsesDto mapFromGenreDtoSetToGetAllGenresResponseDto(final Set<GenreDto> genreDtoSet) {
        return new GetAllGenresResponsesDto(genreDtoSet);
    }

}
