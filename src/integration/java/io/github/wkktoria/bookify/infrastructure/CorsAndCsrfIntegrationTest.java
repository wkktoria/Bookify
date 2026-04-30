package io.github.wkktoria.bookify.infrastructure;

import io.github.wkktoria.bookify.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CorsAndCsrfIntegrationTest extends BaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:18-alpine");

    @DynamicPropertySource
    static void propertyOverride(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    // CSRF is disabled
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCsrf() throws Exception {
        mockMvc.perform(post("/genres")
                        .content("""
                                {
                                    "name": "Test"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void testCors() throws Exception {
        mockMvc.perform(options("/books")
                        .header("Access-Control-Request-Method", List.of("GET", "POST", "PUT", "DELETE", "PATCH"))
                        .header("Origin", "https://localhost:5173"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,PATCH"))
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "https://localhost:5173"))
                .andExpect(status().isOk());
    }

}
