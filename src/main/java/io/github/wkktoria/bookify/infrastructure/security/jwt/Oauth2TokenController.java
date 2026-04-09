package io.github.wkktoria.bookify.infrastructure.security.jwt;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Oauth2TokenController {

    @GetMapping("/oauth-token")
    ResponseEntity<TokenResponseDto> getToken(final Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OidcUser oidcUser) {
            String token = oidcUser.getIdToken().getTokenValue();
            return ResponseEntity.ok(new TokenResponseDto(token));
        }

        return ResponseEntity.notFound().build();
    }

}
