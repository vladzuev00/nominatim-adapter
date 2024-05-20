package by.aurorasoft.distanceclassifier.crud.service;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.crud.model.dto.ScannedLocation;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class ScannedLocationServiceTest extends AbstractSpringBootTest {

    @Autowired
    private ScannedLocationService service;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void geometryShouldBeAppended() {
        final Geometry givenGeometry = createPolygon("POLYGON((13 16, 13 17, 15 17, 15 13, 13 16))");

        service.append(givenGeometry);

        final Long expectedId = 1L;
        final ScannedLocation actual = service.getById(expectedId);
        final ScannedLocation expected = new ScannedLocation(
                expectedId,
                createMultiPolygon("MULTIPOLYGON (((1 1, 1 15, 12 15, 12 1, 1 1)), ((13 16, 13 17, 15 17, 15 13, 13 16)))")
        );
        assertEquals(expected, actual);
    }

    @Test
    public void locationShouldBeGot() {
        final ScannedLocation actual = service.get();

        final ScannedLocation expected = new ScannedLocation(
                1L,
                createPolygon("POLYGON((1 1, 1 15, 12 15, 12 1, 1 1))")
        );
        assertEquals(expected, actual);
    }

    private Polygon createPolygon(final String text) {
        return GeometryUtil.createPolygon(text, geometryFactory);
    }

    @SuppressWarnings("SameParameterValue")
    private MultiPolygon createMultiPolygon(final String text) {
        return GeometryUtil.createMultiPolygon(text, geometryFactory);
    }
}
