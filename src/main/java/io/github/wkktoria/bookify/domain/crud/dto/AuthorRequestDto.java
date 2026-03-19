package io.github.wkktoria.bookify.domain.crud.dto;

import lombok.Builder;

@Builder
public record AuthorRequestDto(String firstname, String lastname) {
}
