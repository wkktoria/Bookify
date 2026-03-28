package io.github.wkktoria.bookify.infrastructure.crud.genre.controller;

import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreWithBooksDto;
import io.github.wkktoria.bookify.infrastructure.crud.genre.controller.dto.request.CreateGenreRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.genre.controller.dto.response.GetAllGenresResponsesDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.wkktoria.bookify.infrastructure.crud.genre.controller.GenreControllerMapper.mapFromGenreDtoSetToGetAllGenresResponseDto;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
@Log4j2
class GenreRestController {

    private final BookifyCrudFacade bookifyCrudFacade;

    @GetMapping
    ResponseEntity<GetAllGenresResponsesDto> getAllGenres() {
        log.info("GET /genres request received");
        return ResponseEntity.ok(mapFromGenreDtoSetToGetAllGenresResponseDto(bookifyCrudFacade.findAllGenres()));
    }

    @GetMapping("/{id}")
    ResponseEntity<GenreWithBooksDto> getGenreWithBooks(@PathVariable final Long id) {
        log.info("GET /genres/{} request received", id);
        return ResponseEntity.ok(bookifyCrudFacade.findGenreByIdWithBooks(id));
    }

    @PostMapping
    ResponseEntity<GenreDto> createGenre(@RequestBody @Valid final CreateGenreRequestDto requestDto) {
        log.info("POST /genres request received with request body: {}", requestDto);

        GenreDto genreDto = bookifyCrudFacade
                .addGenre(new GenreRequestDto(requestDto.name()));

        log.info("Genre created successfully with id={}", genreDto.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genreDto);
    }

}
