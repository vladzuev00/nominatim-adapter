CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TYPE city_type AS ENUM ('CAPITAL', 'CITY', 'TOWN');

CREATE TABLE city
(
    id           SERIAL PRIMARY KEY,
    type         city_type    NOT NULL,
    name         VARCHAR(256) NOT NULL,
    geometry     GEOMETRY     NOT NULL,
    bounding_box GEOMETRY     NOT NULL
);

ALTER SEQUENCE city_id_seq INCREMENT 50;

CREATE INDEX city_bounding_box_index ON city USING GIST(bounding_box);

