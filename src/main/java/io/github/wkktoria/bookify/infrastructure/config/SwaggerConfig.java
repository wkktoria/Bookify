package io.github.wkktoria.bookify.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SwaggerConfig {

    @Bean
    OpenAPI customizeOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bookify API")
                        .description("""
                                Authentication (Google OAuth2)
                                
                                Login flow:
                                1. Open /login endpoint
                                2. You will be redirected to Google
                                3. After successful login, backend sets accessToken cookie
                                4. Swagger UI automatically sends this cookie in all requests
                                
                                Notes:
                                - No manual token input needed
                                - Authentication happens via Google OAuth2
                                - Cookie must be present in browser session
                                """)
                );
    }

}
