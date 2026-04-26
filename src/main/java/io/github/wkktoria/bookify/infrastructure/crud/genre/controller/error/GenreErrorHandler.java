package io.github.wkktoria.bookify.infrastructure.crud.genre.controller.error;

import io.github.wkktoria.bookify.domain.crud.GenreNotDeletedException;
import io.github.wkktoria.bookify.domain.crud.GenreNotFoundException;
import io.github.wkktoria.bookify.infrastructure.crud.genre.controller.GenreRestController;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = GenreRestController.class)
@Log4j2
class GenreErrorHandler {

    @ExceptionHandler(GenreNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorGenreResponseDto handleGenreNotFoundException(final GenreNotFoundException exception) {
        log.warn("Genre not found: {}", exception.getMessage());
        return new ErrorGenreResponseDto(exception.getMessage());
    }

    @ExceptionHandler(GenreNotDeletedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorGenreResponseDto handleGenreNotDeletedException(final GenreNotDeletedException exception) {
        log.warn("Genre not deleted: {}", exception.getMessage());
        return new ErrorGenreResponseDto(exception.getMessage());
    }

}
