package io.github.wkktoria.bookify.infrastructure.security;

import io.github.wkktoria.bookify.domain.usercrud.User;
import io.github.wkktoria.bookify.domain.usercrud.UserConfirmer;
import io.github.wkktoria.bookify.domain.usercrud.UserExistsException;
import io.github.wkktoria.bookify.domain.usercrud.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
@Transactional
@Log4j2
class UserDetailsServiceImpl implements UserDetailsManager {

    public static final String DEFAULT_USER_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConfirmer userConfirmer;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findFirstByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new RuntimeException("Could not find user with username=" + username));
    }

    @Override
    public void createUser(final UserDetails user) {
        if (userExists(user.getUsername())) {
            log.warn("User could not be saved, because user with username='{}' already exists", user.getUsername());
            throw new UserExistsException("User not saved - already exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        String confirmationToken = UUID.randomUUID().toString();

        User createdUser = new User(
                user.getUsername(),
                encodedPassword,
                List.of(DEFAULT_USER_ROLE),
                confirmationToken
        );
        User savedUser = userRepository.save(createdUser);
        log.info("User saved with id={}", savedUser.getId());
        userConfirmer.sendConfirmationEmail(createdUser);
    }

    @Override
    public void updateUser(final UserDetails user) {

    }

    @Override
    public void deleteUser(final String username) {

    }

    @Override
    public void changePassword(final String oldPassword, final String newPassword) {

    }

    @Override
    public boolean userExists(final String username) {
        return userRepository.existsByEmail(username);
    }

}
