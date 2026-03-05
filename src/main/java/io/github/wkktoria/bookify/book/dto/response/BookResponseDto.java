package io.github.wkktoria.bookify.book.dto.response;

import io.github.wkktoria.bookify.book.controller.Book;

import java.util.Map;

public record BookResponseDto(Map<Integer, Book> books) {
}
