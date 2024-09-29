CREATE TABLE url_short (
    id SERIAL PRIMARY KEY,
    original_url VARCHAR(255) NOT NULL,
    hashed_url VARCHAR(255) NOT NULL
);