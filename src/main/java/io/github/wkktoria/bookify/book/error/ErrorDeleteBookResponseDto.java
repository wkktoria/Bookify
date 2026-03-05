package io.github.wkktoria.bookify.book.error;

import org.springframework.http.HttpStatus;

public record ErrorDeleteBookResponseDto(String message, HttpStatus status) {
}
