package by.aurorasoft.distanceclassifier.crud.mapper;

import by.aurorasoft.distanceclassifier.base.AbstractJunitSpringBootTest;
import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity;
import by.aurorasoft.distanceclassifier.model.CityType;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;

import static by.aurorasoft.distanceclassifier.model.CityType.CAPITAL;
import static by.aurorasoft.distanceclassifier.testutil.CityEntityUtil.checkEquals;
import static org.junit.Assert.assertEquals;

public final class CityMapperTest extends AbstractJunitSpringBootTest {

    @Autowired
    private CityMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Long givenId = 255L;
        final String givenName = "city";
        final Geometry givenGeometry = createPolygon("POLYGON((1 1, 2 1, 2 2, 1 1))");
        final CityType givenType = CAPITAL;
        final Geometry givenBoundingBox = createPolygon("POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))");
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
        final Geometry givenGeometry = createPolygon("POLYGON((1 1, 2 1, 2 2, 1 1))");
        final CityType givenType = CAPITAL;
        final Geometry givenBoundingBox = createPolygon("POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))");
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

    private Polygon createPolygon(final String text) {
        return GeometryUtil.createPolygon(text, geometryFactory);
    }
}
