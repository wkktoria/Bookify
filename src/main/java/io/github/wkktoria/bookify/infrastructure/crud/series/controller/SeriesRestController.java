package io.github.wkktoria.bookify.infrastructure.crud.series.controller;

import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesWithAuthorsAndBooksDto;
import io.github.wkktoria.bookify.infrastructure.crud.series.controller.dto.request.CreateSeriesWithBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.series.controller.dto.response.GetAllSeriesResponseDto;
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

@RestController
@RequestMapping("/series")
@AllArgsConstructor
@Log4j2
class SeriesRestController {

    private final BookifyCrudFacade bookifyCrudFacade;

    @GetMapping
    ResponseEntity<GetAllSeriesResponseDto> getAllSeries(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        log.info("GET /series request received");

        List<SeriesDto> allSeries = bookifyCrudFacade.findAllSeries();
        GetAllSeriesResponseDto body = mapFromSeriesDtoListToGetAllSeriesResponseDto(allSeries);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    ResponseEntity<SeriesWithAuthorsAndBooksDto> getSeriesWithAuthorsAndBooks(@PathVariable final Long id) {
        log.info("GET /series/{} request received", id);
        return ResponseEntity.ok(
                bookifyCrudFacade.findSeriesByIdWithAuthorsAndBooks(id)
        );
    }

    @PostMapping
    ResponseEntity<SeriesDto> createSeries(@RequestBody @Valid final CreateSeriesWithBookRequestDto requestDto) {
        log.info("POST /series request received with request body: {}", requestDto);

        SeriesDto seriesDto = bookifyCrudFacade
                .addSeriesWithBook(new SeriesRequestDto(requestDto.bookId(), requestDto.name()));

        log.info("Series created successfully with id={}", seriesDto.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seriesDto);
    }

    @PutMapping("/{seriesId}/books/{bookId}")
    ResponseEntity<String> addBookToSeries(@PathVariable final Long seriesId,
                                           @PathVariable final Long bookId) {
        log.info("PUT /series/{}/books/{} request received", seriesId, bookId);
        bookifyCrudFacade.addBookToSeries(bookId, seriesId);
        return ResponseEntity.ok("Book added to series");
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSeries(@PathVariable final Long id) {
        log.info("DELETE /series/{} request received", id);
        bookifyCrudFacade.deleteSeries(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
