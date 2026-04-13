package io.github.wkktoria.bookify.domain.usercrud;

import io.github.wkktoria.bookify.domain.crud.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "users_id_seq",
            sequenceName = "users_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    private Collection<String> authorities = new HashSet<>();

    private boolean enabled = false;

    private String confirmationToken;

    public User(final String email, final String password, final Collection<String> authorities,
                final String confirmationToken) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.confirmationToken = confirmationToken;
    }

    public User(final String email, final Collection<String> authorities, final Instant createdOn) {
        this.email = email;
        this.authorities = authorities;
        this.createdOn = createdOn;
    }

    public boolean confirm() {
        this.setEnabled(true);
        this.setConfirmationToken(null);
        return true;
    }

}
