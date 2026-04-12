INSERT INTO users(email, password, authorities, enabled)
VALUES ('user@bookify.com', '$2a$12$7jJaPUbrrW0uY7mKqhdk2.X9DqAVKO/doq18r1SLzJAJX2a1kU/YC',
        '{ROLE_USER}', true),
       ('admin@bookify.com', '$2a$12$Q7q4PFI60GwFzP9UALN0LejAAatXNMC68EEGSyqynuxGaRgUmV1wO',
        '{ROLE_ADMIN, ROLE_USER}', true);