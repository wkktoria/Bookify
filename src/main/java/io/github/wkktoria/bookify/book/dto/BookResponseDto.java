package io.github.wkktoria.bookify.book.dto;

import java.util.Map;

public record BookResponseDto(Map<Integer, String> books) {
}
