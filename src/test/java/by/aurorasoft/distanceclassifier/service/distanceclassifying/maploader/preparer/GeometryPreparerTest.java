package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.preparer;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.mockito.MockedStatic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public final class GeometryPreparerTest {
    private final GeometryPreparer preparer = new GeometryPreparer();

    @Test
    public void geometryShouldBePrepared() {
        try (final MockedStatic<PreparedGeometryFactory> mockedFactory = mockStatic(PreparedGeometryFactory.class)) {
            final Geometry givenGeometry = mock(Geometry.class);

            final PreparedGeometry givenPreparedGeometry = mockPreparedGeometryFor(givenGeometry, mockedFactory);

            final PreparedGeometry actual = preparer.prepare(givenGeometry);
            assertSame(givenPreparedGeometry, actual);
        }
    }

    @Test
    public void cityGeometryShouldBePrepared() {
        try (final MockedStatic<PreparedGeometryFactory> mockedFactory = mockStatic(PreparedGeometryFactory.class)) {
            final Geometry givenGeometry = mock(Geometry.class);
            final Geometry givenBoundingBox = mock(Geometry.class);
            final CityGeometry givenCityGeometry = new CityGeometry(givenGeometry, givenBoundingBox);

            final PreparedGeometry givenPreparedGeometry = mockPreparedGeometryFor(givenGeometry, mockedFactory);
            final PreparedGeometry givenPreparedBoundingBox = mockPreparedGeometryFor(givenBoundingBox, mockedFactory);

            final PreparedCityGeometry actual = preparer.prepare(givenCityGeometry);
            final PreparedCityGeometry expected = new PreparedCityGeometry(
                    givenPreparedGeometry,
                    givenPreparedBoundingBox
            );
            assertEquals(expected, actual);
        }
    }

    private static PreparedGeometry mockPreparedGeometryFor(final Geometry geometry,
                                                            final MockedStatic<PreparedGeometryFactory> mockedFactory) {
        final PreparedGeometry preparedGeometry = mock(PreparedGeometry.class);
        mockedFactory.when(() -> prepare(same(geometry))).thenReturn(preparedGeometry);
        return preparedGeometry;
    }
}
