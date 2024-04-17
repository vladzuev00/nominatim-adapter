package by.aurorasoft.nominatim.crud.service;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.crud.model.dto.City;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static by.aurorasoft.nominatim.util.IdUtil.mapToIds;
import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CityServiceTest extends AbstractJunitSpringBootTest {

    @Autowired
    private CityService service;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void allCitiesShouldBeFound() {
        final List<City> actual = service.findAll(PageRequest.of(0, 3));
        final List<Long> actualIds = mapToIds(actual);
        final List<Long> expectedIds = List.of(255L, 256L, 257L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void allCitiesShouldNotBeFound() {
        final List<City> actual = service.findAll(PageRequest.of(1, 3));
        assertTrue(actual.isEmpty());
    }

    @Test
    public void preparedGeometriesByPreparedBoundingBoxesShouldBeFound() {
        final Map<PreparedGeometry, PreparedGeometry> actual = service.findPreparedGeometriesByPreparedBoundingBoxes();
        final Map<Geometry, Geometry> actualGeometriesByBoundingBoxes = unwrapGeometries(actual);
        final Map<Geometry, Geometry> expectedGeometriesByBoundingBoxes = Map.of(
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new CoordinateXY(1, 1),
                                new CoordinateXY(1, 2),
                                new CoordinateXY(2, 2),
                                new CoordinateXY(2, 1),
                                new CoordinateXY(1, 1)
                        }
                ),
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new CoordinateXY(1, 1),
                                new CoordinateXY(1, 2),
                                new CoordinateXY(2, 2),
                                new CoordinateXY(2, 1),
                                new CoordinateXY(1, 1),
                        }
                ),
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new CoordinateXY(3, 3),
                                new CoordinateXY(3, 4),
                                new CoordinateXY(4, 4),
                                new CoordinateXY(4, 3),
                                new CoordinateXY(3, 3)
                        }
                ),
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new CoordinateXY(3, 3),
                                new CoordinateXY(4, 3),
                                new CoordinateXY(4, 4),
                                new CoordinateXY(3, 3)
                        }
                ),
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new CoordinateXY(3, 1),
                                new CoordinateXY(3, 2),
                                new CoordinateXY(4, 2),
                                new CoordinateXY(4, 1),
                                new CoordinateXY(3, 1),
                        }
                ),
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new CoordinateXY(3, 1),
                                new CoordinateXY(3, 2),
                                new CoordinateXY(4, 2),
                                new CoordinateXY(4, 1),
                                new CoordinateXY(3, 1),
                        }
                )
        );
        assertEquals(expectedGeometriesByBoundingBoxes, actualGeometriesByBoundingBoxes);
    }

    @Test
    @Sql(statements = "DELETE FROM city")
    public void preparedGeometriesByPreparedBoundingBoxesShouldNotBeFound() {
        final Map<PreparedGeometry, PreparedGeometry> actual = service.findPreparedGeometriesByPreparedBoundingBoxes();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void intersectedPreparedGeometriesShouldBeFound() {
        final LineString givenLine = geometryFactory.createLineString(
                new Coordinate[]{
                        new CoordinateXY(1.5, 1.5),
                        new CoordinateXY(3.5, 3.5),
                        new CoordinateXY(4.5, 4.5)
                }
        );

        final List<PreparedGeometry> actual = service.findIntersectedPreparedGeometries(givenLine);
        final List<Geometry> actualGeometries = mapToGeometries(actual);
        final List<Geometry> expectedGeometries = List.of(
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new CoordinateXY(1, 1),
                                new CoordinateXY(1, 2),
                                new CoordinateXY(2, 2),
                                new CoordinateXY(2, 1),
                                new CoordinateXY(1, 1)
                        }),
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new CoordinateXY(3, 3),
                                new CoordinateXY(4, 3),
                                new CoordinateXY(4, 4),
                                new CoordinateXY(3, 3)
                        })
        );
        assertEquals(expectedGeometries, actualGeometries);
    }

    @Test
    public void intersectedPreparedGeometriesShouldNotBeFound() {
        final LineString givenLine = geometryFactory.createLineString(
                new Coordinate[]{
                        new CoordinateXY(5.5, 5.5),
                        new CoordinateXY(6.5, 6.5),
                        new CoordinateXY(7.5, 7.5)
                }
        );

        final List<PreparedGeometry> actual = service.findIntersectedPreparedGeometries(givenLine);
        assertTrue(actual.isEmpty());
    }

    private static Map<Geometry, Geometry> unwrapGeometries(final Map<PreparedGeometry, PreparedGeometry> source) {
        return source.entrySet()
                .stream()
                .collect(toMap(entry -> entry.getKey().getGeometry(), entry -> entry.getValue().getGeometry()));
    }

    private static List<Geometry> mapToGeometries(final List<PreparedGeometry> preparedGeometries) {
        return preparedGeometries.stream()
                .map(PreparedGeometry::getGeometry)
                .toList();
    }
}
