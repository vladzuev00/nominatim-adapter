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

CREATE TABLE scanned_location
(
    id INTEGER PRIMARY KEY,
    geometry GEOMETRY NOT NULL
);

INSERT INTO scanned_location(id, geometry) VALUES(1, ST_GeomFromText('POLYGON EMPTY', 4326));

CREATE FUNCTION deny_insert_and_delete_scanned_location()
  RETURNS TRIGGER
AS
$body$
BEGIN
  RAISE EXCEPTION 'Impossible to insert or delete scanned location';
END;
$body$
LANGUAGE plpgsql;

CREATE TRIGGER trigger_on_insert_and_delete_scanned_location
BEFORE INSERT OR DELETE ON scanned_location
FOR EACH STATEMENT EXECUTE PROCEDURE deny_insert_and_delete_scanned_location();
