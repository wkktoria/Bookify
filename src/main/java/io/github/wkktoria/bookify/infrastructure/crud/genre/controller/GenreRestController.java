package io.github.wkktoria.bookify.infrastructure.crud.genre.controller;

import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreWithBooksDto;
import io.github.wkktoria.bookify.infrastructure.apivalidation.ApiValidationErrorResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.dto.ErrorResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.genre.controller.dto.request.CreateGenreRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.genre.controller.dto.response.GetAllGenresResponsesDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.wkktoria.bookify.infrastructure.crud.genre.controller.GenreControllerMapper.mapFromGenreDtoSetToGetAllGenresResponseDto;

@Tag(name = "Genres", description = "API for managing genres")
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
@Log4j2
public class GenreRestController {

    private final BookifyCrudFacade bookifyCrudFacade;

    @Operation(summary = "Get all genres", description = "Returns list of genres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(schema = @Schema(implementation = GetAllGenresResponsesDto.class)))
    })
    @GetMapping
    ResponseEntity<GetAllGenresResponsesDto> getAllGenres() {
        log.info("GET /genres request received");
        return ResponseEntity.ok(mapFromGenreDtoSetToGetAllGenresResponseDto(bookifyCrudFacade.findAllGenres()));
    }

    @Operation(summary = "Get genre by ID with books", description = "Returns genre details including books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre found",
                    content = @Content(schema = @Schema(implementation = GenreWithBooksDto.class))),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    ResponseEntity<GenreWithBooksDto> getGenreWithBooks(@Parameter(description = "Genre ID", example = "1")
                                                        @PathVariable final Long id) {
        log.info("GET /genres/{} request received", id);
        return ResponseEntity.ok(bookifyCrudFacade.findGenreByIdWithBooks(id));
    }

    @Operation(summary = "Create new genre", description = "Creates a new genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Genre created",
                    content = @Content(schema = @Schema(implementation = GenreDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiValidationErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    ResponseEntity<GenreDto> createGenre(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Genre data",
            required = true,
            content = @Content(schema = @Schema(implementation = CreateGenreRequestDto.class))
    )
                                         @RequestBody @Valid final CreateGenreRequestDto requestDto) {
        log.info("POST /genres request received with request body: {}", requestDto);

        GenreDto genreDto = bookifyCrudFacade
                .addGenre(new GenreRequestDto(requestDto.name()));

        log.info("Genre created successfully with id={}", genreDto.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(genreDto);
    }

    @Operation(summary = "Delete genre", description = "Deletes genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Genre deleted",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Genre has associated books and cannot be deleted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteGenre(@Parameter(description = "Genre ID", example = "1")
                                     @PathVariable final Long id) {
        log.info("DELETE /genres/{} request received", id);
        bookifyCrudFacade.deleteGenre(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
