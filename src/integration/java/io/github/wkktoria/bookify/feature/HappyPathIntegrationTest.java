package io.github.wkktoria.bookify.feature;

import io.github.wkktoria.bookify.BaseIntegrationTest;
import io.github.wkktoria.bookify.infrastructure.security.jwt.JwtAuthConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HappyPathIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private JwtAuthConverter jwtAuthConverter;

    @Test
    void happy_path() throws Exception {
        // 1. When user goes to /authors then user can see no authors.
        mockMvc.perform(get("/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authors", empty()));

        // SECURITY STEP - When user goes to /authors with JWT then user can see no authors.
        mockMvc.perform(get("/authors")
                        .with(authentication(createJwtWithUserRole()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authors", empty()));

        // 2. When user posts to /authors with admin role and author "Eric Freeman" then author "Eric Freeman" is returned with id 1.
        mockMvc.perform(post("/authors")
                        .with(authentication(createJwtWithAdminRole()))
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

        // SECURITY STEP - When user posts to /authors without JWT then 401 Unauthorized is returned.
        mockMvc.perform(post("/authors"))
                .andExpect(status().isUnauthorized());

        // SECURITY STEP - When user posts to /authors with user role and author "Eric Freeman" then 403 Forbidden is returned.
        mockMvc.perform(post("/authors")
                        .with(authentication(createJwtWithUserRole()))
                        .content("""
                                {
                                    "firstname": "Eric",
                                    "lastname": "Freeman"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        // 3. When user posts to /authors with admin role and author "Elisabeth Robson" then author "Elisabeth Robson" is returned with id 2.
        mockMvc.perform(post("/authors")
                        .with(authentication(createJwtWithAdminRole()))
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
        mockMvc.perform(get("/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genres[0].id", is(1)))
                .andExpect(jsonPath("$.genres[0].name", is("default")));

        // 5. When user posts to /genres with admin role and genre "Software Engineering" then genre "Software Engineering" is returned with id 2.
        mockMvc.perform(post("/genres")
                        .with(authentication(createJwtWithAdminRole()))
                        .content("""
                                {
                                    "name": "Software Engineering"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Software Engineering")));

        // 6. When user goes to /books then user can see no books.
        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books", empty()));

        // 7. When user posts to /books with admin role and book "Head First Design Patterns" of author with id 1 ("Eric Freeman") then book "Head First Design Patterns" is returned with id 1.
        mockMvc.perform(post("/books")
                        .with(authentication(createJwtWithAdminRole()))
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

        // 8. When user posts to /books with admin role and book "Head First JavaScript" of author with id 1 ("Eric Freeman") then book "Head First JavaScript" is returned with id 2.
        mockMvc.perform(post("/books")
                        .with(authentication(createJwtWithAdminRole()))
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
        mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.id", is(1)))
                .andExpect(jsonPath("$.book.title", is("Head First Design Patterns")))
                .andExpect(jsonPath("$.book.genre.id", is(1)))
                .andExpect(jsonPath("$.book.genre.name", is("default")));

        // 10. When user puts to /books/1/genres/2 with admin role then genre with id 2 ("Software Engineering") is added to book with id 1 ("Head First Design Patterns").
        mockMvc.perform(put("/books/1/genres/2")
                        .with(authentication(createJwtWithAdminRole()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Genre assigned to book")));

        // 11. When user goes to /books/1 then user can see book info and "Software Engineering" genre.
        mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.id", is(1)))
                .andExpect(jsonPath("$.book.title", is("Head First Design Patterns")))
                .andExpect(jsonPath("$.book.genre.id", is(2)))
                .andExpect(jsonPath("$.book.genre.name", is("Software Engineering")));

        // 12. When user puts to /authors/2/books/1 with admin role then author with id 2 ("Elisabeth Robson") is added to book with id 1 ("Head First Design Patterns").
        mockMvc.perform(put("/authors/2/books/1")
                        .with(authentication(createJwtWithAdminRole()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Author assigned to book")));

        // 13. When user goes to /books/1 then user can see book info and 2 authors (id1 and id2).
        mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.id", is(1)))
                .andExpect(jsonPath("$.book.title", is("Head First Design Patterns")))
                .andExpect(jsonPath("$.book.authors[*].id", containsInAnyOrder(1, 2)));

        // 14. When user goes to /series then user can see no series.
        mockMvc.perform(get("/series")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.series", empty()));

        // 15. When user posts to /series with admin role and series "Head First Series" and book with id 1 then series "Head First Series" is returned with id 1.
        mockMvc.perform(post("/series")
                        .with(authentication(createJwtWithAdminRole()))
                        .content("""
                                {
                                    "name": "Head First Series",
                                    "bookId": 1
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Head First Series")));

        // 16. When users puts to /series/1/books/2 with admin role then book with id 2 ("Head First JavaScript") is added to series with id 1 (" Head First Series").
        mockMvc.perform(put("/series/1/books/2")
                        .with(authentication(createJwtWithAdminRole()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Book added to series")));

        // 17. When user goes to /series/1 then user can see series with 2 books (id1 and id2).
        mockMvc.perform(get("/series/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.series.id", is(1)))
                .andExpect(jsonPath("$.series.name", is("Head First Series")))
                .andExpect(jsonPath("$.books[*].id", containsInAnyOrder(1, 2)));
    }

    private JwtAuthenticationToken createJwtWithAdminRole() {
        Jwt jwt = Jwt.withTokenValue("123")
                .claim("email", "admin@bookify.com")
                .header("alg", "none")
                .build();
        return jwtAuthConverter.convert(jwt);
    }

    private JwtAuthenticationToken createJwtWithUserRole() {
        Jwt jwt = Jwt.withTokenValue("123")
                .claim("email", "user@bookify.com")
                .header("alg", "none")
                .build();
        return jwtAuthConverter.convert(jwt);
    }

}
