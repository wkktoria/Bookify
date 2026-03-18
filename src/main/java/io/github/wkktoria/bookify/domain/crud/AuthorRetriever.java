package io.github.wkktoria.bookify.domain.crud;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class AuthorRetriever {

    private final AuthorRepository authorRepository;

    Author findAuthorById(final Long id) {
        log.debug("Fetching author with id={}", id);

        return authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Author with id={} not found", id);
                    return new AuthorNotFoundException("Could not find author with id=" + id);
                });
    }

}
