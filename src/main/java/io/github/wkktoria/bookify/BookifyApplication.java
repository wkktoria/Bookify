package io.github.wkktoria.bookify;

import io.github.wkktoria.bookify.infrastructure.security.jwt.JwtConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {JwtConfigurationProperties.class})
public class BookifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookifyApplication.class, args);
    }

}
