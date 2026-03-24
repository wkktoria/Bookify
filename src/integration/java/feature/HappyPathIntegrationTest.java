package feature;

import io.github.wkktoria.bookify.BookifyApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BookifyApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("integration")
class HappyPathIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:18-alpine");

    @DynamicPropertySource
    public static void propertyOverride(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Autowired
    public MockMvc mockMvc;

    @Test
    void happy_path() throws Exception {
        // 1. When user goes to /authors then user can see no authors.
        mockMvc.perform(get("/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authors", empty()));

        // 2. When user posts to /authors with author "Eric Freeman" then author "Eric Freeman" is returned with id 1.
        mockMvc.perform(post("/authors")
                        .content("""
                                {
                                    "firstname": "Eric",
                                    "lastname": "Freeman"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Eric")))
                .andExpect(jsonPath("$.lastname", is("Freeman")));

        // 3. When user posts to /authors with author "Elisabeth Robson" then author "Elisabeth Robson" is returned with id 2.
        mockMvc.perform(post("/authors")
                        .content("""
                                {
                                    "firstname": "Elisabeth",
                                    "lastname": "Robson"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstname", is("Elisabeth")))
                .andExpect(jsonPath("$.lastname", is("Robson")));

        // 4. When user goes to /genres then user can see only default genre with id 1.

        // 5. When user posts to /genres with genre "Software Engineering" then genre "Software Engineering" is returned with id 2.

        // 6. When user goes to /books then user can see no books.
        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books", empty()));

        // 7. When user posts to /books with book "Head First Design Patterns" of author with id 1 ("Eric Freeman") then book "Head First Design Patterns" is returned with id 1.
        mockMvc.perform(post("/books")
                        .content("""
                                {
                                    "bookTitle": "Head First Design Patterns",
                                    "publicationDate": "2004-10-01",
                                    "isbn": "9780596007126",
                                    "pages": 692,
                                    "authorId": 1,
                                    "language": "ENGLISH"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.id", is(1)))
                .andExpect(jsonPath("$.book.title", is("Head First Design Patterns")))
                .andExpect(jsonPath("$.book.genre.id", is(1)))
                .andExpect(jsonPath("$.book.genre.name", is("default")))
                .andExpect(jsonPath("$.book.authors[0].id", is(1)))
                .andExpect(jsonPath("$.book.authors[0].firstname", is("Eric")))
                .andExpect(jsonPath("$.book.authors[0].lastname", is("Freeman")));

        // 8. When user posts to /books with book "Head First JavaScript" of author with id 1 ("Eric Freeman") then book "Head First JavaScript" is returned with id 2.
        mockMvc.perform(post("/books")
                        .content("""
                                {
                                    "bookTitle": "Head First JavaScript",
                                    "publicationDate": "2014-05-06",
                                    "isbn": "9781449340131",
                                    "pages": 700,
                                    "authorId": 1,
                                    "language": "ENGLISH"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.id", is(2)))
                .andExpect(jsonPath("$.book.title", is("Head First JavaScript")))
                .andExpect(jsonPath("$.book.genre.id", is(1)))
                .andExpect(jsonPath("$.book.genre.name", is("default")))
                .andExpect(jsonPath("$.book.authors[0].id", is(1)))
                .andExpect(jsonPath("$.book.authors[0].firstname", is("Eric")))
                .andExpect(jsonPath("$.book.authors[0].lastname", is("Freeman")));

        // 9. When user goes to /books/1 then user can see book info and the default genre with id 1 and name default.

        // 10. When user puts to /books/1/genre/2 then genre with id 2 ("Software Engineering") is added to book with id 1 ("Head First Design Patterns").

        // 11. When user goes to /books/1 then user can see book info and "Software Engineering" genre.

        // 12. When user goes to /series then user can see no series.

        // 13. When user posts to /series with series "Head First Series" and book with id 1 then series "Head First Series" is returned with id 1.

        // 14. When users puts to /series/1/books/2 then book with id 2 ("Head First JavaScript") is added to series with id 1 (" Head First Series").

        // 15. When user goes to /series/1 then user can see series with 2 books (id1 and id2).

    }

}
