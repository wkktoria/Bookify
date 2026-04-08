package io.github.wkktoria.bookify.domain.usercrud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class UserRetriever {

    private final UserRepository userRepository;

    Set<UserDto> findAll() {
        log.debug("Retrieving all users");
        return userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .email(user.getEmail())
                        .roles(new HashSet<>(user.getAuthorities()))
                        .build())
                .collect(Collectors.toSet());
    }

}
