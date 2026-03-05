package io.github.wkktoria.bookify.book.domain.model;

import lombok.Builder;

@Builder
public record Book(String title, String author) {
}
