package io.github.wkktoria.bookify.domain.usercrud;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends Repository<User, Long> {

    @Query("SELECT u FROM User u")
    Set<User> findAll();

    Optional<User> findFirstByEmail(final String email);

    User save(final User user);

    boolean existsByEmail(final String email);

}
