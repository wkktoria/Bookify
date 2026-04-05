package io.github.wkktoria.bookify.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
class JwtGenerator {

    private final AuthenticationManager authenticationManager;

    String authenticateAndGenerateToken(final String username, final String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return "token123";
    }

}
