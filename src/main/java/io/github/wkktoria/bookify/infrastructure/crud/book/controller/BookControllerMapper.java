package io.github.wkktoria.bookify.infrastructure.crud.book.controller;

import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.request.CreateBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.request.PartiallyUpdateBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.request.UpdateBookRequestDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.CreateBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.DeleteBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.GetAllBooksResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.GetBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.PartiallyUpdateBookResponseDto;
import io.github.wkktoria.bookify.infrastructure.crud.book.controller.dto.response.UpdateBookResponseDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public class BookControllerMapper {

    public static GetAllBooksResponseDto mapFromBookDtoListToGetAllResponseDto(final List<BookDto> bookDtoList) {
        return new GetAllBooksResponseDto(bookDtoList);
    }

    public static GetBookResponseDto mapFromBookDtoGetGetBookResponseDto(final BookDto bookDto) {
        return new GetBookResponseDto(bookDto);
    }


    public static BookDto mapFromCreateBookRequestDtoToBookDto(final CreateBookRequestDto requestDto) {
        return BookDto.builder()
                .title(requestDto.bookTitle())
                .author(requestDto.author())
                .publicationDate(requestDto.publicationDate())
                .isbn(requestDto.isbn())
                .pages(requestDto.pages())
                .build();
    }

    public static CreateBookResponseDto mapFromBookDtoToCreateBookResponseDto(final BookDto bookDto) {
        return new CreateBookResponseDto(bookDto);
    }

    public static DeleteBookResponseDto mapFromLongIdToDeleteBookResponseDto(final Long id) {
        return new DeleteBookResponseDto("Deleted book with id " + id, HttpStatus.OK);
    }

    public static BookDto mapFromUpdateBookRequestDtoToBookDto(final UpdateBookRequestDto requestDto) {
        return BookDto.builder()
                .title(requestDto.bookTitle())
                .author(requestDto.author())
                .publicationDate(requestDto.publicationDate())
                .isbn(requestDto.isbn())
                .pages(requestDto.pages())
                .build();
    }

    public static UpdateBookResponseDto mapFromBookDtoToUpdateBookResponseDto(final BookDto bookDto) {
        return new UpdateBookResponseDto(bookDto);
    }

    public static BookDto mapFromPartiallyUpdateBookRequestDtoToBookDto(final PartiallyUpdateBookRequestDto requestDto) {
        return BookDto.builder()
                .title(requestDto.bookTitle())
                .author(requestDto.author())
                .build();
    }

    public static PartiallyUpdateBookResponseDto mapFromBookDtoToPartiallyUpdateBookResponseDto(final BookDto bookDto) {
        return new PartiallyUpdateBookResponseDto(bookDto);
    }

}
