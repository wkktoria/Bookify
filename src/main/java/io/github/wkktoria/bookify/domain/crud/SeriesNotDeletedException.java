package io.github.wkktoria.bookify.domain.crud;

public class SeriesNotDeletedException extends RuntimeException {

    public SeriesNotDeletedException(final String message) {
        super(message);
    }

}
