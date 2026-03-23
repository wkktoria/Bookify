package io.github.wkktoria.bookify.domain.crud.dto;

import lombok.Builder;

@Builder
public record SeriesRequestDto(Long bookId, String name) {
}
