package io.github.wkktoria.bookify.domain.usercrud;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserDto(String email, Set<String> roles) {
}
