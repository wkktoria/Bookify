package io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response;

import io.github.wkktoria.bookify.domain.crud.dto.BookDto;

import java.util.List;

public record GetAllBooksResponseDto(List<BookDto> books) {
}
