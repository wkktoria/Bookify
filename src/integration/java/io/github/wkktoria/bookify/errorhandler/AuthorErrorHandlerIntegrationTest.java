package io.github.wkktoria.bookify.errorhandler;

import io.github.wkktoria.bookify.BaseIntegrationTest;
import io.github.wkktoria.bookify.infrastructure.crud.author.controller.error.ErrorAuthorResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = "USER")
class AuthorErrorHandlerIntegrationTest extends BaseIntegrationTest {

    @Test
    void should_return_not_found_when_getting_nonexistent_author_by_id() throws Exception {
        ResultActions perform = mockMvc.perform(get("/authors/999")
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = perform.andExpect(status().isNotFound()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ErrorAuthorResponseDto result = objectMapper.readValue(json, ErrorAuthorResponseDto.class);
        assertThat(result.message()).isEqualTo("Could not find author with id=999");
    }

}
