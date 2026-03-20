package io.github.wkktoria.bookify.infrastructure.crud.author.controller;

import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.UpdateAuthorRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.author.controller.dto.request.CreateAuthorRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.author.controller.dto.response.AllAuthorsResponseDto;
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

@RestController
@RequestMapping("/authors")
@AllArgsConstructor
@Log4j2
class AuthorRestController {

    private final BookifyCrudFacade bookifyCrudFacade;

    @GetMapping
    ResponseEntity<AllAuthorsResponseDto> getAuthors(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        log.info("GET /authors request received");

        List<AuthorDto> allAuthors = bookifyCrudFacade.findAllAuthors(pageable);
        AllAuthorsResponseDto body = new AllAuthorsResponseDto(allAuthors);

        log.debug("Returning {} authors", allAuthors.size());

        return ResponseEntity.ok(body);
    }

    @PostMapping
    ResponseEntity<AuthorDto> createAuthor(@RequestBody final CreateAuthorRequestDto requestDto) {
        log.info("POST /authors request received");

        AuthorDto authorDto = bookifyCrudFacade
                .addAuthor(new AuthorRequestDto(requestDto.firstname(), requestDto.lastname()));

        log.info("Author created successfully with id={}", authorDto.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorDto);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteAuthorWithAllBooks(@PathVariable final Long id) {
        log.info("DELETE /authors/{} request received", id);

        bookifyCrudFacade.deleteAuthorByIdWithBooks(id);
        return ResponseEntity.ok("Author deleted");
    }

    @PutMapping("/{authorId}/books/{bookId}")
    ResponseEntity<String> assignAuthorToBook(@PathVariable final Long authorId,
                                              @PathVariable final Long bookId) {
        log.info("PUT /authors/{}/books/{} request received", authorId, bookId);
        bookifyCrudFacade.addAuthorToBook(authorId, bookId);
        return ResponseEntity.ok("Author assigned to book");
    }

    @PatchMapping("/{id}")
    ResponseEntity<AuthorDto> updateAuthorById(@PathVariable final Long id,
                                               @RequestBody @Valid final UpdateAuthorRequestDto requestDto) {
        log.info("PATCH /authors/{} request received with request body: {}", id, requestDto);
        AuthorDto authorDto = bookifyCrudFacade.updateAuthorById(id, requestDto);
        return ResponseEntity.ok(authorDto);
    }

}
