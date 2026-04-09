package io.github.wkktoria.bookify.infrastructure.security.jwt;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MessageController {

    @GetMapping("/message")
    ResponseEntity<MessageResponseDto> getMessage(final Authentication principal) {
        MessageResponseDto message = new MessageResponseDto("Hi " + principal.getName());
        return ResponseEntity.ok(message);
    }

}
