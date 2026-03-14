package io.github.wkktoria.bookify.book.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Book {

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


    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "publication_date", nullable = false)
    private Instant publicationDate;

    @Column(name = "isbn", nullable = false, length = 13)
    private String isbn;

    @Column(name = "pages", nullable = false)
    private Integer pages;

    @Enumerated(EnumType.STRING)
    private BookLanguage language;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

}
