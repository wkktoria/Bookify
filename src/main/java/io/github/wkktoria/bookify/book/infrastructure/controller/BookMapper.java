package io.github.wkktoria.bookify.book.infrastructure.controller;

import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.CreateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.request.UpdateBookRequestDto;
import io.github.wkktoria.bookify.book.infrastructure.controller.dto.response.*;
import io.github.wkktoria.bookify.book.domain.model.Book;
import org.springframework.http.HttpStatus;

import java.util.List;

public class BookMapper {

    public static GetAllBooksResponseDto mapFromMapToGetAllBooksResponseDto(List<Book> books) {
        return new GetAllBooksResponseDto(books);
    }

    public static GetBookResponseDto mapFromBookToGetBookResponseDto(Book book) {
        return new GetBookResponseDto(book);
    }

    public static Book mapFromCreateBookRequestDtoToBook(CreateBookRequestDto dto) {
        return new Book(dto.bookTitle(), dto.author());
    }

    public static CreateBookResponseDto mapFromBookToCreateBookResponseDto(Book book) {
        return new CreateBookResponseDto(book);
    }

    public static DeleteBookResponseDto mapFromIdToDeleteBookResponseDto(Integer id) {
        return new DeleteBookResponseDto("Deleted book with id " + id, HttpStatus.OK);
    }

    public static PartiallyUpdateBookResponseDto mapFromBookToPartiallyUpdateBookResponseDto(Book book) {
        return new PartiallyUpdateBookResponseDto(book);
    }

    public static Book mapFromUpdateBookRequestDtoToBook(UpdateBookRequestDto dto) {
        return new Book(dto.bookTitle(), dto.author());
    }

    public static UpdateBookResponseDto mapFromBookToUpdateBookResponseDto(Book book) {
        return new UpdateBookResponseDto(book);
    }

}
