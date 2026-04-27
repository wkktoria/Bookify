package io.github.wkktoria.bookify.infrastructure.crud.book.controller.error;

import io.github.wkktoria.bookify.domain.crud.BookNotFoundException;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.BookRestController;
import io.github.wkktoria.bookify.infrastructure.crud.dto.ErrorResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = BookRestController.class)
@Log4j2
public class BookErrorHandler {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleBookNotFoundException(final BookNotFoundException exception) {
        log.warn("Book not found: {}", exception.getMessage());
        return new ErrorResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

}
