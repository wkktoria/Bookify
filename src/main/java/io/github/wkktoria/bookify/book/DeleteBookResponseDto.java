package io.github.wkktoria.bookify.book;

import org.springframework.http.HttpStatus;

public record DeleteBookResponseDto(String message, HttpStatus status) {
}
