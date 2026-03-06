package io.github.wkktoria.bookify.book.domain.repository;

import io.github.wkktoria.bookify.book.domain.model.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookRepositoryInMemory implements BookRepository {

    private static final Map<Integer, Book> database = new HashMap<>();

    static {
        database.put(1, new Book("Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin"));
        database.put(2, new Book("Refactoring: Improving the Design of Existing Code", "Martin Fowler"));
        database.put(3, new Book("Fundamentals of Software Architecture: A Modern Engineering Approach", "Mark Richards & Neal Ford"));
        database.put(4, new Book("Effective Java", "Joshua Bloch"));
        database.put(5, new Book("Spring Start Here: Learn what you need and learn it well", "Laurentiu Spilca"));
    }

    @Override
    public Book saveToDatabase(Book book) {
        database.put(database.size() + 1, book);
        return book;
    }

    @Override
    public List<Book> findAll() {
        return database.values()
                .stream()
                .toList();
    }

}
