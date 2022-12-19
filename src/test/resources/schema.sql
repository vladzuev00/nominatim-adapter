CREATE EXTENSION postgis;

CREATE TABLE city(
                     id SERIAL PRIMARY KEY,
                     name VARCHAR(256) NOT NULL,
                     geometry GEOMETRY NOT NULL
);
