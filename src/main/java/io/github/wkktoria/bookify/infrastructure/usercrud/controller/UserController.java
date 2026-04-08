package io.github.wkktoria.bookify.infrastructure.usercrud.controller;

import io.github.wkktoria.bookify.domain.usercrud.BookifyUserCrudFacade;
import io.github.wkktoria.bookify.domain.usercrud.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Log4j2
class UserController {

    private final BookifyUserCrudFacade bookifyUserCrudFacade;

    @GetMapping
    ResponseEntity<AllUsersResponseDto> getUsers() {
        log.info("GET /users request received");

        Set<UserDto> allUsers = bookifyUserCrudFacade.findAllUsers();
        AllUsersResponseDto body = new AllUsersResponseDto(allUsers);

        return ResponseEntity.ok(body);
    }

}
