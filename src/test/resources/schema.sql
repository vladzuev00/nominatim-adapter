DROP TABLE IF EXISTS city;
DROP TYPE IF EXISTS city_type;

DROP TABLE IF EXISTS searching_cities_process;
DROP TYPE IF EXISTS searching_cities_process_type;

DROP FUNCTION IF EXISTS on_update_searching_cities_process();

CREATE TYPE city_type AS ENUM ('CAPITAL', 'REGIONAL', 'NOT_DEFINED');

CREATE TABLE city
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(256) NOT NULL,
    geometry GEOMETRY     NOT NULL,
    type     city_type    NOT NULL,
    bounding_box GEOMETRY NOT NULL
);

CREATE TYPE searching_cities_process_type AS ENUM('HANDLING', 'SUCCESS', 'ERROR');

CREATE TABLE searching_cities_process
(
    id             BIGSERIAL PRIMARY KEY,
    bounds         GEOMETRY                      NOT NULL,
    search_step    DECIMAL                       NOT NULL,
    total_points   BIGINT                        NOT NULL,
    handled_points BIGINT                        NOT NULL,
    status         searching_cities_process_type NOT NULL,
    created_time   TIMESTAMP                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time   TIMESTAMP                     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION on_update_searching_cities_process() RETURNS TRIGGER AS
'
    BEGIN
        NEW.updated_time = CURRENT_TIMESTAMP;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER tr_on_update_searching_cities_process
    BEFORE UPDATE
    ON searching_cities_process
    FOR EACH ROW
    EXECUTE PROCEDURE on_update_searching_cities_process();

CREATE INDEX ON city using GIST(geometry);
CREATE INDEX ON city using GIST(bounding_box);





