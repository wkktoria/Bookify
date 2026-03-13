package io.github.wkktoria.bookify.book.infrastructure.controller;

import io.github.wkktoria.bookify.book.domain.service.BookRetriever;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookViewController {

    private final BookRetriever bookRetriever;

    BookViewController(BookRetriever bookRetriever) {
        this.bookRetriever = bookRetriever;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/view/books")
    public String books(Model model) {
        model.addAttribute("bookList", bookRetriever.findAll(Pageable.ofSize(Integer.MAX_VALUE)));
        return "books";
    }

}
