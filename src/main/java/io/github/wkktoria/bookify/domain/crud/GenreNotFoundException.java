package io.github.wkktoria.bookify.domain.crud;

public class GenreNotFoundException extends RuntimeException {

    public GenreNotFoundException(String message) {
        super(message);
    }

}
