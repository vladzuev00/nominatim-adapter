package by.aurorasoft.distanceclassifier.crud.service;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.distanceclassifier.testutil.IdUtil.mapToIds;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class CityServiceTest extends AbstractSpringBootTest {

    @Autowired
    private CityService service;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void allCitiesShouldBeFound() {
        final Page<City> actual = service.findAll(PageRequest.of(0, 3));
        final Set<Long> actualIds = mapToIds(actual);
        final Set<Long> expectedIds = Set.of(255L, 256L, 257L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    @Sql(statements = "DELETE FROM city")
    public void allCitiesShouldNotBeFound() {
        final Page<City> actual = service.findAll(PageRequest.of(0, 3));
        assertTrue(actual.isEmpty());
    }

    @Test
    public void geometriesShouldBeFound() {
        try (final Stream<CityGeometry> stream = service.findGeometries()) {
            final Set<CityGeometry> actual = stream.collect(toUnmodifiableSet());
            final Set<CityGeometry> expected = Set.of(
                    new CityGeometry(
                            createPolygon("POLYGON((2 3, 5 2.5, 5 6, 3 5, 2 3))"),
                            createPolygon("POLYGON((2 2.5, 5 2.5, 5 6, 2 6, 2 2.5))")
                    ),
                    new CityGeometry(
                            createPolygon("POLYGON((7.5 4, 8 4, 10.5 5, 11.5 7.5, 8.5 6.5, 7.5 4))"),
                            createPolygon("POLYGON((7.5 4, 11.5 4, 11.5 7.5, 7.5 7.5, 7.5 4))")
                    ),
                    new CityGeometry(
                            createPolygon("POLYGON((3 8, 6 8, 6 11, 3 11, 3 8))"),
                            createPolygon("POLYGON((3 8, 6 8, 6 11, 3 11, 3 8))")
                    )
            );
            assertEquals(expected, actual);
        }
    }

    @Test
    @Sql(statements = "DELETE FROM city")
    public void geometriesShouldNotBeFound() {
        try (final Stream<CityGeometry> actual = service.findGeometries()) {
            assertTrue(actual.findAny().isEmpty());
        }
    }

    @Test
    public void intersectedGeometriesShouldBeFound() {
        final LineString givenLine = createLine("LINESTRING(1 4, 4 4, 7 5, 8 7)");

        try (final Stream<CityGeometry> stream = service.findIntersectedGeometries(givenLine)) {
            final Set<CityGeometry> actual = stream.collect(toUnmodifiableSet());
            final Set<CityGeometry> expected = Set.of(
                    new CityGeometry(
                            createPolygon("POLYGON((2 3, 5 2.5, 5 6, 3 5, 2 3))"),
                            createPolygon("POLYGON((2 2.5, 5 2.5, 5 6, 2 6, 2 2.5))")
                    ),
                    new CityGeometry(
                            createPolygon("POLYGON((7.5 4, 8 4, 10.5 5, 11.5 7.5, 8.5 6.5, 7.5 4))"),
                            createPolygon("POLYGON((7.5 4, 11.5 4, 11.5 7.5, 7.5 7.5, 7.5 4))")
                    )
            );
            assertEquals(expected, actual);
        }
    }

    @Test
    public void intersectedGeometriesShouldNotBeFound() {
        final LineString givenLine = createLine("LINESTRING(1 7, 5 7.5, 7 7.5, 8 9)");

        try (final Stream<CityGeometry> stream = service.findIntersectedGeometries(givenLine)) {
            assertTrue(stream.findAny().isEmpty());
        }
    }

    private Polygon createPolygon(final String text) {
        return GeometryUtil.createPolygon(text, geometryFactory);
    }

    private LineString createLine(final String text) {
        return GeometryUtil.createLine(text, geometryFactory);
    }
}
