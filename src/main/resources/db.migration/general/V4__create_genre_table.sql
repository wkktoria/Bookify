CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE genre
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    name       VARCHAR(25)           NOT NULL UNIQUE,
    uuid       UUID,
    created_on TIMESTAMP WITH TIME ZONE
);

INSERT INTO genre(name, uuid, created_on)
VALUES ('default', uuid_generate_v4(), now());