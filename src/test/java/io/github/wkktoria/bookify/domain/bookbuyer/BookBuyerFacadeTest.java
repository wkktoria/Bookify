package io.github.wkktoria.bookify.domain.bookbuyer;

import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookBuyerFacadeTest {

    BookifyCrudFacade bookifyCrudFacade = mock(BookifyCrudFacade.class);
    AmazonHttpClient amazonHttpClient = mock(AmazonHttpClient.class);

    BookBuyerFacade bookBuyerFacade = new BookBuyerFacade(
            bookifyCrudFacade, amazonHttpClient
    );

    @Test
    @DisplayName("Should return success When bought book")
    void should_return_success_when_bought_book() {
        // given
        when(bookifyCrudFacade.findBookById(any()))
                .thenReturn(BookDto.builder()
                        .id(1L)
                        .title("Test Book")
                        .build());
        when(amazonHttpClient.buyBookByTitle(any())).thenReturn("success");

        // when
        String result = bookBuyerFacade.buyBookWithId(1L);

        // then
        assertThat(result).isEqualTo("success");
    }

    @Test
    @DisplayName("Should throw exception")
    void should_throw_exception() {
        // given
        when(bookifyCrudFacade.findBookById(any()))
                .thenReturn(BookDto.builder()
                        .id(1L)
                        .title("Test Book")
                        .build());
        when(amazonHttpClient.buyBookByTitle(any()))
                .thenReturn("error");

        // when
        Throwable throwable = catchThrowable(() -> bookBuyerFacade.buyBookWithId(1L));

        // then
        assertThat(throwable).isInstanceOf(RuntimeException.class);
        assertThat(throwable).hasMessage("An error occurred - could not buy book");
    }

}