INSERT INTO users(email, password, authorities, enabled)
VALUES ('user', 'user', '{ROLE_USER}', true),
       ('admin', 'admin', '{ROLE_ADMIN, ROLE_USER}', true);