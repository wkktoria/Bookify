package io.github.wkktoria.bookify.domain.crud;

public class SeriesNotUpdatedException extends RuntimeException {

    public SeriesNotUpdatedException(final String message) {
        super(message);
    }

}
