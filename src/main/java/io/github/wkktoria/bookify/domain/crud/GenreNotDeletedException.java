package io.github.wkktoria.bookify.domain.crud;

class GenreNotDeletedException extends RuntimeException {

    public GenreNotDeletedException(final String message) {
        super(message);
    }

}
