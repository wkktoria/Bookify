package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.AuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookLanguageDto;
import io.github.wkktoria.bookify.domain.crud.dto.BookRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreDto;
import io.github.wkktoria.bookify.domain.crud.dto.GenreRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.SeriesWithAuthorsAndBooksDto;
import io.github.wkktoria.bookify.domain.crud.dto.UpdateAuthorRequestDto;
import io.github.wkktoria.bookify.domain.crud.dto.UpdateGenreRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    @Test
    @DisplayName("Should throw GenreNotDeletedException When deleting genre with assigned books")
    void should_throw_genere_not_deleted_exception_when_deleting_genre_with_assigned_books() {
        // given
        GenreRequestDto genreRequest = new GenreRequestDto("Programming");
        GenreDto genre = bookifyCrudFacade.addGenre(genreRequest);

        AuthorRequestDto authorRequest = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        AuthorDto author = bookifyCrudFacade.addAuthor(authorRequest);

        BookRequestDto bookRequest = BookRequestDto.builder()
                .title("Programming Book")
                .publicationDate(LocalDate.now())
                .isbn("123456789123")
                .pages(100)
                .authorId(author.id())
                .language(BookLanguageDto.ENGLISH)
                .build();
        BookDto book = bookifyCrudFacade.addBookWithAuthor(bookRequest);

        bookifyCrudFacade.assignGenreToBook(genre.id(), book.id());

        // when
        Throwable throwable = catchThrowable(() -> bookifyCrudFacade.deleteGenre(genre.id()));

        // then
        assertThat(throwable).isInstanceOf(GenreNotDeletedException.class)
                .hasMessageContaining(genre.id().toString());
    }

    @Test
    @DisplayName("Should add series with book")
    void should_add_series_with_book() {
        // given
        AuthorRequestDto author = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        AuthorDto authorDto = bookifyCrudFacade.addAuthor(author);

        BookRequestDto book = BookRequestDto.builder()
                .title("")
                .publicationDate(LocalDate.now())
                .isbn("1234567890123")
                .pages(100)
                .authorId(authorDto.id())
                .language(BookLanguageDto.ENGLISH)
                .build();
        BookDto bookDto = bookifyCrudFacade.addBookWithAuthor(book);

        SeriesRequestDto series = SeriesRequestDto.builder()
                .bookId(bookDto.id())
                .name("Series of The Bests")
                .build();

        assertThat(bookifyCrudFacade.findAllSeries()).isEmpty();

        // when
        SeriesDto seriesDto = bookifyCrudFacade.addSeriesWithBook(series);

        // then
        assertThat(bookifyCrudFacade.findAllSeries()).isNotEmpty();
        SeriesWithAuthorsAndBooksDto seriesWithAuthorsAndBooks = bookifyCrudFacade
                .findSeriesByIdWithAuthorsAndBooks(seriesDto.id());
        Set<BookDto> books = seriesWithAuthorsAndBooks.books();
        assertTrue(books.stream().anyMatch(b -> b.id().equals(bookDto.id())));
    }

    @Test
    @DisplayName("Should throw SeriesNotDeletedException When deleting series with assigned books")
    void should_throw_series_not_deleted_exception_when_deleting_series_with_assigned_books() {
        // given
        AuthorRequestDto authorRequest = AuthorRequestDto.builder()
                .firstname("John")
                .lastname("Doe")
                .build();
        AuthorDto author = bookifyCrudFacade.addAuthor(authorRequest);

        BookRequestDto bookRequest = BookRequestDto.builder()
                .title("Programming Book")
                .publicationDate(LocalDate.now())
                .isbn("123456789123")
                .pages(100)
                .authorId(author.id())
                .language(BookLanguageDto.ENGLISH)
                .build();
        BookDto book = bookifyCrudFacade.addBookWithAuthor(bookRequest);

        SeriesRequestDto seriesRequest = new SeriesRequestDto(book.id(), "Programming Series");
        SeriesDto series = bookifyCrudFacade.addSeriesWithBook(seriesRequest);

        // when
        Throwable throwable = catchThrowable(() -> bookifyCrudFacade.deleteSeries(series.id()));

        // then
        assertThat(throwable).isInstanceOf(SeriesNotDeletedException.class)
                .hasMessageContaining(series.id().toString());
    }

    @Test
    @DisplayName("Should throw SeriesNoUpdatedException When updating series name to existing name")
    void should_throw_series_not_updated_exception_when_updating_series_name_to_existing_name() {
        // given
        AuthorDto authorDto = bookifyCrudFacade.addAuthor(new AuthorRequestDto("John", "Doe"));
        BookDto bookDto = bookifyCrudFacade.addBookWithAuthor(BookRequestDto.builder()
                .title("Programming Book")
                .publicationDate(LocalDate.now())
                .isbn("123456789123")
                .pages(100)
                .authorId(authorDto.id())
                .language(BookLanguageDto.ENGLISH)
                .build());
        SeriesDto seriesDto = bookifyCrudFacade.addSeriesWithBook(SeriesRequestDto.builder()
                .bookId(bookDto.id())
                .name("test1")
                .build());
        bookifyCrudFacade.addSeriesWithBook(SeriesRequestDto.builder()
                .bookId(bookDto.id())
                .name("test2")
                .build());

        // when
        Throwable throwable = catchThrowable(() -> bookifyCrudFacade.updateSeries(seriesDto.id(), "test2"));


        // then
        assertThat(throwable).isInstanceOf(SeriesNotUpdatedException.class)
                .hasMessageContaining(seriesDto.id().toString());
    }

    @Test
    @DisplayName("Should update series name")
    void should_update_series_name() {
        // given
        AuthorDto authorDto = bookifyCrudFacade.addAuthor(new AuthorRequestDto("John", "Doe"));
        BookDto bookDto = bookifyCrudFacade.addBookWithAuthor(BookRequestDto.builder()
                .title("Programming Book")
                .publicationDate(LocalDate.now())
                .isbn("123456789123")
                .pages(100)
                .authorId(authorDto.id())
                .language(BookLanguageDto.ENGLISH)
                .build());
        SeriesDto seriesDto = bookifyCrudFacade.addSeriesWithBook(SeriesRequestDto.builder()
                .bookId(bookDto.id())
                .name("test1")
                .build());

        // when
        SeriesDto result = bookifyCrudFacade.updateSeries(seriesDto.id(), "updated");

        // then
        assertThat(result.name()).isEqualTo("updated");
    }

    @Test
    @DisplayName("Should add books to series")
    void should_add_books_to_series() {
        // given
        AuthorDto authorDto = bookifyCrudFacade.addAuthor(new AuthorRequestDto("John", "Doe"));
        BookDto bookDto1 = bookifyCrudFacade.addBookWithAuthor(BookRequestDto.builder()
                .title("Programming Book")
                .publicationDate(LocalDate.now())
                .isbn("123456789123")
                .pages(100)
                .authorId(authorDto.id())
                .language(BookLanguageDto.ENGLISH)
                .build());
        BookDto bookDto2 = bookifyCrudFacade.addBookWithAuthor(BookRequestDto.builder()
                .title("Engineering Book")
                .publicationDate(LocalDate.now())
                .isbn("123987654321")
                .pages(100)
                .authorId(authorDto.id())
                .language(BookLanguageDto.ENGLISH)
                .build());
        BookDto bookDto3 = bookifyCrudFacade.addBookWithAuthor(BookRequestDto.builder()
                .title("Testing Book")
                .publicationDate(LocalDate.now())
                .isbn("123123123123")
                .pages(100)
                .authorId(authorDto.id())
                .language(BookLanguageDto.ENGLISH)
                .build());
        SeriesDto seriesDto = bookifyCrudFacade.addSeriesWithBook(SeriesRequestDto.builder()
                .bookId(bookDto1.id())
                .name("test")
                .build());

        // when
        bookifyCrudFacade.addBookToSeries(bookDto2.id(), seriesDto.id());
        bookifyCrudFacade.addBookToSeries(bookDto3.id(), seriesDto.id());

        // then
        SeriesWithAuthorsAndBooksDto series = bookifyCrudFacade.findSeriesByIdWithAuthorsAndBooks(seriesDto.id());
        assertThat(series.series().id()).isEqualTo(seriesDto.id());
        assertThat(series.books().size()).isEqualTo(3);
        assertThat(series.books()).containsExactlyInAnyOrder(bookDto1, bookDto2, bookDto3);
    }

    @Test
    @DisplayName("Should add genre")
    void should_add_genre() {
        // given
        GenreRequestDto genre = new GenreRequestDto("test");

        // when
        GenreDto genreDto = bookifyCrudFacade.addGenre(genre);

        // then
        Set<GenreDto> allGenres = bookifyCrudFacade.findAllGenres();
        assertThat(allGenres).extracting(GenreDto::id).contains(genreDto.id());
    }

    @Test
    @DisplayName("Should add author to book")
    void should_add_author_to_book() {
        // given
        AuthorRequestDto authorRequest = AuthorRequestDto.builder()
                .firstname("Firstname1")
                .lastname("Lastname1")
                .build();
        AuthorDto author = bookifyCrudFacade.addAuthor(authorRequest);
        BookRequestDto bookRequest = BookRequestDto.builder()
                .title("Book")
                .publicationDate(LocalDate.now())
                .isbn("1234567890")
                .pages(100)
                .authorId(author.id())
                .language(BookLanguageDto.ENGLISH)
                .build();
        BookDto book = bookifyCrudFacade.addBookWithAuthor(bookRequest);
        AuthorRequestDto authorToAddRequest = AuthorRequestDto.builder()
                .firstname("Firstname2")
                .lastname("Lastname2")
                .build();
        AuthorDto authorToAdd = bookifyCrudFacade.addAuthor(authorToAddRequest);

        assertThat(bookifyCrudFacade.findBooksByAuthorId(authorToAdd.id())).isEmpty();

        // when
        bookifyCrudFacade.addAuthorToBook(authorToAdd.id(), book.id());

        // then
        Set<BookDto> booksByAuthorId = bookifyCrudFacade.findBooksByAuthorId(authorToAdd.id());
        assertThat(booksByAuthorId).extracting(BookDto::id).containsExactly(book.id());
    }

    @Test
    @DisplayName("Should return book by id")
    void should_return_book_by_id() {
        // given
        AuthorRequestDto authorRequest = AuthorRequestDto.builder()
                .firstname("Firstname1")
                .lastname("Lastname1")
                .build();
        AuthorDto author = bookifyCrudFacade.addAuthor(authorRequest);
        BookRequestDto bookRequest = BookRequestDto.builder()
                .title("Book")
                .publicationDate(LocalDate.now())
                .isbn("1234567890")
                .pages(100)
                .authorId(author.id())
                .language(BookLanguageDto.ENGLISH)
                .build();
        BookDto book = bookifyCrudFacade.addBookWithAuthor(bookRequest);
        Long bookId = book.id();

        // when
        BookDto bookById = bookifyCrudFacade.findBookById(bookId);

        // then
        assertThat(bookById).extracting(BookDto::id).isEqualTo(bookId);
    }

    @Test
    @DisplayName("Should throw BookNotFoundException When book not found by id")
    void should_throw_book_not_found_exception_when_book_not_found_by_id() {
        // given
        assertThat(bookifyCrudFacade.findAllBooks(Pageable.unpaged())).isEmpty();

        // when
        Throwable throwable = catchThrowable(() -> bookifyCrudFacade.findBookById(0L));

        // then
        assertThat(throwable).isInstanceOf(BookNotFoundException.class);
        assertThat(throwable).hasMessage("Could not find book with id=0");
    }

    @Test
    @DisplayName("Should throw GenreNotFoundException When genre not found by id")
    void should_throw_genre_not_found_exception_when_genre_not_found_by_id() {
        // given
        // There is always a default genre in the database.
        assertThat(bookifyCrudFacade.findAllGenres()).hasSize(1);

        // when
        Throwable throwable = catchThrowable(() -> bookifyCrudFacade
                .findGenreByIdWithBooks(0L));

        // then
        assertThat(throwable).isInstanceOf(GenreNotFoundException.class);
        assertThat(throwable).hasMessage("Could not find genre with id=0");
    }

    @Test
    @DisplayName("Should throw AuthorNotFoundException When author not found by id")
    void should_throw_author_not_found_exception_when_author_not_found_by_id() {
        // given
        assertThat(bookifyCrudFacade.findAllAuthors(Pageable.unpaged())).isEmpty();

        // when
        Throwable throwable = catchThrowable(() -> bookifyCrudFacade
                .findAuthorByIdWithBooks(0L));

        // then
        assertThat(throwable).isInstanceOf(AuthorNotFoundException.class);
        assertThat(throwable).hasMessage("Could not find author with id=0");
    }

    @Test
    @DisplayName("Should throw SeriesNotFoundException When series not found by id")
    void should_throw_series_not_found_exception_when_series_not_found_by_id() {
        // given
        assertThat(bookifyCrudFacade.findAllSeries()).isEmpty();

        // when
        Throwable throwable = catchThrowable(() -> bookifyCrudFacade
                .findSeriesByIdWithAuthorsAndBooks(0L));

        // then
        assertThat(throwable).isInstanceOf(SeriesNotFoundException.class);
        assertThat(throwable).hasMessage("Could not find series with id=0");
    }

}