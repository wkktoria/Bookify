package io.github.wkktoria.bookify.infrastructure.crud.author.controller;

import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorWithBooksDto;
import io.github.wkktoria.bookify.domain.crud.dto.UpdateAuthorRequestDto;
import io.github.wkktoria.bookify.infrastructure.apivalidation.ApiValidationErrorResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.author.controller.dto.request.CreateAuthorRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.author.controller.dto.response.AllAuthorsResponseDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Authors", description = "API for managing authors")
@RestController
@RequestMapping("/authors")
@AllArgsConstructor
@Log4j2
public class AuthorRestController {

    private final BookifyCrudFacade bookifyCrudFacade;

    @Operation(summary = "Get all authors", description = "Returns paginated list of authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authors retrieved successfully")
    })
    @GetMapping
    ResponseEntity<AllAuthorsResponseDto> getAuthors(
            @Parameter(description = "Pagination parameters")
            @PageableDefault(page = 0, size = 10) final Pageable pageable) {
        log.info("GET /authors request received");

        List<AuthorDto> allAuthors = bookifyCrudFacade.findAllAuthors(pageable);
        AllAuthorsResponseDto body = new AllAuthorsResponseDto(allAuthors);

        log.debug("Returning {} authors", allAuthors.size());

        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Get author by ID with books", description = "Returns author details including books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<AuthorWithBooksDto> getAuthorWithBooks(
            @Parameter(description = "Author ID", example = "1")
            @PathVariable final Long id) {
        log.info("GET /authors/{} request received", id);
        return ResponseEntity.ok(bookifyCrudFacade.findAuthorByIdWithBooks(id));
    }

    @Operation(summary = "Create new author", description = "Creates a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created",
                    content = @Content(schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiValidationErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    ResponseEntity<AuthorDto> createAuthor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Author data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateAuthorRequestDto.class))
            )
            @RequestBody @Valid final CreateAuthorRequestDto requestDto) {
        log.info("POST /authors request received");

        AuthorDto authorDto = bookifyCrudFacade
                .addAuthor(new AuthorRequestDto(requestDto.firstname(), requestDto.lastname()));

        log.info("Author created successfully with id={}", authorDto.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorDto);
    }

    @Operation(summary = "Delete author", description = "Deletes author and all associated books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author deleted",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteAuthorWithAllBooks(
            @Parameter(description = "Author ID", example = "1")
            @PathVariable final Long id) {
        log.info("DELETE /authors/{} request received", id);

        bookifyCrudFacade.deleteAuthorByIdWithBooks(id);
        return ResponseEntity.ok("Author deleted");
    }

    @Operation(summary = "Assign author to book", description = "Links an author to a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author assigned to book",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Author or Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{authorId}/books/{bookId}")
    ResponseEntity<String> assignAuthorToBook(
            @Parameter(description = "Author ID", example = "1") @PathVariable final Long authorId,
            @Parameter(description = "Book ID", example = "10") @PathVariable final Long bookId) {
        log.info("PUT /authors/{}/books/{} request received", authorId, bookId);
        bookifyCrudFacade.addAuthorToBook(authorId, bookId);
        return ResponseEntity.ok("Author assigned to book");
    }

    @Operation(summary = "Update author", description = "Partially updates author data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated",
                    content = @Content(schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("/{id}")
    ResponseEntity<AuthorDto> updateAuthorById(@Parameter(description = "Author ID", example = "1") @PathVariable final Long id,
                                               @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                       description = "Updated author data",
                                                       required = true,
                                                       content = @Content(schema = @Schema(implementation = UpdateAuthorRequestDto.class))
                                               ) @RequestBody @Valid final UpdateAuthorRequestDto requestDto) {
        log.info("PATCH /authors/{} request received with request body: {}", id, requestDto);
        AuthorDto authorDto = bookifyCrudFacade.updateAuthorById(id, requestDto);
        return ResponseEntity.ok(authorDto);
    }

    @Operation(summary = "Create author with default series and book",
            description = "Creates author and automatically assigns default series and book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author with series and book created",
                    content = @Content(schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/series/book")
    ResponseEntity<AuthorDto> addAuthorWithDefaultSeriesAndBook(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Author data",
            required = true,
            content = @Content(schema = @Schema(implementation = AuthorRequestDto.class))
    ) @RequestBody @Valid final AuthorRequestDto requestDto) {
        log.info("POST /authors/series/book request received");
        AuthorDto authorDto = bookifyCrudFacade.addAuthorWithDefaultSeriesAndBook(requestDto);
        return ResponseEntity.ok(authorDto);
    }

}
