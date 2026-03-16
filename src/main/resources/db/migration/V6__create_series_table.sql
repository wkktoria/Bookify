CREATE TABLE series
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    name       VARCHAR(50)           NOT NULL UNIQUE,
    uuid       UUID,
    created_on TIMESTAMP WITH TIME ZONE
);