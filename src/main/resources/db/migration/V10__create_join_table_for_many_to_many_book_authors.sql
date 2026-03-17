CREATE TABLE book_authors
(
    authors_id BIGINT NOT NULL,
    books_id   BIGINT NOT NULL,
    CONSTRAINT pk_book_authors PRIMARY KEY (authors_id, books_id)
);

ALTER TABLE book_authors
    ADD CONSTRAINT fk_booaut_on_author FOREIGN KEY (authors_id) REFERENCES author (id);

ALTER TABLE book_authors
    ADD CONSTRAINT fk_booaut_on_book FOREIGN KEY (books_id) REFERENCES book (id);