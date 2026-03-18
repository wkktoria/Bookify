package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class AuthorRetriever {

    private final AuthorRepository authorRepository;

    Set<AuthorDto> findAllAuthors() {
        log.debug("Retrieving authors");

        Set<Author> authors = authorRepository.findAll();

        log.debug("Retrieved {} authors", authors.size());

        return authors.stream()
                .map(author -> new AuthorDto(author.getId(), author.getFirstname(), author.getLastname()))
                .collect(Collectors.toSet());
    }

    Author findAuthorById(final Long id) {
        log.debug("Retrieving author with id={}", id);

        return authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Author with id={} not found", id);
                    return new AuthorNotFoundException("Could not find author with id=" + id);
                });
    }

}
