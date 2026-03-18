package io.github.wkktoria.bookify.domain.crud;

import org.springframework.data.repository.Repository;

interface AuthorRepository extends Repository<Author, Long> {

    Author save(final Author author);

}
