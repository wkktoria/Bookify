package io.github.wkktoria.bookify.feature;

import io.github.wkktoria.bookify.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.empty;


class UserWantsToCreateEntitiesButEndpointsAreSecuredIntegrationTest extends BaseIntegrationTest {

    @Test
    void user_wants_to_create_entities_but_endpoints_are_secured() throws Exception {
        // Step 1: When user posts to /authors without JWT then 401 Unauthorized is returned.
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                """.trim()))
                .andExpect(status().isUnauthorized());

        // Step 2: When user goes to /authors then user can see no authors.
        mockMvc.perform(get("/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authors", empty()));

        // Step 3: When user posts to /books without JWT then 401 Unauthorized is returned.
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                """.trim()))
                .andExpect(status().isUnauthorized());

        // Step 4: When user goes to /books then user can see no books.
        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books", empty()));

        // Step 5: When user posts to /genres without JWT then 401 Unauthorized is returned.
        mockMvc.perform(post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                """.trim()))
                .andExpect(status().isUnauthorized());

        // Step 6: When user goes to /genres then user can see the default genre.
        mockMvc.perform(get("/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genres", hasSize(1)));

        // Step 7: When user posts to /series without JWT then 401 Unauthorized is returned.
        mockMvc.perform(post("/series")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                """.trim()))
                .andExpect(status().isUnauthorized());

        // Step 8: When user goes to /series then user can see no series.
        mockMvc.perform(get("/series")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.series", empty()));
    }

}
