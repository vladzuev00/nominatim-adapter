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
                new TestArgument(
                        "track-1.csv",
                        new Response(
                                new DistanceResponse(24.775799999999997, 17.2471, 42.02289999999999),
                                new DistanceResponse(25.975800000000003, 18.2471, 44.2229)
                        )
                ),
                new TestArgument(
                        "track-2.csv",
                        new Response(
                                new DistanceResponse(4.39473671128118, 5.708503981604896, 10.103240692886075),
                                new DistanceResponse(4.834210382409298, 6.279354379765387, 11.113564762174684)
                        )
                ),
                new TestArgument(
                        "track-3.csv",
                        new Response(
                                new DistanceResponse(7.660139202484009, 0.5810205415816223, 8.241159744065632),
                                new DistanceResponse(8.42615312273241, 0.6391225957397846, 9.065275718472195)
                        )
                ),
                new TestArgument(
                        "track-4.csv",
                        new Response(
                                new DistanceResponse(1517.6749524836482, 3159.213726069959, 4676.888678553607),
                                new DistanceResponse(1669.4424477320315, 3475.135098676968, 5144.577546408999)
                        )
                ),
                new TestArgument(
                        "track-5.csv",
                        new Response(
                                new DistanceResponse(2411.816607212017, 4438.952293840524, 6850.768901052541),
                                new DistanceResponse(2652.9982679331615, 4882.847523224588, 7535.84579115775)
                        )
                ),
                new TestArgument(
                        "track-6.csv",
                        new Response(
                                new DistanceResponse(4757.130587495315, 9328.18395875217, 14085.314546247486),
                                new DistanceResponse(5232.84364624481, 10261.002354627271, 15493.846000872081)
                        )
                ),
                new TestArgument(
                        "track-7.csv",
                        new Response(
                                new DistanceResponse(588.1970860851806, 1183.0119491405042, 1771.2090352256846),
                                new DistanceResponse(647.0167946936974, 1301.3131440545535, 1948.329938748251)
                        )
                )
        );
    }

    private void test(final TestArgument... arguments) {
        stream(arguments).forEach(this::test);
    }

    private void test(final TestArgument argument) {
        final ClassifyDistanceRequest givenRequest = readRequest(argument.fileName);
        final Response actual = postExpectingOk(restTemplate, URL, givenRequest, Response.class);
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
                getGpsDistance(point),
                getOdometerDistance(point)
        );
    }

    private DistanceRequest getGpsDistance(final TrackPoint point) {
        return getDistance(point, TrackPoint::getGpsDistance);
    }

    private DistanceRequest getOdometerDistance(final TrackPoint point) {
        return getDistance(point, TrackPoint::getOdometerDistance);
    }

    private DistanceRequest getDistance(final TrackPoint point, final Function<TrackPoint, Distance> getter) {
        final Distance source = getter.apply(point);
        return new DistanceRequest(source.getRelative(), source.getAbsolute());
    }

    private ClassifyDistanceRequest createRequest(final List<PointRequest> points) {
        return new ClassifyDistanceRequest(points, GIVEN_URBAN_SPEED_THRESHOLD);
    }

    @Value
    private static class Response {
        DistanceResponse gpsDistance;
        DistanceResponse odometerDistance;

        @JsonCreator
        public Response(@JsonProperty("gpsDistance") final DistanceResponse gpsDistance,
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
        Response expected;
    }
}

