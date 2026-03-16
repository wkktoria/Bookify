package io.github.wkktoria.bookify.domain.crud.author;

import io.github.wkktoria.bookify.domain.crud.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "author")
@NoArgsConstructor
@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Builder
@ToString
class Author extends BaseEntity {

    @Id
    @GeneratedValue(generator = "author_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "author_id_seq",
            sequenceName = "author_id_seq",
            allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "firstname", nullable = false, length = 15)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 30)
    private String lastname;

}
