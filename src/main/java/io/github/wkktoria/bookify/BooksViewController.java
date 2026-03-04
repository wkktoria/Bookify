package io.github.wkktoria.bookify;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BooksViewController {

    @GetMapping("/")
    public String home() {
        return "home.html";
    }

}
