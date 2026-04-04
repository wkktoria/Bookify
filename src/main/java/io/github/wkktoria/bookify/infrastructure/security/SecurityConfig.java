package io.github.wkktoria.bookify.infrastructure.security;

import io.github.wkktoria.bookify.domain.usercrud.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Log4j2
class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsService(final UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/users/register/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/books/**").permitAll()
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
                .anyRequest().authenticated());
        return http.build();
    }

}
