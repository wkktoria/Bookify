package io.github.wkktoria.bookify.infrastructure.security.jwt;

import org.springframework.stereotype.Component;

@Component
class JwtGenerator {

    String authenticateAndGenerateToken(final String username, final String password) {
        return "token123";
    }

}
