package io.github.wkktoria.bookify.book.infrastructure.controller;

import io.github.wkktoria.bookify.book.domain.model.BookLanguage;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.BookDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.CreateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.PartiallyUpdateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.UpdateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.response.*;
import io.github.wkktoria.bookify.book.domain.model.Book;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

public class BookMapper {

    public static BookDto mapFromBookToBookDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor());
    }

    public static GetAllBooksResponseDto mapFromListToGetAllBooksResponseDto(List<Book> books) {
        List<BookDto> bookDtos = books.stream()
                .map(BookMapper::mapFromBookToBookDto)
                .toList();
        return new GetAllBooksResponseDto(bookDtos);
    }

    public static GetBookResponseDto mapFromBookToGetBookResponseDto(Book book) {
        BookDto bookDto = mapFromBookToBookDto(book);
        return new GetBookResponseDto(bookDto);
    }

    public static Book mapFromCreateBookRequestDtoToBook(CreateBookRequestDto dto) {
        return Book.builder()
                .title(dto.bookTitle())
                .author(dto.author())
                .publicationDate(dto.publicationDate()
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC))
                .isbn(dto.isbn())
                .pages(dto.pages())
                .language(BookLanguage.valueOf(dto.language().toUpperCase()))
                .build();
    }

    public static CreateBookResponseDto mapFromBookToCreateBookResponseDto(Book book) {
        BookDto bookDto = mapFromBookToBookDto(book);
        return new CreateBookResponseDto(bookDto);
    }

    public static DeleteBookResponseDto mapFromIdToDeleteBookResponseDto(Long id) {
        return new DeleteBookResponseDto("Deleted book with id " + id, HttpStatus.OK);
    }

    public static Book mapFromPartiallyUpdateBookRequestDtoToBook(PartiallyUpdateBookRequestDto dto) {
        return new Book(dto.bookTitle(), dto.author());
    }

    public static PartiallyUpdateBookResponseDto mapFromBookToPartiallyUpdateBookResponseDto(Book book) {
        BookDto bookDto = mapFromBookToBookDto(book);
        return new PartiallyUpdateBookResponseDto(bookDto);
    }

    public static Book mapFromUpdateBookRequestDtoToBook(UpdateBookRequestDto dto) {
        return new Book(dto.bookTitle(), dto.author());
    }

    public static UpdateBookResponseDto mapFromBookToUpdateBookResponseDto(Book book) {
        BookDto bookDto = mapFromBookToBookDto(book);
        return new UpdateBookResponseDto(bookDto);
    }

}
