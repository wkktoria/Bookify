package io.github.wkktoria.bookify.infrastructure.security.jwt;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@Log4j2
class JwtController {

    private final JwtGenerator tokenGenerator;

    @PostMapping("/token")
    ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@RequestBody @Valid final TokenRequestDto requestDto) {
        String token = tokenGenerator.authenticateAndGenerateToken(requestDto.username(), requestDto.password());

        return ResponseEntity.ok(
                JwtResponseDto.builder()
                        .token(token)
                        .build()
        );
    }

}
