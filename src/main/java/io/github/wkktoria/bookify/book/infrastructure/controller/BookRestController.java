package io.github.wkktoria.bookify.book.infrastructure.controller;

import io.github.wkktoria.bookify.book.domain.service.BookAdder;
import io.github.wkktoria.bookify.book.domain.service.BookDeleter;
import io.github.wkktoria.bookify.book.domain.service.BookRetriever;
import io.github.wkktoria.bookify.book.domain.service.BookUpdater;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.CreateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.PartiallyUpdateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.UpdateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.response.*;
import io.github.wkktoria.bookify.book.domain.model.Book;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final BookUpdater bookUpdater;

    @GetMapping
    public ResponseEntity<GetAllBooksResponseDto> getAllBooks(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        log.info("Received request to retrieve all books");

        List<Book> allBooks = bookRetriever.findAll(pageable);
        GetAllBooksResponseDto body = mapFromMapToGetAllBooksResponseDto(allBooks);

        log.debug("Returning {} books", allBooks.size());

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBookResponseDto> getBookById(@PathVariable Long id) {
        log.info("Getting book with id={}", id);
        Book book = bookRetriever.findBookById(id);
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
    public ResponseEntity<UpdateBookResponseDto> updateBook(@PathVariable Long id,
                                                            @RequestBody @Valid UpdateBookRequestDto request) {
        log.info("Updating book with id={} with request={}", id, request);
        Book newBook = mapFromUpdateBookRequestDtoToBook(request);
        bookUpdater.updateById(id, newBook);
        UpdateBookResponseDto body = mapFromBookToUpdateBookResponseDto(newBook);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateBookResponseDto> partiallyUpdateBook(@PathVariable Long id,
                                                                              @RequestBody PartiallyUpdateBookRequestDto request) {
        log.info("Partially updating book with id={} with request={}", id, request);
        Book updatedBook = mapFromPartiallyUpdateBookRequestDtoToBook(request);
        Book savedBook = bookUpdater.updatePartiallyById(id, updatedBook);
        PartiallyUpdateBookResponseDto body = mapFromBookToPartiallyUpdateBookResponseDto(savedBook);
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<DeleteBookResponseDto> deleteBook(Long id) {
        log.info("Deleting book with id={}", id);
        bookDeleter.deleteById(id);
        DeleteBookResponseDto body = mapFromIdToDeleteBookResponseDto(id);
        return ResponseEntity.ok(body);
    }

}
