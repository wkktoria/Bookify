package io.github.wkktoria.bookify.book;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BooksViewController {

    private static final Map<Integer, String> database = new HashMap<>();

    static {
        database.put(1, "Clean Code: A Handbook of Agile Software Craftsmanship");
        database.put(2, "Refactoring: Improving the Design of Existing Code");
        database.put(3, "Fundamentals of Software Architecture: A Modern Engineering Approach");
        database.put(4, "Effective Java");
        database.put(5, "Spring Start Here: Learn what you need and learn it well");
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
