package io.github.wkktoria.bookify.book.infrastructure.controller.dto.response;

import io.github.wkktoria.bookify.book.domain.model.Book;

import java.util.Map;

public record GetAllBooksResponseDto(Map<Integer, Book> books) {
}
