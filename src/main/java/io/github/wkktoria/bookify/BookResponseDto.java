package io.github.wkktoria.bookify;

import java.util.Map;

public record BookResponseDto(Map<Integer, String> books) {
}
