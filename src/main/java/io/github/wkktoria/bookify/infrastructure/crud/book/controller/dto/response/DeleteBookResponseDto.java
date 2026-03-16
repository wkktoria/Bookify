package io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response;

import org.springframework.http.HttpStatus;

public record DeleteBookResponseDto(String message, HttpStatus status) {
}
