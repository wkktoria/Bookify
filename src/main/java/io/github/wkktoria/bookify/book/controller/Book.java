package io.github.wkktoria.bookify.book.controller;

import lombok.Builder;

@Builder
public record Book(String title, String author) {
}
