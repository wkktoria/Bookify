package io.github.wkktoria.bookify.infrastructure.crud.series.controller.error;

import io.github.wkktoria.bookify.domain.crud.SeriesNotDeletedException;
import io.github.wkktoria.bookify.domain.crud.SeriesNotFoundException;
import io.github.wkktoria.bookify.infrastructure.crud.series.controller.SeriesRestController;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = SeriesRestController.class)
@Log4j2
class SeriesErrorHandler {

    @ExceptionHandler(SeriesNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorSeriesResponseDto handleSeriesNotFoundException(final SeriesNotFoundException exception) {
        log.error("Series not found: {}", exception.getMessage());
        return new ErrorSeriesResponseDto(exception.getMessage());
    }

    @ExceptionHandler(SeriesNotDeletedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorSeriesResponseDto handleSeriesNotDeletedException(final SeriesNotDeletedException exception) {
        log.error("Series not deleted: {}", exception.getMessage());
        return new ErrorSeriesResponseDto(exception.getMessage());
    }

}
