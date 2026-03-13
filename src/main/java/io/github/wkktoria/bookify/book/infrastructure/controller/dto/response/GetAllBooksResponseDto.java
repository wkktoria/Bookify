package io.github.wkktoria.bookify.book.infrastructure.controller.dto.response;

import io.github.wkktoria.bookify.book.infrastructure.controller.dto.BookDto;

import java.util.List;

public record GetAllBooksResponseDto(List<BookDto> books) {
}
