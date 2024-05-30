package by.aurorasoft.distanceclassifier.it.classifydistance;

import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest;
import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest.DistanceRequest;
import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest.PointRequest;
import by.aurorasoft.distanceclassifier.it.base.AbstractIT;
import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Function;

import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.postExpectingOk;
import static by.aurorasoft.distanceclassifier.testutil.TrackCSVFileReadUtil.read;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public abstract class ClassifyingDistanceIT extends AbstractIT {
    private static final String URL = "/api/v1/classifyDistance";
    private static final int GIVEN_URBAN_SPEED_THRESHOLD = 75;

    @Test
    public final void distancesShouldBeClassifiedForTracksFromFiles() {
        test(
//                new TestArgument(
//                        "track-1.csv",
//                        new ClassifyDistanceResponse(
//                                new DistanceResponse(550, 430, 980),
//                                new DistanceResponse(670, 540, 1210)
//                        )
//                ),
//                new TestArgument(
//                        "track-2.csv",
//                        new ClassifyDistanceResponse(
//                                new DistanceResponse(0, 10.103240692886082, 10.103240692886082),
//                                new DistanceResponse(0, 11.113564762174692, 11.113564762174692)
//                        )
//                ),
//                new TestArgument(
//                        "track-3.csv",
//                        new ClassifyDistanceResponse(
//                                new DistanceResponse(8.241159744065634, 0, 8.241159744065634),
//                                new DistanceResponse(9.065275718472197, 0, 9.065275718472197)
//                        )
//                ),
//                new TestArgument(
//                        "track-4.csv",
//                        new ClassifyDistanceResponse(
//                                new DistanceResponse(1351.9430469455722, 3324.9456316080723, 4676.888678553644),
//                                new DistanceResponse(1487.1373516401302, 3657.44019476888, 5144.57754640901)
//                        )
//                ),
//                new TestArgument(
//                        "track-5.csv",
//                        new ClassifyDistanceResponse(
//                                new DistanceResponse(2372.085930733812, 4478.68297031878, 6850.768901052592),
//                                new DistanceResponse(2609.294523807194, 4926.551267350658, 7535.845791157852)
//                        )
//                ),
//                new TestArgument(
//                        "track-6.csv",
//                        new ClassifyDistanceResponse(
//                                new DistanceResponse(4570.045744948054, 9515.268801299402, 14085.314546247457),
//                                new DistanceResponse(5027.050319442857, 10466.795681429337, 15493.846000872194)
//                        )
//                ),
//                new TestArgument(
//                        "track-7.csv",
//                        new ClassifyDistanceResponse(
//                                new DistanceResponse(562.1872620573471, 1209.0217731683263, 1771.2090352256732),
//                                new DistanceResponse(618.4059882630817, 1329.9239504851594, 1948.3299387482411)
//                        )
//                ),
                new TestArgument(
                        "track-8.csv",
                        new ClassifyDistanceResponse(
                                new DistanceResponse(0, 0, 0),
                                new DistanceResponse(0, 0, 0)
                        )
                )
        );
    }

    private void test(final TestArgument... arguments) {
        stream(arguments).forEach(this::test);
    }

    private void test(final TestArgument argument) {
        final ClassifyDistanceRequest givenRequest = readRequest(argument.fileName);
        final ClassifyDistanceResponse actual = postExpectingOk(
                restTemplate,
                URL,
                givenRequest,
                ClassifyDistanceResponse.class
        );
        assertEquals(argument.expected, actual);
    }

    private ClassifyDistanceRequest readRequest(final String fileName) {
        return read(fileName)
                .getPoints()
                .stream()
                .map(this::createPointRequest)
                .collect(collectingAndThen(toList(), this::createRequest));
    }

    private PointRequest createPointRequest(final TrackPoint point) {
        final Coordinate coordinate = point.getCoordinate();
        return new PointRequest(
                coordinate.getLatitude(),
                coordinate.getLongitude(),
                point.getSpeed(),
                getGpsDistanceRequest(point),
                getOdometerDistanceRequest(point)
        );
    }

    private DistanceRequest getGpsDistanceRequest(final TrackPoint point) {
        return getDistanceRequest(point, TrackPoint::getGpsDistance);
    }

    private DistanceRequest getOdometerDistanceRequest(final TrackPoint point) {
        return getDistanceRequest(point, TrackPoint::getOdometerDistance);
    }

    private DistanceRequest getDistanceRequest(final TrackPoint point, final Function<TrackPoint, Distance> getter) {
        final Distance distance = getter.apply(point);
        return new DistanceRequest(distance.getRelative(), distance.getAbsolute());
    }

    private ClassifyDistanceRequest createRequest(final List<PointRequest> points) {
        return new ClassifyDistanceRequest(points, GIVEN_URBAN_SPEED_THRESHOLD);
    }

    @Value
    private static class ClassifyDistanceResponse {
        DistanceResponse gpsDistance;
        DistanceResponse odometerDistance;

        @JsonCreator
        public ClassifyDistanceResponse(@JsonProperty("gpsDistance") final DistanceResponse gpsDistance,
                                        @JsonProperty("odoDistance") final DistanceResponse odometerDistance) {
            this.gpsDistance = gpsDistance;
            this.odometerDistance = odometerDistance;
        }
    }

    @Value
    private static class DistanceResponse {
        double urban;
        double country;
        double total;

        @JsonCreator
        public DistanceResponse(@JsonProperty("urban") final double urban,
                                @JsonProperty("country") final double country,
                                @JsonProperty("total") final double total) {
            this.urban = urban;
            this.country = country;
            this.total = total;
        }
    }

    @Value
    private static class TestArgument {
        String fileName;
        ClassifyDistanceResponse expected;
    }
}

