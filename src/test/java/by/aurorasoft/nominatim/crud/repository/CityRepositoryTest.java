package by.aurorasoft.nominatim.crud.repository;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.aurorasoft.nominatim.crud.model.entity.CityEntity.Type.CAPITAL;
import static org.junit.Assert.*;

public final class CityRepositoryTest extends AbstractContextTest {

    @Autowired
    private CityRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), 'CAPITAL')")
    public void cityShouldBeFoundById() {
        super.startQueryCount();
        final CityEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final Coordinate[] expectedCoordinates = new Coordinate[]{
                new CoordinateXY(1, 2),
                new CoordinateXY(3, 4),
                new CoordinateXY(5, 6),
                new CoordinateXY(6, 7),
                new CoordinateXY(1, 2)
        };
        final CityEntity expected = CityEntity.builder()
                .id(255L)
                .name("Minsk")
                .geometry(this.geometryFactory.createPolygon(expectedCoordinates))
                .type(CAPITAL)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void cityShouldBeSaved() {
        final Coordinate[] givenCoordinates = new Coordinate[]{
                new CoordinateXY(2, 1),
                new CoordinateXY(4, 3),
                new CoordinateXY(6, 5),
                new CoordinateXY(7, 6),
                new CoordinateXY(2, 1)
        };
        final CityEntity givenCity = CityEntity.builder()
                .name("Minsk")
                .geometry(this.geometryFactory.createPolygon(givenCoordinates))
                .type(CAPITAL)
                .build();

        super.startQueryCount();
        this.repository.save(givenCity);
        super.checkQueryCount(1);
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

        super.startQueryCount();
        final boolean exists = this.repository.isExistByGeometry(givenGeometry);
        super.checkQueryCount(1);
        assertTrue(exists);
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

        super.startQueryCount();
        final boolean exists = this.repository.isExistByGeometry(givenGeometry);
        super.checkQueryCount(1);
        assertFalse(exists);
    }

    private static void checkEquals(CityEntity expected, CityEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getGeometry(), actual.getGeometry());
        assertSame(expected.getType(), actual.getType());
    }
}
