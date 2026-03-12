package io.github.wkktoria.bookify.book.infrastructure.controller;

import io.github.wkktoria.bookify.book.domain.service.BookAdder;
import io.github.wkktoria.bookify.book.domain.service.BookDeleter;
import io.github.wkktoria.bookify.book.domain.service.BookRetriever;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.CreateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.PartiallyUpdateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.UpdateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.response.*;
import io.github.wkktoria.bookify.book.domain.model.BookNotFoundException;
import io.github.wkktoria.bookify.book.domain.model.Book;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.github.wkktoria.bookify.book.infrastructure.controller.BookMapper.*;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
@Log4j2
public class BookRestController {

    private final BookAdder bookAdder;
    private final BookRetriever bookRetriever;
    private final BookDeleter bookDeleter;

    @GetMapping
    public ResponseEntity<GetAllBooksResponseDto> getAllBooks(@RequestParam(required = false) Integer id,
                                                              @RequestParam(required = false) Integer limit,
                                                              @RequestHeader(required = false) String requestId) {
        if (requestId != null) {
            log.info("Request id={}", requestId);
        }

        if (id != null) {
            log.info("Getting book with id={}", id);
            Book book = bookRetriever.findAll().get(id);

            if (book == null) {
                throw new BookNotFoundException("Could not find book with id=" + id);
            }

            GetAllBooksResponseDto body = mapFromMapToGetAllBooksResponseDto(List.of(book));
            return ResponseEntity.ok(body);
        }

        if (limit != null) {
            log.info("Getting books with limit={}", limit);
            List<Book> limitedBooks = bookRetriever.findAllLimitedBy(limit);
            GetAllBooksResponseDto body = mapFromMapToGetAllBooksResponseDto(limitedBooks);
            return ResponseEntity.ok(body);
        }

        log.info("Getting all books");
        GetAllBooksResponseDto body = mapFromMapToGetAllBooksResponseDto(bookRetriever.findAll());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBookResponseDto> getBookById(@PathVariable Long id) {
        log.info("Getting book with id={}", id);
        Book book = bookRetriever.findBookById(id)
                .orElseThrow(() -> new BookNotFoundException("Could not find book with id=" + id));
        GetBookResponseDto body = mapFromBookToGetBookResponseDto(book);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<CreateBookResponseDto> createBook(@RequestBody @Valid CreateBookRequestDto request) {
        log.info("Creating book from request: {}", request);
        Book book = mapFromCreateBookRequestDtoToBook(request);
        bookAdder.addBook(book);
        CreateBookResponseDto body = mapFromBookToCreateBookResponseDto(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteBookResponseDto> deleteBookByIdUsingPathVariable(@PathVariable Long id) {
        return deleteBook(id);
    }

    @DeleteMapping
    public ResponseEntity<DeleteBookResponseDto> deleteBookByIdUsingRequestParam(@RequestParam Long id) {
        return deleteBook(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateBookResponseDto> updateBook(@PathVariable Integer id,
                                                            @RequestBody @Valid UpdateBookRequestDto request) {
        List<Book> allBooks = bookRetriever.findAll();

        if (!allBooks.contains(id)) {
            throw new BookNotFoundException("Could not find book with id=" + id);
        }

        Book newBook = mapFromUpdateBookRequestDtoToBook(request);
        Book oldBook = bookAdder.addBook(newBook);
        log.info("Updating oldBook={} with id={} to newBook={}", oldBook, id, newBook);
        UpdateBookResponseDto body = mapFromBookToUpdateBookResponseDto(newBook);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateBookResponseDto> partiallyUpdateBook(@PathVariable Integer id,
                                                                              @RequestBody PartiallyUpdateBookRequestDto request) {
        List<Book> allBooks = bookRetriever.findAll();

        if (!allBooks.contains(id)) {
            throw new BookNotFoundException("Could not find book with id=" + id);
        }

        Book bookFromDatabase = allBooks.get(id);
        Book.BookBuilder builder = Book.builder();

        if (request.bookTitle() != null) {
            builder.title(request.bookTitle());
        } else {
            builder.title(bookFromDatabase.getTitle());
        }

        if (request.author() != null) {
            builder.author(request.author());
        } else {
            builder.author(bookFromDatabase.getAuthor());
        }

        Book updatedBook = builder.build();
        bookAdder.addBook(updatedBook);
        log.info("Partially updating bookFromDatabase={} with id={} to updatedBook={}", bookFromDatabase, id, updatedBook);
        PartiallyUpdateBookResponseDto body = mapFromBookToPartiallyUpdateBookResponseDto(updatedBook);
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<DeleteBookResponseDto> deleteBook(Long id) {
        log.info("Deleting book with id={}", id);
        bookRetriever.existsById(id);
        bookDeleter.deleteById(id);
        DeleteBookResponseDto body = mapFromIdToDeleteBookResponseDto(id);
        return ResponseEntity.ok(body);
    }

}
