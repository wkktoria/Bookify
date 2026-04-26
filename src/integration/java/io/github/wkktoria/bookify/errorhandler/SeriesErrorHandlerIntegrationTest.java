package io.github.wkktoria.bookify.errorhandler;

import io.github.wkktoria.bookify.BaseIntegrationTest;
import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookLanguageDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.series.controller.error.ErrorSeriesResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SeriesErrorHandlerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BookifyCrudFacade bookifyCrudFacade;

    @Test
    @WithMockUser(roles = "USER")
    void should_return_not_found_when_getting_nonexistent_series_by_id() throws Exception {
        ResultActions perform = mockMvc.perform(get("/series/999")
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = perform.andExpect(status().isNotFound()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ErrorSeriesResponseDto result = objectMapper.readValue(json, ErrorSeriesResponseDto.class);
        assertThat(result.message()).isEqualTo("Could not find series with id=999");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_return_conflict_when_deleting_series_with_assigned_books() throws Exception {
        // given
        AuthorDto authorDto = bookifyCrudFacade.addAuthor(AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build());
        BookDto bookDto = bookifyCrudFacade.addBookWithAuthor(BookRequestDto.builder()
                .title("Test Book")
                .publicationDate(LocalDate.now())
                .isbn("1234567890123")
                .pages(100)
                .authorId(authorDto.id())
                .language(BookLanguageDto.ENGLISH)
                .build());
        SeriesDto seriesDto = bookifyCrudFacade.addSeriesWithBook(new SeriesRequestDto(bookDto.id(), "TEST"));

        // when
        ResultActions perform = mockMvc.perform(delete("/series/{id}", seriesDto.id())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult mvcResult = perform.andExpect(status().isConflict()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ErrorSeriesResponseDto result = objectMapper.readValue(json, ErrorSeriesResponseDto.class);
        assertThat(result.message()).isEqualTo("Could not delete series with id=" + seriesDto.id());
    }

}
