package io.github.wkktoria.bookify.domain.crud;

public class SeriesNotFoundException extends RuntimeException {

    public SeriesNotFoundException(String message) {
        super(message);
    }

}
