package io.github.wkktoria.bookify.apivalidationerror;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.wkktoria.bookify.BaseIntegrationTest;
import io.github.wkktoria.bookify.infrastructure.apivalidation.ApiValidationErrorResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = "ADMIN")
class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_bad_request_when_blanks_and_nulls_in_create_book_request() throws Exception {
        ResultActions perform = mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .content("""
                        {
                            "bookTitle": "",
                            "publicationDate": "",
                            "isbn": "",
                            "pages": null,
                            "authorId": null,
                            "language": null
                        }
                        """));

        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        System.out.println(json);
        ApiValidationErrorResponseDto result = objectMapper.readValue(json, ApiValidationErrorResponseDto.class);
        assertThat(result.errors()).containsExactlyInAnyOrder(
                "bookTitle must not be empty",
                "publicationDate must not be null",
                "isbn must not be empty",
                "pages must not be null",
                "authorId must not be null",
                "language must not be null"
        );
    }

}
