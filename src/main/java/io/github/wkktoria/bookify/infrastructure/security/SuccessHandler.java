package io.github.wkktoria.bookify.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws ServletException, IOException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        setResponseCookie(response, oidcUser.getIdToken().getTokenValue());
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("https://localhost:5173");
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void setResponseCookie(final HttpServletResponse response, final String tokenValue) {
        Cookie cookie = new Cookie("accessToken", tokenValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
    }

}
