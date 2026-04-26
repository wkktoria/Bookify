package io.github.wkktoria.bookify.infrastructure;

import io.github.wkktoria.bookify.BaseIntegrationTest;
import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookLanguageDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.author.controller.error.ErrorAuthorResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.error.ErrorBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.genre.controller.error.ErrorGenreResponseDto;
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

class ErrorHandlerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BookifyCrudFacade bookifyCrudFacade;

    @Test
    @WithMockUser(roles = "USER")
    void should_return_not_found_when_getting_nonexistent_author_by_id() throws Exception {
        ResultActions perform = mockMvc.perform(get("/authors/999")
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = perform.andExpect(status().isNotFound()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ErrorAuthorResponseDto result = objectMapper.readValue(json, ErrorAuthorResponseDto.class);
        assertThat(result.message()).isEqualTo("Could not find author with id=999");
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_return_not_found_when_getting_nonexistent_book_by_id() throws Exception {
        ResultActions perform = mockMvc.perform(get("/books/999")
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = perform.andExpect(status().isNotFound()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ErrorBookResponseDto result = objectMapper.readValue(json, ErrorBookResponseDto.class);
        assertThat(result.message()).isEqualTo("Could not find book with id=999");
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_return_not_found_when_getting_nonexistent_genre_by_id() throws Exception {
        ResultActions perform = mockMvc.perform(get("/genres/999")
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = perform.andExpect(status().isNotFound()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ErrorGenreResponseDto result = objectMapper.readValue(json, ErrorGenreResponseDto.class);
        assertThat(result.message()).isEqualTo("Could not find genre with id=999");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_return_conflict_when_deleting_genre_with_assigned_books() throws Exception {
        // given
        GenreDto genreDto = bookifyCrudFacade.addGenre(new GenreRequestDto("TEST"));
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
        bookifyCrudFacade.assignGenreToBook(genreDto.id(), bookDto.id());

        // when
        ResultActions perform = mockMvc.perform(delete("/genres/{id}", genreDto.id())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult mvcResult = perform.andExpect(status().isConflict()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ErrorGenreResponseDto result = objectMapper.readValue(json, ErrorGenreResponseDto.class);
        assertThat(result.message()).isEqualTo("Could not delete genre with id=" + genreDto.id());
    }

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
