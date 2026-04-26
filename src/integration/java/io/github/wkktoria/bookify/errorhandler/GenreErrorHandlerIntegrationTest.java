package io.github.wkktoria.bookify.errorhandler;

import io.github.wkktoria.bookify.BaseIntegrationTest;
import io.github.wkktoria.bookify.infrastructure.crud.genre.controller.error.ErrorGenreResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GenreErrorHandlerIntegrationTest extends BaseIntegrationTest {

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

}
