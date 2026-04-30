package io.github.wkktoria.bookify.infrastructure.usercrud.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Log4j2
public class RegisterController {

    private final UserDetailsManager userDetailsManager;

    @PostMapping("/register")
    ResponseEntity<RegisterUserResponseDto> register(@RequestBody @Valid final RegisterUserRequestDto requestDto) {
        String username = requestDto.username();
        String password = requestDto.password();
        UserDetails user = User.builder()
                .username(username)
                .password(password)
                .build();
        userDetailsManager.createUser(user);
        return ResponseEntity.ok(new RegisterUserResponseDto("User created successfully"));
    }

}
