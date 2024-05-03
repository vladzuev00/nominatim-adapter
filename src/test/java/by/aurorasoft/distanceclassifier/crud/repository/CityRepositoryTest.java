package by.aurorasoft.distanceclassifier.crud.repository;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity.CityGeometry;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.distanceclassifier.model.CityType.CAPITAL;
import static by.aurorasoft.distanceclassifier.model.CityType.CITY;
import static by.aurorasoft.distanceclassifier.testutil.CityEntityUtil.checkEquals;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class CityRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private CityRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void cityShouldBeFoundById() {
        final Long givenId = 255L;

        startQueryCount();
        final Optional<CityEntity> optionalActual = repository.findById(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final CityEntity actual = optionalActual.get();
        final CityEntity expected = CityEntity.builder()
                .id(givenId)
                .name("First")
                .geometry(
                        new CityGeometry(
                                createPolygon("POLYGON((3 2, 2.5 5, 6 5, 5 3, 3 2))"),
                                createPolygon("POLYGON((2.5 2, 2.5 5, 6 5, 6 2, 2.5 2))")
                        )
                )
                .type(CAPITAL)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void cityShouldBeSaved() {
        final CityEntity givenCity = CityEntity.builder()
                .name("TestCity")
                .geometry(
                        new CityGeometry(
                                createPolygon("POLYGON((1 1, 2 1, 2 2, 1 1))"),
                                createPolygon("POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))")
                        )
                )
                .type(CITY)
                .build();

        startQueryCount();
        repository.save(givenCity);
        checkQueryCount(2);
    }

    @Test
    public void geometriesShouldBeFound() {
        startQueryCount();
        try (final Stream<CityGeometry> stream = repository.findGeometries()) {
            checkQueryCount(1);

            final Set<CityGeometry> actual = stream.collect(toUnmodifiableSet());
            final Set<CityGeometry> expected = Set.of(
                    new CityGeometry(
                            createPolygon("POLYGON((3 2, 2.5 5, 6 5, 5 3, 3 2))"),
                            createPolygon("POLYGON((2.5 2, 2.5 5, 6 5, 6 2, 2.5 2))")
                    ),
                    new CityGeometry(
                            createPolygon("POLYGON((4 7.5, 4 8, 5 10.5, 7.5 11.5, 6.5 8.5, 4 7.5))"),
                            createPolygon("POLYGON((4 7.5, 4 11.5, 7.5 11.5, 7.5 7.5, 4 7.5))")
                    ),
                    new CityGeometry(
                            createPolygon("POLYGON((8 3, 8 6, 11 6, 11 3, 8 3))"),
                            createPolygon("POLYGON((8 3, 8 6, 11 6, 11 3, 8 3))")
                    )
            );
            assertEquals(expected, actual);
        }
    }

    @Test
    @Sql(statements = "DELETE FROM city")
    public void geometriesShouldNotBeFound() {
        startQueryCount();
        try (final Stream<CityGeometry> actual = repository.findGeometries()) {
            checkQueryCount(1);

            assertTrue(actual.findAny().isEmpty());
        }
    }

    @Test
    public void intersectedGeometriesShouldBeFound() {
        final LineString givenLine = createLine("LINESTRING(4 1, 4 4, 5 7, 7 8)");

        startQueryCount();
        try (final Stream<CityGeometry> stream = repository.findIntersectedGeometries(givenLine)) {
            checkQueryCount(1);

            final Set<CityGeometry> actual = stream.collect(toUnmodifiableSet());
            final Set<CityGeometry> expected = Set.of(
                    new CityGeometry(
                            createPolygon("POLYGON((3 2, 2.5 5, 6 5, 5 3, 3 2))"),
                            createPolygon("POLYGON((2.5 2, 2.5 5, 6 5, 6 2, 2.5 2))")
                    ),
                    new CityGeometry(
                            createPolygon("POLYGON((4 7.5, 4 8, 5 10.5, 7.5 11.5, 6.5 8.5, 4 7.5))"),
                            createPolygon("POLYGON((4 7.5, 4 11.5, 7.5 11.5, 7.5 7.5, 4 7.5))")
                    )
            );
            assertEquals(expected, actual);
        }
    }

    @Test
    public void intersectedGeometriesShouldNotBeFound() {
        final LineString givenLine = createLine("LINESTRING(7 1, 7.5 5, 7.5 7, 9 8)");

        startQueryCount();
        try (final Stream<CityGeometry> actual = repository.findIntersectedGeometries(givenLine)) {
            checkQueryCount(1);

            assertTrue(actual.findAny().isEmpty());
        }
    }

    private Polygon createPolygon(final String text) {
        return GeometryUtil.createPolygon(text, geometryFactory);
    }

    private LineString createLine(final String text) {
        return GeometryUtil.createLine(text, geometryFactory);
    }
}
