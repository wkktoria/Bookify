package io.github.wkktoria.bookify.infrastructure.crud.book.controller;

import io.github.wkktoria.bookify.domain.crud.BookCrudFacade;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class BookViewController {

    private final BookCrudFacade bookFacade;


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/view/books")
    public String books(Model model) {
        model.addAttribute("bookList", bookFacade.findAll(Pageable.ofSize(Integer.MAX_VALUE)));
        return "books";
    }

}
