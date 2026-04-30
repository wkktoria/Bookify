package io.github.wkktoria.bookify.infrastructure.usercrud.controller;

import io.github.wkktoria.bookify.domain.usercrud.UserExistsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = {RegisterController.class})
@Log4j2
class UserErrorHandler {

    @ExceptionHandler(UserExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    UserErrorResponseDto handleUserExistsException(final UserExistsException exception) {
        log.error("User already exists: {}", exception.getMessage());
        return new UserErrorResponseDto(exception.getMessage(), HttpStatus.CONFLICT);
    }

}
