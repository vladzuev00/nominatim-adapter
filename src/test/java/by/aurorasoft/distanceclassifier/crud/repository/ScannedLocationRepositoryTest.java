package by.aurorasoft.distanceclassifier.crud.repository;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.crud.model.entity.ScannedLocationEntity;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static by.aurorasoft.distanceclassifier.testutil.ScannedLocationEntityUtil.checkEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class ScannedLocationRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private ScannedLocationRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void locationShouldBeFoundById() {
        final Long givenId = 1L;

        startQueryCount();
        final Optional<ScannedLocationEntity> optionalActual = repository.findById(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final ScannedLocationEntity actual = optionalActual.get();
        final ScannedLocationEntity expected = ScannedLocationEntity.builder()
                .id(givenId)
                .geometry(createPolygon("POLYGON((1 1, 1 15, 12 15, 12 1, 1 1))"))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void geometryShouldBeAppended() {
        final Geometry givenGeometry = createPolygon("POLYGON((13 16, 13 17, 15 17, 15 13, 13 16))");

        startQueryCount();
        final int actualCountUpdatedRows = repository.append(givenGeometry);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final Long expectedId = 1L;
        final Optional<ScannedLocationEntity> optionalActual = repository.findById(expectedId);
        assertTrue(optionalActual.isPresent());

        final ScannedLocationEntity actual = optionalActual.get();
        final ScannedLocationEntity expected = ScannedLocationEntity.builder()
                .id(expectedId)
                .geometry(createMultiPolygon("MULTIPOLYGON (((1 1, 1 15, 12 15, 12 1, 1 1)), ((13 16, 13 17, 15 17, 15 13, 13 16)))"))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void locationShouldBeGot() {
        startQueryCount();
        final ScannedLocationEntity actual = repository.get();
        checkQueryCount(1);

        final ScannedLocationEntity expected = ScannedLocationEntity.builder()
                .id(1L)
                .geometry(createPolygon("POLYGON((1 1, 1 15, 12 15, 12 1, 1 1))"))
                .build();
        checkEquals(expected, actual);
    }

    private Polygon createPolygon(final String text) {
        return GeometryUtil.createPolygon(text, geometryFactory);
    }

    @SuppressWarnings("SameParameterValue")
    private MultiPolygon createMultiPolygon(final String text) {
        return GeometryUtil.createMultipolygon(text, geometryFactory);
    }
}
