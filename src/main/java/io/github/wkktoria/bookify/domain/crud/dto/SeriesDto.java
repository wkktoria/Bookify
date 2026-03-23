package io.github.wkktoria.bookify.domain.crud.dto;

import lombok.Builder;

@Builder
public record SeriesDto(Long id, String name) {
}
