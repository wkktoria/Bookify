package io.github.wkktoria.bookify.book.infrastructure.controller.dto.response;

import org.springframework.http.HttpStatus;

public record DeleteBookResponseDto(String message, HttpStatus status) {
}
