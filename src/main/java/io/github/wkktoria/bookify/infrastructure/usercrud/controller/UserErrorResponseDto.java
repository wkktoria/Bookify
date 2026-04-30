package io.github.wkktoria.bookify.infrastructure.usercrud.controller;

import org.springframework.http.HttpStatus;

record UserErrorResponseDto(String message, HttpStatus status) {
}
