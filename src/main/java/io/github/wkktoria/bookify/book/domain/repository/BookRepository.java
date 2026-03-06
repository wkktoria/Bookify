package io.github.wkktoria.bookify.book.domain.repository;

import io.github.wkktoria.bookify.book.domain.model.Book;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface BookRepository extends Repository<Book, Long> {

    Book saveToDatabase(Book book);

    List<Book> findAll();

}
