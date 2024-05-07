//package by.aurorasoft.distanceclassifier.service.geometry;
//
//import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
//import by.aurorasoft.distanceclassifier.model.*;
//import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Bounds;
//import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Node;
//import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Relation;
//import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Way;
//import org.junit.Test;
//import org.locationtech.jts.geom.Coordinate;
//import org.locationtech.jts.geom.*;
//import org.locationtech.jts.geom.prep.PreparedGeometry;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//import java.util.Set;
//
//import static org.junit.Assert.*;
//import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;
//
//public final class GeometryServiceTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private GeometryService service;
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Test
//    public void lineShouldBeCreated() {
//        final double firstGivenLatitude = 4.4;
//        final double firstGivenLongitude = 5.5;
//        final double secondGivenLatitude = 6.6;
//        final double secondGivenLongitude = 7.7;
//        final double thirdGivenLatitude = 8.8;
//        final double thirdGivenLongitude = 9.9;
//
//        final Track givenTrack = new Track(
//                List.of(
//                        createTrackPoint(firstGivenLatitude, firstGivenLongitude),
//                        createTrackPoint(secondGivenLatitude, secondGivenLongitude),
//                        createTrackPoint(thirdGivenLatitude, thirdGivenLongitude)
//                )
//        );
//
//        final LineString actual = service.createLine(givenTrack);
//        final LineString expected = createLine(
//                new CoordinateXY(firstGivenLongitude, firstGivenLatitude),
//                new CoordinateXY(secondGivenLongitude, secondGivenLatitude),
//                new CoordinateXY(thirdGivenLongitude, thirdGivenLatitude)
//        );
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void multiPolygonShouldBeCreatedByRelation() {
//        final Relation givenRelation = Relation.builder()
//                .members(
//                        List.of(
//                                new Node(1, 2),
//                                new Way(
//                                        List.of(
//                                                new OverpassSearchCityResponse.Coordinate(3, 1),
//                                                new OverpassSearchCityResponse.Coordinate(6, 2),
//                                                new OverpassSearchCityResponse.Coordinate(8, 4)
//                                        )
//                                ),
//                                new Way(
//                                        List.of(
//                                                new OverpassSearchCityResponse.Coordinate(8, 4),
//                                                new OverpassSearchCityResponse.Coordinate(5, 5),
//                                                new OverpassSearchCityResponse.Coordinate(2, 4),
//                                                new OverpassSearchCityResponse.Coordinate(3, 1)
//                                        )
//                                ),
//                                new Way(
//                                        List.of(
//                                                new OverpassSearchCityResponse.Coordinate(2, 7),
//                                                new OverpassSearchCityResponse.Coordinate(6, 7),
//                                                new OverpassSearchCityResponse.Coordinate(5, 10),
//                                                new OverpassSearchCityResponse.Coordinate(2, 7)
//                                        )
//                                )
//                        )
//                )
//                .build();
//
//        final MultiPolygon actual = service.createMultiPolygon(givenRelation);
//        final MultiPolygon expected = createMultipolygon(
//                createPolygon(
//                        new CoordinateXY(1, 3),
//                        new CoordinateXY(2, 6),
//                        new CoordinateXY(4, 8),
//                        new CoordinateXY(5, 5),
//                        new CoordinateXY(4, 2),
//                        new CoordinateXY(1, 3)
//                ),
//                createPolygon(
//                        new CoordinateXY(7, 2),
//                        new CoordinateXY(7, 6),
//                        new CoordinateXY(10, 5),
//                        new CoordinateXY(7, 2)
//                )
//        );
//        assertTrue(expected.equalsTopo(actual));
//    }
//
//    @Test
//    public void polygonShouldBeCreatedByBounds() {
//        final double givenMinLatitude = 5.5;
//        final double givenMinLongitude = 6.6;
//        final double givenMaxLatitude = 7.7;
//        final double givenMaxLongitude = 8.8;
//        final Bounds givenBounds = new Bounds(givenMinLatitude, givenMinLongitude, givenMaxLatitude, givenMaxLongitude);
//
//        final Polygon actual = service.createPolygon(givenBounds);
//        final Polygon expected = createPolygon(
//                new CoordinateXY(givenMinLongitude, givenMinLatitude),
//                new CoordinateXY(givenMinLongitude, givenMaxLatitude),
//                new CoordinateXY(givenMaxLongitude, givenMaxLatitude),
//                new CoordinateXY(givenMaxLongitude, givenMinLatitude),
//                new CoordinateXY(givenMinLongitude, givenMinLatitude)
//        );
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void polygonShouldBeCreatedByAreaCoordinate() {
//        final double givenMinLatitude = 5.5;
//        final double givenMinLongitude = 6.6;
//        final double givenMaxLatitude = 7.7;
//        final double givenMaxLongitude = 8.8;
//        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
//                new by.aurorasoft.distanceclassifier.model.Coordinate(givenMinLatitude, givenMinLongitude),
//                new by.aurorasoft.distanceclassifier.model.Coordinate(givenMaxLatitude, givenMaxLongitude)
//        );
//
//        final Polygon actual = service.createPolygon(givenAreaCoordinate);
//        final Polygon expected = createPolygon(
//                new CoordinateXY(givenMinLongitude, givenMinLatitude),
//                new CoordinateXY(givenMinLongitude, givenMaxLatitude),
//                new CoordinateXY(givenMaxLongitude, givenMaxLatitude),
//                new CoordinateXY(givenMaxLongitude, givenMinLatitude),
//                new CoordinateXY(givenMinLongitude, givenMinLatitude)
//        );
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void cityGeometriesShouldContainPoint() {
//        final Set<PreparedCityGeometry> givenGeometries = Set.of(
//                PreparedCityGeometry.builder()
//                        .geometry(
//                                createPreparedPolygon(
//                                        new CoordinateXY(1, 1),
//                                        new CoordinateXY(1, 2),
//                                        new CoordinateXY(2, 2),
//                                        new CoordinateXY(2, 1),
//                                        new CoordinateXY(1, 1)
//                                )
//                        )
//                        .build(),
//                PreparedCityGeometry.builder()
//                        .geometry(
//                                createPreparedPolygon(
//                                        new CoordinateXY(3, 3),
//                                        new CoordinateXY(3, 4),
//                                        new CoordinateXY(4, 4),
//                                        new CoordinateXY(4, 3),
//                                        new CoordinateXY(3, 3)
//                                )
//                        )
//                        .build()
//        );
//        final TrackPoint givenPoint = createTrackPoint(3.5, 3.5);
//
//        final boolean actual = service.isAnyContain(givenGeometries, givenPoint);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void cityGeometriesShouldNotContainPoint() {
//        final Set<PreparedCityGeometry> givenGeometries = Set.of(
//                PreparedCityGeometry.builder()
//                        .geometry(
//                                createPreparedPolygon(
//                                        new CoordinateXY(1, 1),
//                                        new CoordinateXY(1, 2),
//                                        new CoordinateXY(2, 2),
//                                        new CoordinateXY(2, 1),
//                                        new CoordinateXY(1, 1)
//                                )
//                        )
//                        .build(),
//                PreparedCityGeometry.builder()
//                        .geometry(
//                                createPreparedPolygon(
//                                        new CoordinateXY(3, 3),
//                                        new CoordinateXY(3, 4),
//                                        new CoordinateXY(4, 4),
//                                        new CoordinateXY(4, 3),
//                                        new CoordinateXY(3, 3)
//                                )
//                        )
//                        .build()
//        );
//        final TrackPoint givenPoint = createTrackPoint(4.5, 4.5);
//
//        final boolean actual = service.isAnyContain(givenGeometries, givenPoint);
//        assertFalse(actual);
//    }
//
//    @Test
//    public void cityBoundingBoxesShouldContainPoint() {
//        final Set<PreparedCityGeometry> givenGeometries = Set.of(
//                PreparedCityGeometry.builder()
//                        .boundingBox(
//                                createPreparedPolygon(
//                                        new CoordinateXY(1, 1),
//                                        new CoordinateXY(1, 2),
//                                        new CoordinateXY(2, 2),
//                                        new CoordinateXY(2, 1),
//                                        new CoordinateXY(1, 1)
//                                )
//                        )
//                        .build(),
//                PreparedCityGeometry.builder()
//                        .boundingBox(
//                                createPreparedPolygon(
//                                        new CoordinateXY(3, 3),
//                                        new CoordinateXY(3, 4),
//                                        new CoordinateXY(4, 4),
//                                        new CoordinateXY(4, 3),
//                                        new CoordinateXY(3, 3)
//                                )
//                        )
//                        .build()
//        );
//        final TrackPoint givenPoint = createTrackPoint(3.5, 3.5);
//
//        final boolean actual = service.isAnyBoundingBoxContain(givenGeometries, givenPoint);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void cityBoundingBoxesShouldNotContainPoint() {
//        final Set<PreparedCityGeometry> givenGeometries = Set.of(
//                PreparedCityGeometry.builder()
//                        .boundingBox(
//                                createPreparedPolygon(
//                                        new CoordinateXY(1, 1),
//                                        new CoordinateXY(1, 2),
//                                        new CoordinateXY(2, 2),
//                                        new CoordinateXY(2, 1),
//                                        new CoordinateXY(1, 1)
//                                )
//                        )
//                        .build(),
//                PreparedCityGeometry.builder()
//                        .boundingBox(
//                                createPreparedPolygon(
//                                        new CoordinateXY(3, 3),
//                                        new CoordinateXY(3, 4),
//                                        new CoordinateXY(4, 4),
//                                        new CoordinateXY(4, 3),
//                                        new CoordinateXY(3, 3)
//                                )
//                        )
//                        .build()
//        );
//        final TrackPoint givenPoint = createTrackPoint(4.5, 4.5);
//
//        final boolean actual = service.isAnyBoundingBoxContain(givenGeometries, givenPoint);
//        assertFalse(actual);
//    }
//
//    @Test
//    public void emptyPolygonShouldBeCreated() {
//        final Polygon actual = service.createEmptyPolygon();
//        assertTrue(actual.isEmpty());
//    }
//
//    private TrackPoint createTrackPoint(final double latitude, final double longitude) {
//        return TrackPoint.builder()
//                .coordinate(new by.aurorasoft.distanceclassifier.model.Coordinate(latitude, longitude))
//                .build();
//    }
//
//    private LineString createLine(final Coordinate... coordinates) {
//        return geometryFactory.createLineString(coordinates);
//    }
//
//    private PreparedGeometry createPreparedPolygon(final Coordinate... coordinates) {
//        return prepare(geometryFactory.createPolygon(coordinates));
//    }
//
//    private Polygon createPolygon(final Coordinate... coordinates) {
//        return geometryFactory.createPolygon(coordinates);
//    }
//
//    private MultiPolygon createMultipolygon(final Polygon... polygons) {
//        return geometryFactory.createMultiPolygon(polygons);
//    }
//}
