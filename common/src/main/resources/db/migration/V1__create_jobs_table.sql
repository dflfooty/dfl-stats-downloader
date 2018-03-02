create table job(
    id SERIAL PRIMARY KEY NOT NULL,
    year INTEGER,
    round INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    status VARCHAR,
    file TEXT
);