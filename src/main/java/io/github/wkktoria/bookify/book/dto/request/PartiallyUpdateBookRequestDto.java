package io.github.wkktoria.bookify.book.dto.request;

public record PartiallyUpdateBookRequestDto(
        String bookTitle,
        String author) {
}
