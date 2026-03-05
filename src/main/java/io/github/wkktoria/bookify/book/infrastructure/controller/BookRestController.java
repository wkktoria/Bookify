package io.github.wkktoria.bookify.book.infrastructure.controller;

import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.CreateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.PartiallyUpdateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.UpdateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.response.*;
import io.github.wkktoria.bookify.book.domain.model.BookNotFoundException;
import io.github.wkktoria.bookify.book.domain.model.Book;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.wkktoria.bookify.book.infrastructure.controller.BookMapper.*;

@RestController
@RequestMapping("/books")
@Log4j2
public class BookRestController {

    private static final Map<Integer, Book> database = new HashMap<>();

    static {
        database.put(1, new Book("Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin"));
        database.put(2, new Book("Refactoring: Improving the Design of Existing Code", "Martin Fowler"));
        database.put(3, new Book("Fundamentals of Software Architecture: A Modern Engineering Approach", "Mark Richards & Neal Ford"));
        database.put(4, new Book("Effective Java", "Joshua Bloch"));
        database.put(5, new Book("Spring Start Here: Learn what you need and learn it well", "Laurentiu Spilca"));
    }

    @GetMapping
    public ResponseEntity<GetAllBooksResponseDto> getAllBooks(@RequestParam(required = false) Integer id,
                                                              @RequestParam(required = false) Integer limit,
                                                              @RequestHeader(required = false) String requestId) {
        if (requestId != null) {
            log.info("Request id={}", requestId);
        }

        if (id != null) {
            log.info("Getting book with id={}", id);
            Book book = database.get(id);

            if (book == null) {
                throw new BookNotFoundException("Could not find book with id=" + id);
            }

            GetAllBooksResponseDto body = mapFromMapToGetAllBooksResponseDto(Map.of(id, book));
            return ResponseEntity.ok(body);
        }

        if (limit != null) {
            log.info("Getting books with limit={}", limit);
            Map<Integer, Book> limitedMap = database.entrySet().stream()
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            GetAllBooksResponseDto body = mapFromMapToGetAllBooksResponseDto(limitedMap);
            return ResponseEntity.ok(body);
        }

        log.info("Getting all books");
        GetAllBooksResponseDto body = mapFromMapToGetAllBooksResponseDto(database);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBookResponseDto> getBookById(@PathVariable Integer id) {
        log.info("Getting book with id={}", id);
        Book book = database.get(id);

        if (book == null) {
            throw new BookNotFoundException("Could not find book with id=" + id);
        }

        GetBookResponseDto body = mapFromBookToGetBookResponseDto(book);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<CreateBookResponseDto> createBook(@RequestBody @Valid CreateBookRequestDto request) {
        Book book = mapFromCreateBookRequestDtoToBook(request);
        log.info("Adding new book={}", book);
        database.put(database.size() + 1, book);
        CreateBookResponseDto body = mapFromBookToCreateBookResponseDto(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteBookResponseDto> deleteBookByIdUsingPathVariable(@PathVariable Integer id) {
        return deleteBook(id);
    }

    @DeleteMapping
    public ResponseEntity<DeleteBookResponseDto> deleteBookByIdUsingRequestParam(@RequestParam Integer id) {
        return deleteBook(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateBookResponseDto> updateBook(@PathVariable Integer id,
                                                            @RequestBody @Valid UpdateBookRequestDto request) {
        if (!database.containsKey(id)) {
            throw new BookNotFoundException("Could not find book with id=" + id);
        }

        Book newBook = mapFromUpdateBookRequestDtoToBook(request);
        Book oldBook = database.put(id, newBook);
        log.info("Updating oldBook={} with id={} to newBook={}", oldBook, id, newBook);
        UpdateBookResponseDto body = mapFromBookToUpdateBookResponseDto(newBook);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateBookResponseDto> partiallyUpdateBook(@PathVariable Integer id,
                                                                              @RequestBody PartiallyUpdateBookRequestDto request) {
        if (!database.containsKey(id)) {
            throw new BookNotFoundException("Could not find book with id=" + id);
        }

        Book bookFromDatabase = database.get(id);
        Book.BookBuilder builder = Book.builder();

        if (request.bookTitle() != null) {
            builder.title(request.bookTitle());
        } else {
            builder.title(bookFromDatabase.title());
        }

        if (request.author() != null) {
            builder.author(request.author());
        } else {
            builder.author(bookFromDatabase.author());
        }

        Book updatedBook = builder.build();
        database.put(id, updatedBook);
        log.info("Partially updating bookFromDatabase={} with id={} to updatedBook={}", bookFromDatabase, id, updatedBook);
        PartiallyUpdateBookResponseDto body = mapFromBookToPartiallyUpdateBookResponseDto(updatedBook);
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<DeleteBookResponseDto> deleteBook(Integer id) {
        if (!database.containsKey(id)) {
            throw new BookNotFoundException("Could not find book with id=" + id);
        }

        log.info("Deleting book with id {}", id);
        database.remove(id);
        DeleteBookResponseDto body = mapFromIdToDeleteBookResponseDto(id);
        return ResponseEntity.ok(body);
    }

}
