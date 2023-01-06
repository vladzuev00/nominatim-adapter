package by.aurorasoft.nominatim.crud.service;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.crud.model.dto.City;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public final class CityServiceTest extends AbstractContextTest {

    @Autowired
    private CityService service;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), 'CAPITAL')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(256, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), 'CAPITAL')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(257, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), 'CAPITAL')")
    public void allCitiesShouldBeFound() {
        final List<City> foundCities = this.service.findAll();
        final List<Long> actualIds = foundCities.stream()
                .map(City::getId)
                .collect(toList());
        final List<Long> expectedIds = List.of(255L, 256L, 257L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void allCitiesShouldNotBeFound() {
        final List<City> foundCities = this.service.findAll();
        assertTrue(foundCities.isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), 'CAPITAL')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(256, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 3, 3 4, 5 6, 6 7, 1 3))', 4326), 'CAPITAL')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(257, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 4, 3 4, 5 6, 6 7, 1 4))', 4326), 'CAPITAL')")
    public void cityWithGivenGeometryShouldExist() {
        final Coordinate[] givenCoordinates = new Coordinate[]{
                new CoordinateXY(1, 2),
                new CoordinateXY(3, 4),
                new CoordinateXY(5, 6),
                new CoordinateXY(6, 7),
                new CoordinateXY(1, 2)
        };
        final Geometry givenGeometry = this.geometryFactory.createPolygon(givenCoordinates);
        assertTrue(this.service.isExistByGeometry(givenGeometry));
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 5, 3 4, 5 6, 6 7, 1 5))', 4326), 'CAPITAL')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(256, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 3, 3 4, 5 6, 6 7, 1 3))', 4326), 'CAPITAL')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(257, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 4, 3 4, 5 6, 6 7, 1 4))', 4326), 'CAPITAL')")
    public void cityWithGivenGeometryShouldNotExist() {
        final Coordinate[] givenCoordinates = new Coordinate[]{
                new CoordinateXY(1, 2),
                new CoordinateXY(3, 4),
                new CoordinateXY(5, 6),
                new CoordinateXY(6, 7),
                new CoordinateXY(1, 2)
        };
        final Geometry givenGeometry = this.geometryFactory.createPolygon(givenCoordinates);
        assertFalse(this.service.isExistByGeometry(givenGeometry));
    }
}
