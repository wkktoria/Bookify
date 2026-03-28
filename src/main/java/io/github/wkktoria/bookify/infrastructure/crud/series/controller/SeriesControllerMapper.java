package io.github.wkktoria.bookify.infrastructure.crud.series.controller;

import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import io.github.wkktoria.bookify.infrastructure.crud.series.controller.dto.response.GetAllSeriesResponseDto;

import java.util.List;

class SeriesControllerMapper {

    static GetAllSeriesResponseDto mapFromSeriesDtoListToGetAllSeriesResponseDto(final List<SeriesDto> seriesDto) {
        return new GetAllSeriesResponseDto(seriesDto);
    }

}
