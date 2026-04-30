package io.github.wkktoria.bookify.domain.bookbuyer;

import io.github.wkktoria.bookify.domain.crud.BookifyCrudFacade;
import io.github.wkktoria.bookify.domain.crud.dto.BookDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookBuyerFacade {

    private final BookifyCrudFacade bookifyCrudFacade;
    private final AmazonHttpClient amazonHttpClient;

    public String buyBookWithId(final Long id) {
        BookDto bookDtoById = bookifyCrudFacade.findBookById(id);
        String title = bookDtoById.title();
        String result = amazonHttpClient.buyBookByTitle(title);

        if (result.equals("success")) {
            return result;
        }

        throw new RuntimeException("An error occurred - could not buy book");
    }

}
