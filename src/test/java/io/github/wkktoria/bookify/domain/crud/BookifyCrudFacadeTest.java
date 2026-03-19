package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static io.github.wkktoria.bookify.domain.crud.BookifyCrudFasadeConfiguration.createBookifyCrudFasade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookifyCrudFacadeTest {

    BookifyCrudFacade bookifyCrudFacade = createBookifyCrudFasade(
            new InMemoryBookRepository(),
            new InMemoryGenreRepository(),
            new InMemoryAuthorRepository(),
            new InMemorySeriesRepository()
    );

    @Test
    @DisplayName("Should add author 'John Doe' with id:0 When 'John Doe' was sent")
    void should_add_author_john_doe_with_id_zero_when_john_doe_was_sent() {
        // given
        AuthorRequestDto author = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        List<AuthorDto> allAuthors = bookifyCrudFacade.findAllAuthors(Pageable.unpaged());
        assertTrue(allAuthors.isEmpty());

        // when
        AuthorDto result = bookifyCrudFacade.addAuthor(author);

        // then
        assertThat(result.id()).isEqualTo(0L);
        assertThat(result.firstname()).isEqualTo("John");
        assertThat(result.lastname()).isEqualTo("Doe");

        int size = bookifyCrudFacade.findAllAuthors(Pageable.unpaged()).size();
        assertThat(size).isEqualTo(1);
    }

}