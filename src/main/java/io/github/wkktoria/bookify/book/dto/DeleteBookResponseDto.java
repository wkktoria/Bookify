package io.github.wkktoria.bookify.book.dto;

import org.springframework.http.HttpStatus;

public record DeleteBookResponseDto(String message, HttpStatus status) {
}
