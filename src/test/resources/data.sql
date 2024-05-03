INSERT INTO city(id, name, type, geometry, bounding_box) VALUES(255, 'First', 'CAPITAL', ST_GeomFromText('POLYGON((2 3, 5 2.5, 5 6, 3 5, 2 3))', 4326), ST_GeomFromText('POLYGON((2 2.5, 5 2.5, 5 6, 2 6, 2 2.5))', 4326));
INSERT INTO city(id, name, type, geometry, bounding_box) VALUES(256, 'Second', 'CITY', ST_GeomFromText('POLYGON((7.5 4, 8 4, 10.5 5, 11.5 7.5, 8.5 6.5, 7.5 4))', 4326), ST_GeomFromText('POLYGON((7.5 4, 11.5 4, 11.5 7.5, 7.5 7.5, 7.5 4))', 4326));
INSERT INTO city(id, name, type, geometry, bounding_box) VALUES(257, 'THIRD', 'TOWN', ST_GeomFromText('POLYGON((3 8, 6 8, 6 11, 3 11))', 4326), ST_GeomFromText('POLYGON((3 8, 6 8, 6 11, 3 11))', 4326));
