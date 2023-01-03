package by.aurorasoft.nominatim.crud.repository;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.aurorasoft.nominatim.crud.model.entity.CityEntity.Type.CAPITAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class CityRepositoryTest extends AbstractContextTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 'CAPITAL')")
    public void cityShouldBeFoundById() {
        super.startQueryCount();
        final CityEntity actual = this.cityRepository.findById(255L).orElseThrow();
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
        this.cityRepository.save(givenCity);
        super.checkQueryCount(1);
    }

    private static void checkEquals(CityEntity expected, CityEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getGeometry(), actual.getGeometry());
        assertSame(expected.getType(), actual.getType());
    }
}
