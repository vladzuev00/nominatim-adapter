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
                        new ClassifyDistanceResponse(
                                new DistanceResponse(565, 415, 980),
                                new DistanceResponse(705, 505, 1210)
                        )
                ),
                new TestArgument(
                        "track-2.csv",
                        new ClassifyDistanceResponse(
                                new DistanceResponse(2.783698342039004, 7.3195423508470725, 10.103240692886077),
                                new DistanceResponse(3.0620681762429056, 8.05149658593178, 11.113564762174686)
                        )
                ),
                new TestArgument(
                        "track-3.csv",
                        new ClassifyDistanceResponse(
                                new DistanceResponse(8.241159744065634, 0, 8.241159744065634),
                                new DistanceResponse(9.065275718472195, 0, 9.065275718472195)
                        )
                ),
                new TestArgument(
                        "track-4.csv",
                        new ClassifyDistanceResponse(
                                new DistanceResponse(1248.0597596679424, 3428.8289188856943, 4676.888678553637),
                                new DistanceResponse(1372.8657356347342, 3771.7118107742754, 5144.577546409009)
                        )
                ),
                new TestArgument(
                        "track-5.csv",
                        new ClassifyDistanceResponse(
                                new DistanceResponse(1980.8001914645922, 4869.968709588005, 6850.768901052597),
                                new DistanceResponse(2178.8802106110293, 5356.965580546799, 7535.845791157828)
                        )
                ),
                new TestArgument(
                        "track-6.csv",
                        new ClassifyDistanceResponse(
                                new DistanceResponse(4211.402520209936, 9873.912026037542, 14085.314546247479),
                                new DistanceResponse(4632.5427722308805, 10861.303228641149, 15493.846000872029)
                        )
                ),
                new TestArgument(
                        "track-7.csv",
                        new ClassifyDistanceResponse(
                                new DistanceResponse(439.0995446852349, 1332.1094905404427, 1771.2090352256776),
                                new DistanceResponse(483.0094991537559, 1465.320439594486, 1948.329938748242)
                        )
                ),
                new TestArgument(
                        "track-8.csv",
                        new ClassifyDistanceResponse(
                                new DistanceResponse(1.1168918504044996, 0.04492468966782326, 1.1618165400723228),
                                new DistanceResponse(220257.14852674733, 22026.385452298575, 242283.5339790459)
                        )
                ),
                new TestArgument(
                        "track-9.csv",
                        new ClassifyDistanceResponse(
                                new DistanceResponse(1.1168918504044996, 0.7454408870435145, 1.862332737448014),
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

