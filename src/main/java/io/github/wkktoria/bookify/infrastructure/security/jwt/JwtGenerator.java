package io.github.wkktoria.bookify.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.wkktoria.bookify.infrastructure.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
@Log4j2
class JwtGenerator {

    public static final String ROLES_CLAIM_NAME = "roles";

    private final AuthenticationManager authenticationManager;
    private final Clock clock;
    private final JwtConfigurationProperties properties;
    private final KeyPair keyPair;

    String authenticateAndGenerateToken(final String username, final String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        Instant issuedAt = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant expiresAt = issuedAt.plus(Duration.ofMinutes(properties.expirationMinutes()));

        Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) keyPair.getPrivate());

        return JWT.create()
                .withSubject(securityUser.getUsername())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withIssuer(properties.issuer())
                .withClaim(ROLES_CLAIM_NAME, securityUser.getAuthoritiesAsString())
                .sign(algorithm);
    }

}
