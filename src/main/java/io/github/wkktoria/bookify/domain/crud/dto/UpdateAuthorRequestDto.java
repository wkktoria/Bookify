package io.github.wkktoria.bookify.domain.crud.dto;

import lombok.Builder;

@Builder
public record UpdateAuthorRequestDto(
        String firstname,
        String lastname) {
}
