package by.aurorasoft.distanceclassifier.crud.mapper;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.crud.model.dto.ScannedLocation;
import by.aurorasoft.distanceclassifier.crud.model.entity.ScannedLocationEntity;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;

import static by.aurorasoft.distanceclassifier.testutil.ScannedLocationEntityUtil.checkEquals;
import static org.junit.Assert.assertEquals;

public final class ScannedLocationMapperTest extends AbstractSpringBootTest {

    @Autowired
    private ScannedLocationMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void entityShouldBeMappedToDto() {
        final Long givenId = 255L;
        final Geometry givenGeometry = createPolygon("POLYGON((1 1, 1 15, 12 15, 12 1, 1 1))");
        final ScannedLocationEntity givenEntity = new ScannedLocationEntity(givenId, givenGeometry);

        final ScannedLocation actual = mapper.toDto(givenEntity);
        final ScannedLocation expected = new ScannedLocation(givenId, givenGeometry);
        assertEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Long givenId = 255L;
        final Geometry givenGeometry = createPolygon("POLYGON((2 2, 2 15, 12 15, 12 2, 2 2))");
        final ScannedLocation givenDto = new ScannedLocation(givenId, givenGeometry);

        final ScannedLocationEntity actual = mapper.toEntity(givenDto);
        final ScannedLocationEntity expected = new ScannedLocationEntity(givenId, givenGeometry);
        checkEquals(expected, actual);
    }

    private Polygon createPolygon(final String text) {
        return GeometryUtil.createPolygon(text, geometryFactory);
    }
}
