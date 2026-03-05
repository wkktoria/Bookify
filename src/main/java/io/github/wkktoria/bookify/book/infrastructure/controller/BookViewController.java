package io.github.wkktoria.bookify.book.infrastructure.controller;

import io.github.wkktoria.bookify.book.domain.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BookViewController {

    private static final Map<Integer, Book> database = new HashMap<>();

    static {
        database.put(1, new Book("Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin"));
        database.put(2, new Book("Refactoring: Improving the Design of Existing Code", "Martin Fowler"));
        database.put(3, new Book("Fundamentals of Software Architecture: A Modern Engineering Approach", "Mark Richards & Neal Ford"));
        database.put(4, new Book("Effective Java", "Joshua Bloch"));
        database.put(5, new Book("Spring Start Here: Learn what you need and learn it well", "Laurentiu Spilca"));
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/view/books")
    public String books(Model model) {
        model.addAttribute("bookMap", database);
        return "books";
    }

}
