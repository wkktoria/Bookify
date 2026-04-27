package io.github.wkktoria.bookify.infrastructure.crud.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(String message, HttpStatus status) {
}
