package io.github.wkktoria.bookify.book;

import java.util.Map;

public record BookResponseDto(Map<Integer, String> books) {
}
