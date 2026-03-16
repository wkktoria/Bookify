package io.github.wkktoria.bookify.infrastructure.crud.book.controller.error;

import org.springframework.http.HttpStatus;

public record ErrorBookResponseDto(String message, HttpStatus status) {
}
