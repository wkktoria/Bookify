package io.github.wkktoria.bookify.infrastructure.crud.series.controller;

import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesWithAuthorsAndBooksDto;
import io.github.wkktoria.bookify.infrastructure.apivalidation.ApiValidationErrorResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.dto.ErrorResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.series.controller.dto.request.CreateSeriesWithBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.series.controller.dto.request.UpdateSeriesRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.series.controller.dto.response.GetAllSeriesResponseDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.github.wkktoria.bookify.infrastructure.crud.series.controller.SeriesControllerMapper.mapFromSeriesDtoListToGetAllSeriesResponseDto;

@Tag(name = "Series", description = "API for managing series")
@RestController
@RequestMapping("/series")
@AllArgsConstructor
@Log4j2
public class SeriesRestController {

    private final BookifyCrudFacade bookifyCrudFacade;

    @Operation(summary = "Get all series", description = "Returns list of series")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Series retrieved successfully",
                    content = @Content(schema = @Schema(implementation = GetAllSeriesResponseDto.class)))
    })
    @GetMapping
    ResponseEntity<GetAllSeriesResponseDto> getAllSeries(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        log.info("GET /series request received");

        List<SeriesDto> allSeries = bookifyCrudFacade.findAllSeries();
        GetAllSeriesResponseDto body = mapFromSeriesDtoListToGetAllSeriesResponseDto(allSeries);

        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Get series by ID with authors and books",
            description = "Returns series details including authors and books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Series found",
                    content = @Content(schema = @Schema(implementation = SeriesWithAuthorsAndBooksDto.class))),
            @ApiResponse(responseCode = "404", description = "Series not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    ResponseEntity<SeriesWithAuthorsAndBooksDto> getSeriesWithAuthorsAndBooks(@PathVariable final Long id) {
        log.info("GET /series/{} request received", id);
        return ResponseEntity.ok(
                bookifyCrudFacade.findSeriesByIdWithAuthorsAndBooks(id)
        );
    }

    @Operation(summary = "Create new series", description = "Creates a new series with associated book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Series created",
                    content = @Content(schema = @Schema(implementation = SeriesDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiValidationErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    ResponseEntity<SeriesDto> createSeries(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Series data",
            required = true,
            content = @Content(schema = @Schema(implementation = CreateSeriesWithBookRequestDto.class)))
                                           @RequestBody @Valid final CreateSeriesWithBookRequestDto requestDto) {
        log.info("POST /series request received with request body: {}", requestDto);

        SeriesDto seriesDto = bookifyCrudFacade
                .addSeriesWithBook(new SeriesRequestDto(requestDto.bookId(), requestDto.name()));

        log.info("Series created successfully with id={}", seriesDto.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seriesDto);
    }

    @Operation(summary = "Update series", description = "Updates series data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Series updated",
                    content = @Content(schema = @Schema(implementation = SeriesDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiValidationErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Series with the given name already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    ResponseEntity<SeriesDto> updateSeries(@Parameter(description = "Series ID", example = "1") @PathVariable final Long id,
                                           @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                   description = "Updated series data",
                                                   required = true,
                                                   content = @Content(schema = @Schema(implementation = UpdateSeriesRequestDto.class))
                                           )
                                           @RequestBody @Valid final UpdateSeriesRequestDto requestDto) {
        log.info("PUT /series/{} request received with request body: {}", id, requestDto);
        SeriesDto seriesDto = bookifyCrudFacade.updateSeries(id, requestDto.name());
        return ResponseEntity.ok(seriesDto);
    }

    @Operation(summary = "Add book to series", description = "Links a book to a series")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book assigned to series",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Series or Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{seriesId}/books/{bookId}")
    ResponseEntity<String> addBookToSeries(@Parameter(description = "Series ID", example = "1") @PathVariable final Long seriesId,
                                           @Parameter(description = "Book ID", example = "10") @PathVariable final Long bookId) {
        log.info("PUT /series/{}/books/{} request received", seriesId, bookId);
        bookifyCrudFacade.addBookToSeries(bookId, seriesId);
        return ResponseEntity.ok("Book added to series");
    }

    @Operation(summary = "Delete series", description = "Deletes series")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Series deleted",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Series has associated books and cannot be deleted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Series not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient privileges (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSeries(@Parameter(description = "Series ID", example = "1")
                                      @PathVariable final Long id) {
        log.info("DELETE /series/{} request received", id);
        bookifyCrudFacade.deleteSeries(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
