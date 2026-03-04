package io.github.wkktoria.bookify.book;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@Log4j2
public class BooksController {

    private static final Map<Integer, String> database = new HashMap<>();

    static {
        database.put(1, "Clean Code: A Handbook of Agile Software Craftsmanship");
        database.put(2, "Refactoring: Improving the Design of Existing Code");
        database.put(3, "Fundamentals of Software Architecture: A Modern Engineering Approach");
        database.put(4, "Effective Java");
        database.put(5, "Spring Start Here: Learn what you need and learn it well");
    }

    @GetMapping
    public ResponseEntity<BookResponseDto> getAllBooks(@RequestParam(required = false) Integer id,
                                                       @RequestParam(required = false) Integer limit,
                                                       @RequestHeader(required = false) String requestId) {
        if (requestId != null) {
            log.info("Request id: {}", requestId);
        }

        if (id != null) {
            log.info("Getting book with id: {}", id);
            String book = database.get(id);

            if (book == null) {
                log.warn("Could not find book with id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            BookResponseDto response = new BookResponseDto(Map.of(id, book));
            return ResponseEntity.ok(response);
        }

        if (limit != null) {
            log.info("Getting books with limit: {}", limit);
            Map<Integer, String> limitedMap = database.entrySet().stream()
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            BookResponseDto response = new BookResponseDto(limitedMap);
            return ResponseEntity.ok(response);
        }

        log.info("Getting all books");
        BookResponseDto response = new BookResponseDto(database);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleBookResponseDto> getBookById(@PathVariable Integer id) {
        log.info("Getting book with id: {}", id);
        String book = database.get(id);

        if (book == null) {
            log.warn("Could not find book with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        SingleBookResponseDto response = new SingleBookResponseDto(book);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SingleBookResponseDto> postBook(@RequestBody @Valid BookRequestDto request) {
        String bookTitle = request.bookTitle();
        log.info("Adding new book with title: {}", bookTitle);
        database.put(database.size() + 1, bookTitle);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SingleBookResponseDto(bookTitle));
    }

}
