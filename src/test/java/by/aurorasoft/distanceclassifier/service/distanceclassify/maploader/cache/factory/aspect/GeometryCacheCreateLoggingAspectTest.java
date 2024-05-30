package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.factory.aspect;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.factory.GeometryCacheFactory;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.preparer.GeometryPreparer;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
import nl.altindag.log.LogCaptor;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static nl.altindag.log.LogCaptor.forClass;
import static org.junit.Assert.assertEquals;

@Import(GeometryCacheCreateLoggingAspectTest.TestGeometryCacheFactory.class)
public final class GeometryCacheCreateLoggingAspectTest extends AbstractSpringBootTest {

    @Autowired
    private GeometryCacheFactory factory;

    @Autowired
    private GeometryCacheCreateLoggingAspect aspect;

    @Test
    public void cacheCreateShouldBeLogged() {
        try (final LogCaptor givenLogCaptor = createLogCaptor()) {
            factory.create();

            final List<String> actual = givenLogCaptor.getLogs();
            final List<String> expected = List.of(
                    """
                            Cache with city geometries was created:
                                City geometries count: 2
                                Scanned location's geometry: POLYGON ((1 1, 1 15, 12 15, 12 1, 1 1))"""
            );
            assertEquals(expected, actual);
        }
    }

    private LogCaptor createLogCaptor() {
        return forClass(aspect.getClass());
    }

    @Primary
    @Component
    public static class TestGeometryCacheFactory extends GeometryCacheFactory {
        private final GeometryFactory geometryFactory;

        public TestGeometryCacheFactory(final GeometryPreparer geometryPreparer, final GeometryFactory geometryFactory) {
            super(geometryPreparer);
            this.geometryFactory = geometryFactory;
        }

        @Override
        protected Stream<CityGeometry> getCityGeometries() {
            return Stream.of(
                    new CityGeometry(
                            createPolygon("POLYGON((3 2, 2.5 5, 6 5, 5 3, 3 2))"),
                            createPolygon("POLYGON((2.5 2, 2.5 5, 6 5, 6 2, 2.5 2))")
                    ),
                    new CityGeometry(
                            createPolygon("POLYGON((4 7.5, 4 8, 5 10.5, 7.5 11.5, 6.5 8.5, 4 7.5))"),
                            createPolygon("POLYGON((4 7.5, 4 11.5, 7.5 11.5, 7.5 7.5, 4 7.5))")
                    )
            );
        }

        @Override
        protected Geometry getScannedGeometry() {
            return createPolygon("POLYGON((1 1, 1 15, 12 15, 12 1, 1 1))");
        }

        private Polygon createPolygon(final String text) {
            return GeometryUtil.createPolygon(text, geometryFactory);
        }
    }
}
