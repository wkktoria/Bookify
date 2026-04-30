package io.github.wkktoria.bookify.infrastructure.apivalidation;

import io.github.wkktoria.bookify.infrastructure.crud.author.controller.AuthorRestController;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.BookRestController;
import io.github.wkktoria.bookify.infrastructure.crud.genre.controller.GenreRestController;
import io.github.wkktoria.bookify.infrastructure.crud.series.controller.SeriesRestController;
import io.github.wkktoria.bookify.infrastructure.usercrud.controller.RegisterController;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice(assignableTypes = {
        BookRestController.class,
        AuthorRestController.class,
        GenreRestController.class,
        SeriesRestController.class,
        RegisterController.class
})
@Log4j2
class ApiValidationErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiValidationErrorResponseDto handleValidationException(MethodArgumentNotValidException exception) {
        List<String> messages = getErrorsFromException(exception);
        log.warn("Validation failed: {}", messages);
        return new ApiValidationErrorResponseDto(messages, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
    }

    private List<String> getErrorsFromException(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }

}
