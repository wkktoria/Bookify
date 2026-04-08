package io.github.wkktoria.bookify.infrastructure.usercrud.controller;

import io.github.wkktoria.bookify.domain.usercrud.UserDto;

import java.util.Set;

record AllUsersResponseDto(Set<UserDto> users) {
}
