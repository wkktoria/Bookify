package io.github.wkktoria.bookify.infrastructure.security;

import io.github.wkktoria.bookify.domain.usercrud.User;
import io.github.wkktoria.bookify.domain.usercrud.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
class OAuth2UserServiceImpl implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRepository userRepository;
    private final OidcUserService delegate = new OidcUserService();

    @Override
    @Transactional
    public OidcUser loadUser(final OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = delegate.loadUser(userRequest);

        String email = oidcUser.getEmail();

        if (!userRepository.existsByEmail(email)) {
            User newUser = new User(email, null, List.of("ROLE_USER"), null);
            User savedUser = userRepository.save(newUser);
            savedUser.confirm();
            log.info("Created new OAuth2 user: {}", email);
        }

        return oidcUser;
    }

}
