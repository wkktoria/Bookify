package io.github.wkktoria.bookify.book.infrastructure.controller.dto.response;

import io.github.wkktoria.bookify.book.domain.model.Book;

import java.util.List;

public record GetAllBooksResponseDto(List<Book> books) {
}
