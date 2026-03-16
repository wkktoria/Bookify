CREATE TABLE genre
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    name       VARCHAR(25)           NOT NULL UNIQUE,
    uuid       UUID,
    created_on TIMESTAMP WITH TIME ZONE
);