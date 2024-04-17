//package by.aurorasoft.nominatim.crud.repository;
//
//import by.aurorasoft.nominatim.base.AbstractQueryInterceptorSpringBootTest;
//import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
//import org.junit.Test;
//import org.locationtech.jts.geom.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.jdbc.Sql;
//
//import javax.persistence.Tuple;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//import static by.aurorasoft.nominatim.model.CityType.CAPITAL;
//import static by.aurorasoft.nominatim.util.CityEntityUtil.checkEquals;
//import static by.aurorasoft.nominatim.util.IdUtil.mapToIds;
//import static java.util.stream.Collectors.toMap;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public final class CityRepositoryTest extends AbstractQueryInterceptorSpringBootTest {
//    private static final String TUPLE_ALIAS_BOUNDING_BOX = "boundingBox";
//    private static final String TUPLE_ALIAS_GEOMETRY = "geometry";
//
//    @Autowired
//    private CityRepository repository;
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Test
//    public void cityShouldBeFoundById() {
//        startQueryCount();
//        final Optional<CityEntity> optionalActual = repository.findById(255L);
//        checkQueryCount(1);
//        assertTrue(optionalActual.isPresent());
//        final CityEntity actual = optionalActual.get();
//
//        final CityEntity expected = CityEntity.builder()
//                .id(255L)
//                .name("first-city")
//                .geometry(
//                        geometryFactory.createPolygon(
//                                new Coordinate[]{
//                                        new CoordinateXY(1, 1),
//                                        new CoordinateXY(1, 2),
//                                        new CoordinateXY(2, 2),
//                                        new CoordinateXY(2, 1),
//                                        new CoordinateXY(1, 1)
//                                }
//                        )
//                )
//                .type(CAPITAL)
//                .boundingBox(
//                        geometryFactory.createPolygon(
//                                new Coordinate[]{
//                                        new CoordinateXY(1, 1),
//                                        new CoordinateXY(1, 2),
//                                        new CoordinateXY(2, 2),
//                                        new CoordinateXY(2, 1),
//                                        new CoordinateXY(1, 1)
//                                }
//                        )
//                )
//                .build();
//        checkEquals(expected, actual);
//    }
//
//    @Test
//    public void cityShouldBeSaved() {
//        final CityEntity givenCity = CityEntity.builder()
//                .name("Minsk")
//                .geometry(
//                        geometryFactory.createPolygon(
//                                new Coordinate[]{
//                                        new CoordinateXY(1, 1),
//                                        new CoordinateXY(2, 1),
//                                        new CoordinateXY(2, 2),
//                                        new CoordinateXY(1, 1)
//                                }
//                        )
//                )
//                .type(CAPITAL)
//                .boundingBox(
//                        geometryFactory.createPolygon(
//                                new Coordinate[]{
//                                        new CoordinateXY(1, 1),
//                                        new CoordinateXY(2, 1),
//                                        new CoordinateXY(2, 2),
//                                        new CoordinateXY(1, 2),
//                                        new CoordinateXY(1, 1)
//                                }
//                        )
//                )
//                .build();
//
//        startQueryCount();
//        repository.save(givenCity);
//        checkQueryCount(2);
//    }
//
//    @Test
//    public void boundingBoxesWithGeometriesShouldBeFound() {
//        startQueryCount();
//        final List<Tuple> actualTuples = repository.findBoundingBoxesWithGeometries();
//        checkQueryCount(1);
//
//        final int expectedTuplesCount = 3;
//        final int actualTuplesCount = actualTuples.size();
//        assertEquals(expectedTuplesCount, actualTuplesCount);
//
//        final Map<Geometry, Geometry> actual = findGeometriesByBoundingBox();
//        final Map<Geometry, Geometry> expected = Map.of(
//                geometryFactory.createPolygon(
//                        new Coordinate[]{
//                                new CoordinateXY(1, 1),
//                                new CoordinateXY(1, 2),
//                                new CoordinateXY(2, 2),
//                                new CoordinateXY(2, 1),
//                                new CoordinateXY(1, 1)
//                        }
//                ),
//                geometryFactory.createPolygon(
//                        new Coordinate[]{
//                                new CoordinateXY(1, 1),
//                                new CoordinateXY(1, 2),
//                                new CoordinateXY(2, 2),
//                                new CoordinateXY(2, 1),
//                                new CoordinateXY(1, 1),
//                        }
//                ),
//                geometryFactory.createPolygon(
//                        new Coordinate[]{
//                                new CoordinateXY(3, 3),
//                                new CoordinateXY(3, 4),
//                                new CoordinateXY(4, 4),
//                                new CoordinateXY(4, 3),
//                                new CoordinateXY(3, 3)
//                        }
//                ),
//                geometryFactory.createPolygon(
//                        new Coordinate[]{
//                                new CoordinateXY(3, 3),
//                                new CoordinateXY(4, 3),
//                                new CoordinateXY(4, 4),
//                                new CoordinateXY(3, 3)
//                        }
//                ),
//                geometryFactory.createPolygon(
//                        new Coordinate[]{
//                                new CoordinateXY(3, 1),
//                                new CoordinateXY(3, 2),
//                                new CoordinateXY(4, 2),
//                                new CoordinateXY(4, 1),
//                                new CoordinateXY(3, 1),
//                        }
//                ),
//                geometryFactory.createPolygon(
//                        new Coordinate[]{
//                                new CoordinateXY(3, 1),
//                                new CoordinateXY(3, 2),
//                                new CoordinateXY(4, 2),
//                                new CoordinateXY(4, 1),
//                                new CoordinateXY(3, 1),
//                        }
//                )
//        );
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @Sql(statements = "DELETE FROM city")
//    public void boundingBoxesWithGeometriesShouldNotBeFound() {
//        startQueryCount();
//        final List<Tuple> actual = repository.findBoundingBoxesWithGeometries();
//        checkQueryCount(1);
//
//        assertTrue(actual.isEmpty());
//    }
//
//    @Test
//    public void citiesIntersectedByLineShouldBeFound() {
//        final LineString givenLine = geometryFactory.createLineString(
//                new Coordinate[]{
//                        new CoordinateXY(1.5, 1.5),
//                        new CoordinateXY(3.5, 3.5),
//                        new CoordinateXY(4.5, 4.5)
//                }
//        );
//
//        startQueryCount();
//        try (final Stream<CityEntity> actual = repository.findIntersectedCities(givenLine)) {
//            checkQueryCount(1);
//            final List<Long> actualIds = mapToIds(actual);
//            final List<Long> expected = List.of(255L, 256L);
//            assertEquals(expected, actualIds);
//        }
//    }
//
//    @Test
//    public void citiesIntersectedByLineShouldNotBeFound() {
//        final LineString givenLine = geometryFactory.createLineString(
//                new Coordinate[]{
//                        new CoordinateXY(5.5, 5.5),
//                        new CoordinateXY(6.5, 6.5),
//                        new CoordinateXY(7.5, 7.5)
//                }
//        );
//
//        startQueryCount();
//        try (final Stream<CityEntity> actual = repository.findIntersectedCities(givenLine)) {
//            checkQueryCount(1);
//            assertTrue(actual.findAny().isEmpty());
//        }
//    }
//
//    private Map<Geometry, Geometry> findGeometriesByBoundingBox() {
//        return repository.findBoundingBoxesWithGeometries()
//                .stream()
//                .collect(toMap(CityRepositoryTest::getBoundingBox, CityRepositoryTest::getGeometry));
//    }
//
//    private static Geometry getBoundingBox(final Tuple tuple) {
//        return getGeometry(tuple, TUPLE_ALIAS_BOUNDING_BOX);
//    }
//
//    private static Geometry getGeometry(final Tuple tuple) {
//        return getGeometry(tuple, TUPLE_ALIAS_GEOMETRY);
//    }
//
//    private static Geometry getGeometry(final Tuple tuple, final String alias) {
//        return (Geometry) tuple.get(alias);
//    }
//}
