package io.github.wkktoria.bookify.domain.usercrud;

public class UserExistsException extends RuntimeException {

    public UserExistsException(final String message) {
        super(message);
    }

}
