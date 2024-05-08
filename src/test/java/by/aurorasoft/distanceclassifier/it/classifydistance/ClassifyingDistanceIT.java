package by.aurorasoft.distanceclassifier.it.classifydistance;

import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest;
import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest.DistanceRequest;
import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest.PointRequest;
import by.aurorasoft.distanceclassifier.it.base.AbstractIT;
import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.aurorasoft.distanceclassifier.testutil.TrackCSVFileReadUtil.Line;
import by.nhorushko.classifieddistance.Distance;
import lombok.SneakyThrows;
import lombok.Value;
import org.json.JSONException;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.postExpectingOk;
import static by.aurorasoft.distanceclassifier.testutil.TrackCSVFileReadUtil.read;
import static java.util.Arrays.stream;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public abstract class ClassifyingDistanceIT extends AbstractIT {
    private static final String MESSAGE_TEMPLATE_FAILED_TEST = "Test failed for '%s'.\nExpected: '%s'\nActual: '%s'";
    private static final String URL = "/api/v1/classifyDistance";
    private static final int URBAN_SPEED_THRESHOLD = 75;

    @Test
    public final void distancesShouldBeClassifiedForTracksFromFiles() {
        test(
                new TestArgument(
                        "track-1.csv",
                        """
                                {
                                  "gpsDistance": {
                                    "urban": 24.775799999999997,
                                    "country": 17.2471,
                                    "total": 42.02289999999999
                                  },
                                  "odoDistance": {
                                    "urban": 25.975800000000003,
                                    "country": 18.2471,
                                    "total": 44.2229
                                  }
                                }"""
                ),
                new TestArgument(
                        "track-2.csv",
                        """
                                {
                                  "gpsDistance": {
                                    "urban": 4.39473671128118,
                                    "country": 5.708503981604896,
                                    "total": 10.103240692886075
                                  },
                                  "odoDistance": {
                                    "urban": 4.834210382409298,
                                    "country": 6.279354379765387,
                                    "total": 11.113564762174684
                                  }
                                }"""
                ),
                new TestArgument(
                        "track-3.csv",
                        """
                                {
                                  "gpsDistance": {
                                    "urban": 7.660139202484009,
                                    "country": 0.5810205415816223,
                                    "total": 8.241159744065632
                                  },
                                  "odoDistance": {
                                    "urban": 8.42615312273241,
                                    "country": 0.6391225957397846,
                                    "total": 9.065275718472195
                                  }
                                }"""
                ),
                new TestArgument(
                        "track-4.csv",
                        """
                                {
                                  "gpsDistance": {
                                    "urban": 1517.6749524836482,
                                    "country": 3159.213726069959,
                                    "total": 4676.888678553607
                                  },
                                  "odoDistance": {
                                    "urban": 1669.4424477320315,
                                    "country": 3475.135098676968,
                                    "total": 5144.577546408999
                                  }
                                }"""
                ),
                new TestArgument(
                        "track-5.csv",
                        """
                                {
                                  "gpsDistance": {
                                    "urban": 2411.816607212017,
                                    "country": 4438.952293840524,
                                    "total": 6850.768901052541
                                  },
                                  "odoDistance": {
                                    "urban": 2652.9982679331615,
                                    "country": 4882.847523224588,
                                    "total": 7535.84579115775
                                  }
                                }"""
                ),
                new TestArgument(
                        "track-6.csv",
                        """
                                {
                                  "gpsDistance": {
                                    "urban": 4757.130587495315,
                                    "country": 9328.18395875217,
                                    "total": 14085.314546247486
                                  },
                                  "odoDistance": {
                                    "urban": 5232.84364624481,
                                    "country": 10261.002354627271,
                                    "total": 15493.846000872081
                                  }
                                }"""
                ),
                new TestArgument(
                        "track-7.csv",
                        """
                                {
                                  "gpsDistance": {
                                    "urban": 588.1970860851806,
                                    "country": 1183.0119491405042,
                                    "total": 1771.2090352256846
                                  },
                                  "odoDistance": {
                                    "urban": 647.0167946936974,
                                    "country": 1301.3131440545535,
                                    "total": 1948.329938748251
                                  }
                                }"""
                )
        );
    }

    private void test(final TestArgument... arguments) {
        stream(arguments).forEach(this::test);
    }

    @SneakyThrows(JSONException.class)
    private void test(final TestArgument argument) {
        final ClassifyDistanceRequest givenRequest = readRequest(argument.fileName);
        final String actual = postExpectingOk(restTemplate, URL, givenRequest, String.class);
        assertEquals(createMessageFailedTest(argument, actual), argument.expected, actual, true);
    }

    private ClassifyDistanceRequest readRequest(final String fileName) {
        return read(fileName, this::createPoint, this::createRequest);
    }

    private PointRequest createPoint(final Line line) {
        final Coordinate coordinate = line.getCoordinate();
        return new PointRequest(
                coordinate.getLatitude(),
                coordinate.getLongitude(),
                line.getSpeed(),
                getGpsDistance(line),
                getOdometerDistance(line)
        );
    }

    private DistanceRequest getGpsDistance(final Line line) {
        return getDistance(line, Line::getGpsDistance);
    }

    private DistanceRequest getOdometerDistance(final Line line) {
        return getDistance(line, Line::getOdometerDistance);
    }

    private DistanceRequest getDistance(final Line line, final Function<Line, Distance> getter) {
        final Distance source = getter.apply(line);
        return new DistanceRequest(source.getRelative(), source.getAbsolute());
    }

    private ClassifyDistanceRequest createRequest(final List<PointRequest> points) {
        return new ClassifyDistanceRequest(points, URBAN_SPEED_THRESHOLD);
    }

    private String createMessageFailedTest(final TestArgument argument, final String actual) {
        return MESSAGE_TEMPLATE_FAILED_TEST.formatted(argument.fileName, argument.expected, actual);
    }

    @Value
    private static class TestArgument {
        String fileName;
        String expected;
    }
}

