package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(generator = "book_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "book_id_seq",
            sequenceName = "book_id_seq",
            allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "publication_date", nullable = false)
    private Instant publicationDate;

    @Column(name = "isbn", nullable = false, length = 13)
    private String isbn;

    @Column(name = "pages", nullable = false)
    private Integer pages;

    @Enumerated(EnumType.STRING)
    private BookLanguage language;

    @OneToOne
    private Genre genre;

    @ManyToMany
    private Set<Author> authors = new HashSet<>();

    public Book(String title) {
        this.title = title;
    }

}
