package io.github.wkktoria.bookify.domain.crud;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }

}
