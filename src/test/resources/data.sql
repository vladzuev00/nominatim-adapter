INSERT INTO city(id, name, type, geometry, bounding_box) VALUES(255, 'First', 'CAPITAL', ST_GeomFromText('POLYGON((3 2, 2.5 5, 6 5, 5 3, 3 2))', 4326), ST_GeomFromText('POLYGON((2.5 2, 2.5 5, 6 5, 6 2, 2.5 2))', 4326));
INSERT INTO city(id, name, type, geometry, bounding_box) VALUES(256, 'Second', 'CITY', ST_GeomFromText('POLYGON((4 7.5, 4 8, 5 10.5, 7.5 11.5, 6.5 8.5, 4 7.5))', 4326), ST_GeomFromText('POLYGON((4 7.5, 4 11.5, 7.5 11.5, 7.5 7.5, 4 7.5))', 4326));
INSERT INTO city(id, name, type, geometry, bounding_box) VALUES(257, 'Third', 'TOWN', ST_GeomFromText('POLYGON((8 3, 8 6, 11 6, 11 3, 8 3))', 4326), ST_GeomFromText('POLYGON((8 3, 8 6, 11 6, 11 3, 8 3))', 4326));
INSERT INTO city(id, name, type, geometry, bounding_box) VALUES(258, 'Fourth', 'TOWN', ST_GeomFromText('POLYGON((11 9.5, 11 12, 13 12, 13 9.5, 11 9.5))', 4326), ST_GeomFromText('POLYGON((11 9.5, 11 12, 13 12, 13 9.5, 11 9.5))', 4326));

UPDATE scanned_location SET geometry = ST_Union(geometry, ST_GeomFromText('POLYGON((1 1, 1 15, 12 15, 12 1, 1 1))', 4326));
