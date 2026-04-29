package io.github.wkktoria.bookify.infrastructure.crud.author.controller.error;

import io.github.wkktoria.bookify.domain.crud.AuthorNotFoundException;
import io.github.wkktoria.bookify.infrastructure.crud.author.controller.AuthorRestController;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.BookRestController;
import io.github.wkktoria.bookify.infrastructure.crud.dto.ErrorResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = {AuthorRestController.class, BookRestController.class})
@Log4j2
class AuthorErrorHandler {

    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponseDto handleAuthorNotFoundException(final AuthorNotFoundException exception) {
        log.warn("Author not found: {}", exception.getMessage());
        return new ErrorResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

}
