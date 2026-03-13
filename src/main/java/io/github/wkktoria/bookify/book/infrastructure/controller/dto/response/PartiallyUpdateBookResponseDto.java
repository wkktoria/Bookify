package io.github.wkktoria.bookify.book.infrastructure.controller.dto.response;

import io.github.wkktoria.bookify.book.infrastructure.controller.dto.BookDto;

public record PartiallyUpdateBookResponseDto(BookDto updatedBook) {
}
