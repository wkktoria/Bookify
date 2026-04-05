package io.github.wkktoria.bookify.infrastructure.security.jwt;

import lombok.Builder;

@Builder
record JwtResponseDto(String token) {
}
