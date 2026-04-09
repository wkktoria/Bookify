package io.github.wkktoria.bookify.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws ServletException, IOException {
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("/oauth-token");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
