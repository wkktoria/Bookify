package io.github.wkktoria.bookify.infrastructure.crud.book.controller;

import io.github.wkktoria.bookify.domain.crud.BookCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.request.CreateBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.request.PartiallyUpdateBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.request.UpdateBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.CreateBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.DeleteBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.GetAllBooksResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.GetBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.PartiallyUpdateBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.UpdateBookResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.github.wkktoria.bookify.infrastructure.crud.book.controller.BookControllerMapper.*;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
@Log4j2
public class BookRestController {

    private final BookCrudFacade bookFacade;

    @GetMapping
    public ResponseEntity<GetAllBooksResponseDto> getAllBooks(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        log.debug("GET /books request received");

        List<BookDto> allBooks = bookFacade.findAll(pageable);
        GetAllBooksResponseDto body = mapFromBookDtoListToGetAllResponseDto(allBooks);

        log.debug("Returning {} books", allBooks.size());

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBookResponseDto> getBookById(@PathVariable Long id) {
        log.debug("GET /books/{} request received", id);

        BookDto book = bookFacade.findById(id);
        GetBookResponseDto body = mapFromBookDtoGetGetBookResponseDto(book);

        log.debug("Book with id={} returned successfully", id);

        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<CreateBookResponseDto> createBook(@RequestBody @Valid CreateBookRequestDto request) {
        log.info("POST /books request received");

        BookDto book = mapFromCreateBookRequestDtoToBookDto(request);
        BookDto savedBook = bookFacade.addBook(book);

        CreateBookResponseDto body = mapFromBookDtoToCreateBookResponseDto(savedBook);

        log.info("Book created successfully with id={}", savedBook.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteBookResponseDto> deleteBookByIdUsingPathVariable(@PathVariable Long id) {
        log.info("DELETE /books/{} request received", id);
        return deleteBook(id);
    }

    @DeleteMapping
    public ResponseEntity<DeleteBookResponseDto> deleteBookByIdUsingRequestParam(@RequestParam Long id) {
        log.info("DELETE /books?id={} request received", id);
        return deleteBook(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateBookResponseDto> updateBook(@PathVariable Long id,
                                                            @RequestBody @Valid UpdateBookRequestDto request) {
        log.info("PUT /books/{} request received", id);

        BookDto newBook = mapFromUpdateBookRequestDtoToBookDto(request);
        bookFacade.updateById(id, newBook);

        UpdateBookResponseDto body = mapFromBookDtoToUpdateBookResponseDto(newBook);

        log.info("Book with id={} successfully updated", id);

        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateBookResponseDto> partiallyUpdateBook(@PathVariable Long id,
                                                                              @RequestBody PartiallyUpdateBookRequestDto request) {
        log.info("PATCH /books/{} request received", id);

        BookDto updatedBook = mapFromPartiallyUpdateBookRequestDtoToBookDto(request);
        BookDto savedBook = bookFacade.updatePartiallyById(id, updatedBook);

        PartiallyUpdateBookResponseDto body = mapFromBookDtoToPartiallyUpdateBookResponseDto(savedBook);

        log.info("Book with id={} partially updated", id);

        return ResponseEntity.ok(body);
    }

    private ResponseEntity<DeleteBookResponseDto> deleteBook(Long id) {
        bookFacade.deleteById(id);

        DeleteBookResponseDto body = mapFromLongIdToDeleteBookResponseDto(id);

        log.info("Book with id={} successfully deleted", id);

        return ResponseEntity.ok(body);
    }

}
