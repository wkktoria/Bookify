package io.github.wkktoria.bookify.domain.crud;

public class GenreNotDeletedException extends RuntimeException {

    public GenreNotDeletedException(final String message) {
        super(message);
    }

}
