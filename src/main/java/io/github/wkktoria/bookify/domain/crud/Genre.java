package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "genre", indexes = {
        @Index(name = "idx_genre_name", columnList = "name")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Builder
@ToString
class Genre extends BaseEntity {

    @Id
    @GeneratedValue(generator = "genre_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "genre_id_seq",
            sequenceName = "genre_id_seq",
            allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 25)
    private String name;

    Genre(final String name) {
        this.name = name;
    }

}
