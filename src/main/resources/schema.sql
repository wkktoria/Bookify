CREATE TABLE book
(
    id               BIGINT                      NOT NULL,
    title            VARCHAR(255)                NOT NULL,
    author           VARCHAR(100)                NOT NULL,
    publication_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    isbn             VARCHAR(13)                 NOT NULL,
    pages            INTEGER                     NOT NULL,
    language         VARCHAR(255),
    CONSTRAINT pk_book PRIMARY KEY (id)
);