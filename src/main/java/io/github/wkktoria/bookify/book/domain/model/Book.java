package io.github.wkktoria.bookify.book.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    public Book(final String title, final String author) {
        this.title = title;
        this.author = author;
    }

}
