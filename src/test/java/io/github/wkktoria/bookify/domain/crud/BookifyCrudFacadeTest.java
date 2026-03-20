package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookLanguageDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.UpdateAuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.UpdateGenreRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static io.github.wkktoria.bookify.domain.crud.BookifyCrudFacadeConfiguration.createBookifyCrudFacade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookifyCrudFacadeTest {

    BookifyCrudFacade bookifyCrudFacade = createBookifyCrudFacade(
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

    @Test
    @DisplayName("Should throw exception AuthorNotFoundException When deleting author and id:0")
    void should_throw_author_not_found_exception_when_deleting_author_and_id_was_zero() {
        // given
        assertThat(bookifyCrudFacade.findAllAuthors(Pageable.unpaged())).isEmpty();

        // when
        Throwable throwable = catchThrowable(() -> bookifyCrudFacade
                .deleteAuthorByIdWithBooks(0L));

        // then
        assertThat(throwable).isInstanceOf(AuthorNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Could not find author with id=0");
    }

    @Test
    @DisplayName("Should delete author by id When no books associated with author")
    void should_delete_author_by_id_when_no_books_associated_with_author() {
        // given
        AuthorRequestDto johnDoe = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        Long authorId = bookifyCrudFacade.addAuthor(johnDoe).id();
        assertThat(bookifyCrudFacade.findBooksByAuthorId(authorId)).isEmpty();

        // when
        bookifyCrudFacade.deleteAuthorByIdWithBooks(authorId);

        // then
        assertThat(bookifyCrudFacade.findAllAuthors(Pageable.unpaged())).isEmpty();
    }

    @Test
    @DisplayName("Should delete author with book by id When there is association with one book and author is the only author of book")
    void should_delete_author_by_id_when_there_is_association_with_one_book_and_author_is_the_only_author_of_book() {
        // given
        AuthorRequestDto johnDoe = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        Long authorId = bookifyCrudFacade.addAuthor(johnDoe).id();

        BookRequestDto book = BookRequestDto.builder()
                .title("Book Title")
                .publicationDate(LocalDate.now())
                .isbn("1234567890123")
                .pages(100)
                .authorId(authorId)
                .language(BookLanguageDto.ENGLISH)
                .build();
        Long bookId = bookifyCrudFacade.addBookWithAuthor(book).id();

        assertThat(bookifyCrudFacade.findBooksByAuthorId(authorId)).size().isEqualTo(1);
        assertThat(bookifyCrudFacade.countAuthorsByBookId(bookId)).isEqualTo(1);

        // when
        bookifyCrudFacade.deleteAuthorByIdWithBooks(authorId);

        // then
        assertThat(bookifyCrudFacade.findAllAuthors(Pageable.unpaged())).isEmpty();

        Throwable throwable = catchThrowable(() -> bookifyCrudFacade.findBookById(bookId));
        assertThat(throwable).isInstanceOf(BookNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Could not find book with id=" + bookId);
    }

    @Test
    @DisplayName("Should delete author by id and do not delete book When book has more authors")
    void should_delete_author_by_id_and_do_not_delete_book_when_book_has_more_authors() {
        // given
        AuthorRequestDto johnDoe = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        Long johnDoeId = bookifyCrudFacade.addAuthor(johnDoe).id();
        AuthorRequestDto samSmith = AuthorRequestDto.builder()
                .firstname("Sam")
                .lastname("Smith")
                .build();
        AuthorDto samSmithDto = bookifyCrudFacade.addAuthor(samSmith);
        Long samSmithId = samSmithDto.id();

        BookRequestDto book = BookRequestDto.builder()
                .title("Book Title")
                .publicationDate(LocalDate.now())
                .isbn("1234567890123")
                .pages(100)
                .authorId(johnDoeId)
                .language(BookLanguageDto.ENGLISH)
                .build();
        BookDto bookDto = bookifyCrudFacade.addBookWithAuthor(book);
        Long bookId = bookDto.id();

        bookifyCrudFacade.addAuthorToBook(samSmithId, bookId);

        assertThat(bookifyCrudFacade.countAuthorsByBookId(bookId)).isEqualTo(2);

        // when
        bookifyCrudFacade.deleteAuthorByIdWithBooks(johnDoeId);

        // then
        assertThat(bookifyCrudFacade.findBookById(bookId)).isEqualTo(bookDto);
        assertThat(bookifyCrudFacade.countAuthorsByBookId(bookId)).isEqualTo(1);
        assertThat(bookifyCrudFacade.findAuthorsByBookId(bookId)).containsExactly(samSmithDto);
    }

    @Test
    @DisplayName("Should add book with author")
    void should_add_book_with_author() {
        // given
        AuthorRequestDto johnDoe = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        Long authorId = bookifyCrudFacade.addAuthor(johnDoe).id();

        BookRequestDto book = BookRequestDto.builder()
                .title("Book Title")
                .publicationDate(LocalDate.now())
                .isbn("1234567890123")
                .pages(100)
                .authorId(authorId)
                .language(BookLanguageDto.ENGLISH)
                .build();

        assertThat(bookifyCrudFacade.findAllBooks(Pageable.unpaged())).isEmpty();

        // when
        BookDto result = bookifyCrudFacade.addBookWithAuthor(book);

        // then
        List<BookDto> allBooks = bookifyCrudFacade.findAllBooks(Pageable.unpaged());
        assertThat(allBooks).extracting(BookDto::id).containsExactly(0L);
        assertThat(bookifyCrudFacade.findBooksByAuthorId(authorId)).containsExactly(result);
    }

    @Test
    @DisplayName("Should update author by id When new firstname was sent")
    void should_update_author_by_id_when_new_firstname_was_sent() {
        // given
        AuthorRequestDto author = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        Long authorId = bookifyCrudFacade.addAuthor(author).id();

        UpdateAuthorRequestDto updateRequest = UpdateAuthorRequestDto.builder()
                .firstname("Johnny")
                .build();

        // when
        AuthorDto result = bookifyCrudFacade.updateAuthorById(authorId, updateRequest);

        // then
        assertThat(result.firstname()).isEqualTo("Johnny");
        assertThat(result.lastname()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Should update author by id When new firstname and lastname were sent")
    void should_update_author_by_id_when_new_firstname_and_lastname_were_sent() {
        // given
        AuthorRequestDto author = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        Long authorId = bookifyCrudFacade.addAuthor(author).id();

        UpdateAuthorRequestDto updateRequest = UpdateAuthorRequestDto.builder()
                .firstname("Johnny")
                .lastname("Deep")
                .build();

        // when
        AuthorDto result = bookifyCrudFacade.updateAuthorById(authorId, updateRequest);

        // then
        assertThat(result.firstname()).isEqualTo("Johnny");
        assertThat(result.lastname()).isEqualTo("Deep");
    }

    @Test
    @DisplayName("Should not update author by id When firstname and lastname were not sent")
    void should_not_update_author_by_id_when_firstname_and_lastname_were_not_sent() {
        // given
        AuthorRequestDto author = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        Long authorId = bookifyCrudFacade.addAuthor(author).id();

        UpdateAuthorRequestDto updateRequest = UpdateAuthorRequestDto.builder()
                .build();

        // when
        AuthorDto result = bookifyCrudFacade.updateAuthorById(authorId, updateRequest);

        // then
        assertThat(result.firstname()).isEqualTo("John");
        assertThat(result.lastname()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Should throw AuthorNotFoundException When updating author and id:0")
    void should_throw_author_not_found_exception_when_updating_author_and_id_was_zero() {
        // given
        assertThat(bookifyCrudFacade.findAllAuthors(Pageable.unpaged())).isEmpty();

        // when
        Throwable throwable = catchThrowable(() -> bookifyCrudFacade
                .updateAuthorById(0L, UpdateAuthorRequestDto.builder().build()));

        // then
        assertThat(throwable).isInstanceOf(AuthorNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Could not find author with id=0");
    }

    @Test
    @DisplayName("Should update genre by id When new name was sent")
    void should_update_genre_by_id_when_new_name_was_sent() {
        // given
        GenreRequestDto genre = new GenreRequestDto("Genre");
        Long genreId = bookifyCrudFacade.addGenre(genre).id();

        UpdateGenreRequestDto updateRequest = new UpdateGenreRequestDto("New Genre");

        // when
        GenreDto result = bookifyCrudFacade.updateGenreById(genreId, updateRequest);

        // then
        assertThat(result.name()).isEqualTo("New Genre");
    }

    @Test
    @DisplayName("Should not update genre by id When new name is null")
    void should_not_update_genre_by_id_when_new_name_is_null() {
        // given
        GenreRequestDto genre = new GenreRequestDto("Genre");
        Long genreId = bookifyCrudFacade.addGenre(genre).id();

        UpdateGenreRequestDto updateRequest = new UpdateGenreRequestDto(null);

        // when
        GenreDto result = bookifyCrudFacade.updateGenreById(genreId, updateRequest);

        // then
        assertThat(result.name()).isNotNull();
        assertThat(result.name()).isEqualTo("Genre");
    }

}