package io.github.wkktoria.bookify.infrastructure.crud.book.controller;

import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookRequestDto;
import io.github.wkktoria.bookify.infrastructure.apivalidation.ApiValidationErrorResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.request.CreateBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.request.PartiallyUpdateBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.request.UpdateBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.CreateBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.DeleteBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.GetAllBooksResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.GetBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.PartiallyUpdateBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.UpdateBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Books", description = "API for managing books")
@RestController
@RequestMapping("/books")
@AllArgsConstructor
@Log4j2
public class BookRestController {

    private final BookifyCrudFacade bookFacade;

    @Operation(summary = "Get all books", description = "Returns paginated list of books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(schema = @Schema(implementation = GetAllBooksResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<GetAllBooksResponseDto> getAllBooks(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        log.debug("GET /books request received");

        List<BookDto> allBooks = bookFacade.findAllBooks(pageable);
        GetAllBooksResponseDto body = mapFromBookDtoListToGetAllResponseDto(allBooks);

        log.debug("Returning {} books", allBooks.size());

        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Get book by ID", description = "Returns book details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(schema = @Schema(implementation = GetBookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetBookResponseDto> getBookById(@Parameter(description = "Book ID", example = "1")
                                                          @PathVariable Long id) {
        log.debug("GET /books/{} request received", id);

        BookDto book = bookFacade.findBookById(id);
        GetBookResponseDto body = mapFromBookDtoGetGetBookResponseDto(book);

        log.debug("Book with id={} returned successfully", id);

        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Create new book", description = "Creates a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created",
                    content = @Content(schema = @Schema(implementation = CreateBookResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiValidationErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CreateBookResponseDto> createBook(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Book data",
            required = true,
            content = @Content(schema = @Schema(implementation = CreateBookRequestDto.class))
    ) @RequestBody @Valid final CreateBookRequestDto requestDto) {
        log.info("POST /books request received with request body: {}", requestDto);

        BookDto bookDto = bookFacade
                .addBookWithAuthor(new BookRequestDto(
                        requestDto.bookTitle(),
                        requestDto.publicationDate(),
                        requestDto.isbn(),
                        requestDto.pages(),
                        requestDto.authorId(),
                        requestDto.language())
                );
        CreateBookResponseDto body = mapFromBookDtoToCreateBookResponseDto(bookDto);

        log.info("Book created successfully with id={}", bookDto.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(body);
    }

    @Operation(summary = "Delete book", description = "Deletes book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted",
                    content = @Content(schema = @Schema(implementation = DeleteBookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteBookResponseDto> deleteBookByIdUsingPathVariable(@Parameter(description = "Book ID", example = "1")
                                                                                 @PathVariable Long id) {
        log.info("DELETE /books/{} request received", id);
        return deleteBook(id);
    }

    @Operation(summary = "Delete book", description = "Deletes book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted",
                    content = @Content(schema = @Schema(implementation = DeleteBookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<DeleteBookResponseDto> deleteBookByIdUsingRequestParam(@Parameter(name = "Book ID", example = "1")
                                                                                 @RequestParam Long id) {
        log.info("DELETE /books?id={} request received", id);
        return deleteBook(id);
    }

    @Operation(summary = "Delete book with genre", description = "Deletes book and the genre it's assigned to")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book and genre deleted",
                    content = @Content(schema = @Schema(implementation = DeleteBookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content)
    })
    @DeleteMapping("/{bookId}/genre")
    ResponseEntity<DeleteBookResponseDto> deleteBookWithGenre(@Parameter(description = "Book ID", example = "1")
                                                              @PathVariable final Long bookId) {
        log.info("DELETE /books/{} request received", bookId);
        bookFacade.deleteBookAndGenreById(bookId);
        DeleteBookResponseDto body = mapFromLongIdToDeleteBookResponseDto(bookId);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Update book", description = "Updates book data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated",
                    content = @Content(schema = @Schema(implementation = UpdateBookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiValidationErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UpdateBookResponseDto> updateBook(@Parameter(description = "Book ID", example = "1")
                                                            @PathVariable Long id,
                                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                    description = "Updated book data",
                                                                    required = true,
                                                                    content = @Content(
                                                                            schema = @Schema(
                                                                                    implementation = UpdateBookRequestDto.class))
                                                            )
                                                            @RequestBody @Valid UpdateBookRequestDto request) {
        log.info("PUT /books/{} request received", id);

        BookDto newBook = mapFromUpdateBookRequestDtoToBookDto(request);
        bookFacade.updateById(id, newBook);

        UpdateBookResponseDto body = mapFromBookDtoToUpdateBookResponseDto(newBook);

        log.info("Book with id={} successfully updated", id);

        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Update book", description = "Partially updates book data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated",
                    content = @Content(schema = @Schema(implementation = PartiallyUpdateBookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiValidationErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateBookResponseDto> partiallyUpdateBook(@Parameter(description = "Book ID", example = "1")
                                                                              @PathVariable Long id,
                                                                              @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                                      description = "Updated book title",
                                                                                      required = true,
                                                                                      content = @Content(
                                                                                              schema = @Schema(
                                                                                                      implementation = PartiallyUpdateBookRequestDto.class
                                                                                              )
                                                                                      )
                                                                              )
                                                                              @RequestBody PartiallyUpdateBookRequestDto request) {
        log.info("PATCH /books/{} request received", id);

        BookDto updatedBook = mapFromPartiallyUpdateBookRequestDtoToBookDto(request);
        BookDto savedBook = bookFacade.updatePartiallyById(id, updatedBook);

        PartiallyUpdateBookResponseDto body = mapFromBookDtoToPartiallyUpdateBookResponseDto(savedBook);

        log.info("Book with id={} partially updated", id);

        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Assign genre to book", description = "Links a genre to a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre assigned to book",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Book or Genre not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content)
    })
    @PutMapping("/{bookId}/genres/{genreId}")
    public ResponseEntity<String> assignGenreToBook(
            @Parameter(description = "Book ID", example = "1") @PathVariable final Long bookId,
            @Parameter(description = "Genre ID", example = "10") @PathVariable final Long genreId
    ) {
        log.info("PUT /books/{}/genres/{} request received", bookId, genreId);
        bookFacade.assignGenreToBook(genreId, bookId);
        return ResponseEntity.ok("Genre assigned to book");
    }

    private ResponseEntity<DeleteBookResponseDto> deleteBook(Long id) {
        bookFacade.deleteBookById(id);

        DeleteBookResponseDto body = mapFromLongIdToDeleteBookResponseDto(id);

        log.info("Book with id={} successfully deleted", id);

        return ResponseEntity.ok(body);
    }

}
