package io.github.wkktoria.bookify.errorhandler;

import io.github.wkktoria.bookify.BaseIntegrationTest;
import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookLanguageDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.genre.controller.error.ErrorGenreResponseDto;
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

class GenreErrorHandlerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BookifyCrudFacade bookifyCrudFacade;

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
    void test() throws Exception {
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

}
