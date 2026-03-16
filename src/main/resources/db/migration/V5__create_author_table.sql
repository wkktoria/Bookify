CREATE TABLE author
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    firstname  VARCHAR(15)           NOT NULL,
    lastname   VARCHAR(30)           NOT NULL,
    uuid       UUID,
    created_on TIMESTAMP WITH TIME ZONE
);