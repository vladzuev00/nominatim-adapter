package by.aurorasoft.nominatim.service.geometry;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Relation;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Way;
import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;

public final class GeometryServiceTest extends AbstractJunitSpringBootTest {

    @Autowired
    private GeometryService service;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void lineShouldBeCreated() {
        final float firstGivenLatitude = 4.4F;
        final float firstGivenLongitude = 5.5F;
        final float secondGivenLatitude = 6.6F;
        final float secondGivenLongitude = 7.7F;
        final float thirdGivenLatitude = 8.8F;
        final float thirdGivenLongitude = 9.9F;

        final Track givenTrack = new Track(
                List.of(
                        createTrackPoint(firstGivenLatitude, firstGivenLongitude),
                        createTrackPoint(secondGivenLatitude, secondGivenLongitude),
                        createTrackPoint(thirdGivenLatitude, thirdGivenLongitude)
                )
        );

        final LineString actual = service.createLine(givenTrack);
        final LineString expected = createLine(
                new CoordinateXY(firstGivenLongitude, firstGivenLatitude),
                new CoordinateXY(secondGivenLongitude, secondGivenLatitude),
                new CoordinateXY(thirdGivenLongitude, thirdGivenLatitude)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void geometriesShouldContainPoint() {
        final List<PreparedGeometry> givenGeometries = List.of(
                createPreparedPolygon(
                        new CoordinateXY(1, 1),
                        new CoordinateXY(1, 2),
                        new CoordinateXY(2, 2),
                        new CoordinateXY(2, 1),
                        new CoordinateXY(1, 1)
                ),
                createPreparedPolygon(
                        new CoordinateXY(3, 3),
                        new CoordinateXY(3, 4),
                        new CoordinateXY(4, 4),
                        new CoordinateXY(4, 3),
                        new CoordinateXY(3, 3)
                )
        );
        final TrackPoint givenPoint = createTrackPoint(4, 4);

        final boolean actual = service.isAnyContain(givenGeometries, givenPoint);
        assertFalse(actual);
    }

    @Test
    public void geometriesShouldNotContainPoint() {
        final List<PreparedGeometry> givenGeometries = List.of(
                createPreparedPolygon(
                        new CoordinateXY(1, 1),
                        new CoordinateXY(1, 2),
                        new CoordinateXY(2, 2),
                        new CoordinateXY(2, 1),
                        new CoordinateXY(1, 1)
                ),
                createPreparedPolygon(
                        new CoordinateXY(3, 3),
                        new CoordinateXY(3, 4),
                        new CoordinateXY(4, 4),
                        new CoordinateXY(4, 3),
                        new CoordinateXY(3, 3)
                )
        );
        final TrackPoint givenPoint = createTrackPoint(4.5F, 4.5F);

        final boolean actual = service.isAnyContain(givenGeometries, givenPoint);
        assertFalse(actual);
    }

    @Test
    public void geometryShouldBeCreatedByRelation() {
        final Relation givenRelation = Relation.builder()
                .ways(
                        List.of(
                                createWay(3, 1, 6, 2, 8, 4),
                                createWay(8, 4, 5, 5, 2, 4, 3, 1),
                                createWay(2, 7, 6, 7, 5, 10, 2, 7)
                        )
                )
                .build();

        final MultiPolygon actual = service.createMultiPolygon(givenRelation);
        final MultiPolygon expected = createMultipolygon(
                createPolygon(
                        new CoordinateXY(1, 3),
                        new CoordinateXY(2, 6),
                        new CoordinateXY(4, 8),
                        new CoordinateXY(5, 5),
                        new CoordinateXY(4, 2),
                        new CoordinateXY(1, 3)
                ),
                createPolygon(
                        new CoordinateXY(7, 2),
                        new CoordinateXY(7, 6),
                        new CoordinateXY(10, 5),
                        new CoordinateXY(7, 2)
                )
        );
        assertTrue(expected.equalsTopo(actual));
    }

    private static TrackPoint createTrackPoint(final float latitude, final float longitude) {
        return TrackPoint.builder()
                .coordinate(new by.aurorasoft.nominatim.model.Coordinate(latitude, longitude))
                .build();
    }

    private LineString createLine(final Coordinate... coordinates) {
        return geometryFactory.createLineString(coordinates);
    }

    private PreparedGeometry createPreparedPolygon(final Coordinate... coordinates) {
        return prepare(geometryFactory.createPolygon(coordinates));
    }

    @SuppressWarnings("SameParameterValue")
    private static Way createWay(final double firstLatitude, final double firstLongitude,
                                 final double secondLatitude, final double secondLongitude,
                                 final double thirdLatitude, final double thirdLongitude) {
        return new Way(
                List.of(
                        new OverpassTurboSearchCityResponse.Coordinate(firstLatitude, firstLongitude),
                        new OverpassTurboSearchCityResponse.Coordinate(secondLatitude, secondLongitude),
                        new OverpassTurboSearchCityResponse.Coordinate(thirdLatitude, thirdLongitude)
                )
        );
    }

    private static Way createWay(final double firstLatitude, final double firstLongitude,
                                 final double secondLatitude, final double secondLongitude,
                                 final double thirdLatitude, final double thirdLongitude,
                                 final double fourthLatitude, final double fourthLongitude) {
        return new Way(
                List.of(
                        new OverpassTurboSearchCityResponse.Coordinate(firstLatitude, firstLongitude),
                        new OverpassTurboSearchCityResponse.Coordinate(secondLatitude, secondLongitude),
                        new OverpassTurboSearchCityResponse.Coordinate(thirdLatitude, thirdLongitude),
                        new OverpassTurboSearchCityResponse.Coordinate(fourthLatitude, fourthLongitude)
                )
        );
    }

    private Polygon createPolygon(final Coordinate... coordinates) {
        return geometryFactory.createPolygon(coordinates);
    }

    private MultiPolygon createMultipolygon(final Polygon... polygons) {
        return geometryFactory.createMultiPolygon(polygons);
    }
}
