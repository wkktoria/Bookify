package io.github.wkktoria.bookify.book.domain.model;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }

}
