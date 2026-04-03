package io.github.wkktoria.bookify.infrastructure.security;

import io.github.wkktoria.bookify.domain.usercrud.User;
import io.github.wkktoria.bookify.domain.usercrud.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

@AllArgsConstructor
@Log4j2
class UserDetailsServiceImpl implements UserDetailsManager {

    public static final String DEFAULT_USER_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
            throw new RuntimeException("User not saved - already exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        User createdUser = new User(
                user.getUsername(),
                encodedPassword,
                true,
                List.of(DEFAULT_USER_ROLE)
        );
        User savedUser = userRepository.save(createdUser);
        log.info("User saved with id={}", savedUser.getId());

        // todo send email confirmation
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
