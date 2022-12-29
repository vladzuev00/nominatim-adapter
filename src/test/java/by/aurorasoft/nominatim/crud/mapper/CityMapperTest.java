package by.aurorasoft.nominatim.crud.mapper;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static by.aurorasoft.nominatim.crud.model.entity.CityEntity.Type.CAPITAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class CityMapperTest extends AbstractContextTest {

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Coordinate[] givenGeometryCoordinates = createGeometryCoordinates();
        final City givenDto = City.builder()
                .id(255L)
                .name("city")
                .geometry(this.geometryFactory.createPolygon(givenGeometryCoordinates))
                .type(CAPITAL)
                .build();

        final CityEntity actual = this.cityMapper.toEntity(givenDto);
        final CityEntity expected = CityEntity.builder()
                .id(255L)
                .name("city")
                .geometry(this.geometryFactory.createPolygon(givenGeometryCoordinates))
                .type(CAPITAL)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final Coordinate[] givenGeometryCoordinates = createGeometryCoordinates();
        final CityEntity givenEntity = CityEntity.builder()
                .id(255L)
                .name("city")
                .geometry(this.geometryFactory.createPolygon(givenGeometryCoordinates))
                .type(CAPITAL)
                .build();

        final City actual = this.cityMapper.toDto(givenEntity);
        final City expected = City.builder()
                .id(255L)
                .name("city")
                .geometry(this.geometryFactory.createPolygon(givenGeometryCoordinates))
                .type(CAPITAL)
                .build();
        assertEquals(expected, actual);
    }

    private static Coordinate[] createGeometryCoordinates() {
        return new Coordinate[]{
                new Coordinate(1, 2),
                new Coordinate(2, 3),
                new Coordinate(3, 4),
                new Coordinate(4, 5),
                new Coordinate(5, 6),
                new Coordinate(6, 7),
                new Coordinate(1, 2)
        };
    }

    private static void checkEquals(CityEntity expected, CityEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getGeometry(), actual.getGeometry());
        assertSame(expected.getType(), actual.getType());
    }
}
