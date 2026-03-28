package io.github.wkktoria.bookify.infrastructure.crud.series.controller.dto.response;

import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;

import java.util.List;

public record GetAllSeriesResponseDto(List<SeriesDto> series) {
}
