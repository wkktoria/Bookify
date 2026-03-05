package io.github.wkktoria.bookify.book.error;

import org.springframework.http.HttpStatus;

public record ErrorBookResponseDto(String message, HttpStatus status) {
}
