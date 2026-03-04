package io.github.wkktoria.bookify.apivalidation;

import java.util.List;

public record ApiValidationErrorResponseDto(List<String> errors) {
}
