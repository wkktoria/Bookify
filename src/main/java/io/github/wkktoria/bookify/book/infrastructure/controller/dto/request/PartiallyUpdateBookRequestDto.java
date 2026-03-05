package io.github.wkktoria.bookify.book.infrastructure.controller.dto.request;

public record PartiallyUpdateBookRequestDto(
        String bookTitle,
        String author) {
}
