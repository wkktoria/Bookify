package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import io.github.wkktoria.bookify.domain.crud.dto.UpdateAuthorRequestDto;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Transactional
@Log4j2
class AuthorUpdater {

    private final AuthorRetriever authorRetriever;

    AuthorDto updateAuthorById(final Long id, final UpdateAuthorRequestDto requestDto) {
        log.debug("Updating author with id={} with values: {}", id, requestDto);

        Author author = authorRetriever.findAuthorById(id);

        if (requestDto.firstname() != null && !requestDto.firstname().isBlank()) {
            log.debug("Setting author's firstname to: {}", requestDto.firstname());
            author.setFirstname(requestDto.firstname());
        }

        if (requestDto.lastname() != null && !requestDto.lastname().isBlank()) {
            log.debug("Setting author's lastname to: {}", requestDto.lastname());
            author.setLastname(requestDto.lastname());
        }

        log.debug("Author with id={} successfully updated", id);

        return new AuthorDto(author.getId(), author.getFirstname(), author.getLastname());
    }

}
