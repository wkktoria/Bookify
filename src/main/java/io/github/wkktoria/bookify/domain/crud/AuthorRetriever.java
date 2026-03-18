package io.github.wkktoria.bookify.domain.crud;

import io.github.wkktoria.bookify.domain.crud.dto.AuthorDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
class AuthorRetriever {

    private final AuthorRepository authorRepository;

    List<AuthorDto> findAllAuthors(final Pageable pageable) {
        log.debug("Retrieving authors");
        return authorRepository.findAll(pageable).stream()
                .map(author -> new AuthorDto(author.getId(), author.getFirstname(), author.getLastname()))
                .toList();
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
