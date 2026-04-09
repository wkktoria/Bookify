INSERT INTO users(email, password, authorities, enabled)
VALUES ('user@example.com', '$2a$10$j0GARHDw68BU9ercXrP2NueYShKkHqU/VpRW2S9KAJh3Vi5W7Suni', '{ROLE_USER}', true),
       ('wkktoria2002@gmail.com', '$2a$10$VEFh0QiiotS8PZzRMdj6VeZ/kJU5hf4r1SUue9JZRcR/Z35Sk9.rm',
        '{ROLE_ADMIN, ROLE_USER}', true);