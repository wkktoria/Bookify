package io.github.wkktoria.bookify.infrastructure.crud.series.controller;

import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.series.controller.dto.request.CreateSeriesWithBookRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/series")
@AllArgsConstructor
@Log4j2
class SeriesRestController {

    private final BookifyCrudFacade bookifyCrudFacade;

    @PostMapping
    ResponseEntity<SeriesDto> createSeries(@RequestBody @Valid final CreateSeriesWithBookRequestDto requestDto) {
        log.info("POST /series request received with request body: {}", requestDto);

        SeriesDto seriesDto = bookifyCrudFacade
                .addSeriesWithBook(new SeriesRequestDto(requestDto.bookId(), requestDto.name()));

        log.info("Series created successfully with id={}", seriesDto.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seriesDto);
    }

}
