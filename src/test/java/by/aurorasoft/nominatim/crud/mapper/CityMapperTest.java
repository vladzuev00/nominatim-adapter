package by.aurorasoft.nominatim.crud.mapper;

import by.aurorasoft.nominatim.base.AbstractSpringBootTest;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import by.aurorasoft.nominatim.model.CityType;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static by.aurorasoft.nominatim.model.CityType.CAPITAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class CityMapperTest extends AbstractSpringBootTest {
    private static final Coordinate[] GIVEN_GEOMETRY_COORDINATES = new Coordinate[]{
            new Coordinate(1, 1),
            new Coordinate(2, 1),
            new Coordinate(2, 2),
            new Coordinate(1, 1)
    };
    private static final Coordinate[] GIVEN_BOUNDING_BOX_COORDINATES = new Coordinate[]{
            new Coordinate(1, 1),
            new Coordinate(2, 1),
            new Coordinate(2, 2),
            new CoordinateXY(1, 2),
            new Coordinate(1, 1)
    };

    @Autowired
    private CityMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Long givenId = 255L;
        final String givenName = "city";
        final Geometry givenGeometry = createGivenGeometry();
        final CityType givenType = CAPITAL;
        final Geometry givenBoundingBox = createGivenBoundingBox();
        final City givenDto = City.builder()
                .id(givenId)
                .name(givenName)
                .geometry(givenGeometry)
                .type(givenType)
                .boundingBox(givenBoundingBox)
                .build();

        final CityEntity actual = mapper.toEntity(givenDto);
        final CityEntity expected = CityEntity.builder()
                .id(givenId)
                .name(givenName)
                .geometry(givenGeometry)
                .type(givenType)
                .boundingBox(givenBoundingBox)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final Long givenId = 255L;
        final String givenName = "city";
        final Geometry givenGeometry = createGivenGeometry();
        final CityType givenType = CAPITAL;
        final Geometry givenBoundingBox = createGivenBoundingBox();
        final CityEntity givenEntity = CityEntity.builder()
                .id(givenId)
                .name(givenName)
                .geometry(givenGeometry)
                .type(givenType)
                .boundingBox(givenBoundingBox)
                .build();

        final City actual = mapper.toDto(givenEntity);
        final City expected = City.builder()
                .id(givenId)
                .name(givenName)
                .geometry(givenGeometry)
                .type(givenType)
                .boundingBox(givenBoundingBox)
                .build();
        assertEquals(expected, actual);
    }

    private Geometry createGivenGeometry() {
        return createGeometry(GIVEN_GEOMETRY_COORDINATES);
    }

    private Geometry createGivenBoundingBox() {
        return createGeometry(GIVEN_BOUNDING_BOX_COORDINATES);
    }

    private Geometry createGeometry(final Coordinate[] coordinates) {
        return geometryFactory.createPolygon(coordinates);
    }

    private static void checkEquals(final CityEntity expected, final CityEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getGeometry(), actual.getGeometry());
        assertSame(expected.getType(), actual.getType());
        assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
    }
}
