package io.github.wkktoria.bookify.domain.usercrud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
public class BookifyUserCrudFacade {

    private final UserRetriever userRetriever;

    public Set<UserDto> findAllUsers() {
        log.info("Fetching all users");
        return userRetriever.findAll();
    }

}
