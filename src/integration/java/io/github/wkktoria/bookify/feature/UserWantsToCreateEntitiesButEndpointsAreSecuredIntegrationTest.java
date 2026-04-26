package io.github.wkktoria.bookify.feature;

import io.github.wkktoria.bookify.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserWantsToCreateEntitiesButEndpointsAreSecuredIntegrationTest extends BaseIntegrationTest {

    @Test
    void user_wants_to_create_entities_but_endpoints_are_secured() throws Exception {
        // Step 1: When user posts to /authors without JWT then 401 Unauthorized is returned.
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                """.trim()))
                .andExpect(status().isUnauthorized());

        // Step 2: When user posts to /books without JWT then 401 Unauthorized is returned.
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                """.trim()))
                .andExpect(status().isUnauthorized());

        // Step 3: When user posts to /genres without JWT then 401 Unauthorized is returned.
        mockMvc.perform(post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                """.trim()))
                .andExpect(status().isUnauthorized());

        // Step 4: When user posts to /series without JWT then 401 Unauthorized is returned.
        mockMvc.perform(post("/series")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                """.trim()))
                .andExpect(status().isUnauthorized());
    }

}
