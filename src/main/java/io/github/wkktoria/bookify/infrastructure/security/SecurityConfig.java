package io.github.wkktoria.bookify.infrastructure.security;

import io.github.wkktoria.bookify.domain.usercrud.UserRepository;
import io.github.wkktoria.bookify.infrastructure.security.jwt.JwtAuthFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@Log4j2
class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsManager userDetailsService(final UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http, final JwtAuthFilter jwtAuthFilter)
            throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(corsConfigurerCustomizer());
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.sessionManagement(c ->
                c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/users/register/**").permitAll()
                .requestMatchers("/users/register/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/token/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/authors/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/series/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/genres/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/authors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/authors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/authors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/authors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/series/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/series/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/genres/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/actuator/**").hasRole("ADMIN")
                .anyRequest().authenticated());
        return http.build();
    }

    public Customizer<CorsConfigurer<HttpSecurity>> corsConfigurerCustomizer() {
        return c -> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(
                        List.of("http://localhost:5173")
                );
                config.setAllowedMethods(
                        List.of("GET", "POST", "PUT", "DELETE", "PATCH")
                );
                config.setAllowedHeaders(List.of("*"));
                return config;
            };
            c.configurationSource(source);
        };
    }

}
