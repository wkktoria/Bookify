package io.github.wkktoria.bookify.domain.usercrud;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

    Optional<User> findFirstByEmail(final String email);

    User save(final User user);

    boolean existsByEmail(final String email);

}
