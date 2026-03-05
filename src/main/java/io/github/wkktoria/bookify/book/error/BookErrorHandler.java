package io.github.wkktoria.bookify.book.error;

import io.github.wkktoria.bookify.book.controller.BookRestController;
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
    public ErrorDeleteBookResponseDto handleException(BookNotFoundException exception) {
        log.warn("Error while deleting book: {}", exception.getMessage());
        return new ErrorDeleteBookResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

}
