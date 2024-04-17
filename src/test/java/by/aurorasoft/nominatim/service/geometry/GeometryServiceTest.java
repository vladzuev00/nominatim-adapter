//package by.aurorasoft.nominatim.service.geometry;
//
//import by.aurorasoft.nominatim.base.AbstractQueryInterceptorSpringBootTest;
//import by.aurorasoft.nominatim.model.Track;
//import by.aurorasoft.nominatim.model.TrackPoint;
//import org.junit.jupiter.api.Test;
//import org.locationtech.jts.geom.*;
//import org.locationtech.jts.geom.prep.PreparedGeometry;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;
//
//public final class GeometryServiceTest extends AbstractQueryInterceptorSpringBootTest {
//
//    @Autowired
//    private GeometryService service;
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Test
//    public void lineShouldBeCreated() {
//        final float firstPointGivenLatitude = 4.4F;
//        final float firstPointGivenLongitude = 5.5F;
//        final float secondPointGivenLatitude = 6.6F;
//        final float secondPointGivenLongitude = 7.7F;
//        final float thirdPointGivenLatitude = 8.8F;
//        final float thirdPointGivenLongitude = 9.9F;
//
//        final Track givenTrack = new Track(
//                List.of(
//                        createTrackPoint(firstPointGivenLatitude, firstPointGivenLongitude),
//                        createTrackPoint(secondPointGivenLatitude, secondPointGivenLongitude),
//                        createTrackPoint(thirdPointGivenLatitude, thirdPointGivenLongitude)
//                )
//        );
//
//        final LineString actual = service.createLine(givenTrack);
//        final LineString expected = geometryFactory.createLineString(
//                new Coordinate[]{
//                        new CoordinateXY(firstPointGivenLongitude, firstPointGivenLatitude),
//                        new CoordinateXY(secondPointGivenLongitude, secondPointGivenLatitude),
//                        new CoordinateXY(thirdPointGivenLongitude, thirdPointGivenLatitude)
//                }
//        );
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void geometriesShouldContainPoint() {
//        final List<PreparedGeometry> givenGeometries = List.of(
//                createPreparedGeometry(
//                        new Coordinate[]{
//                                new CoordinateXY(1, 1),
//                                new CoordinateXY(1, 2),
//                                new CoordinateXY(2, 2),
//                                new CoordinateXY(2, 1),
//                                new CoordinateXY(1, 1)
//                        }
//                ),
//                createPreparedGeometry(
//                        new Coordinate[]{
//                                new CoordinateXY(3, 3),
//                                new CoordinateXY(3, 4),
//                                new CoordinateXY(4, 4),
//                                new CoordinateXY(4, 3),
//                                new CoordinateXY(3, 3)
//                        }
//                )
//        );
//        final TrackPoint givenPoint = createTrackPoint(4, 4);
//
//        final boolean actual = service.isAnyContain(givenGeometries, givenPoint);
//        assertFalse(actual);
//    }
//
//    @Test
//    public void geometriesShouldNotContainPoint() {
//        final List<PreparedGeometry> givenGeometries = List.of(
//                createPreparedGeometry(
//                        new Coordinate[]{
//                                new CoordinateXY(1, 1),
//                                new CoordinateXY(1, 2),
//                                new CoordinateXY(2, 2),
//                                new CoordinateXY(2, 1),
//                                new CoordinateXY(1, 1)
//                        }
//                ),
//                createPreparedGeometry(
//                        new Coordinate[]{
//                                new CoordinateXY(3, 3),
//                                new CoordinateXY(3, 4),
//                                new CoordinateXY(4, 4),
//                                new CoordinateXY(4, 3),
//                                new CoordinateXY(3, 3)
//                        }
//                )
//        );
//        final TrackPoint givenPoint = createTrackPoint(4.5F, 4.5F);
//
//        final boolean actual = service.isAnyContain(givenGeometries, givenPoint);
//        assertFalse(actual);
//    }
//
//    private static TrackPoint createTrackPoint(final float latitude, final float longitude) {
//        return TrackPoint.builder()
//                .latitude(latitude)
//                .longitude(longitude)
//                .build();
//    }
//
//    private PreparedGeometry createPreparedGeometry(final Coordinate[] coordinates) {
//        final Geometry geometry = geometryFactory.createPolygon(coordinates);
//        return prepare(geometry);
//    }
//}
