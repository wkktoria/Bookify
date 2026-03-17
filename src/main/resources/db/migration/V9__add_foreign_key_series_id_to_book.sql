ALTER TABLE book
    ADD series_id BIGINT REFERENCES series (id);