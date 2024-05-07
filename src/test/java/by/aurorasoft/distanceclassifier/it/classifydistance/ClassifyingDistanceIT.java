package by.aurorasoft.distanceclassifier.it.classifydistance;

import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest;
import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest.DistanceRequest;
import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest.PointRequest;
import by.aurorasoft.distanceclassifier.it.base.AbstractIT;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.postExpectingOk;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public abstract class ClassifyingDistanceIT extends AbstractIT {
    private static final String URL = "/api/v1/classifyDistance";

    private final RequestReader requestFactory = new RequestReader();

    @ParameterizedTest
    @MethodSource("provideTrackFileNamesAndExpectedResponses")
    public final void distanceShouldBeClassifiedForTrackFromFile(final String fileName, final String expected)
            throws Exception {
        final ClassifyDistanceRequest givenRequest = requestFactory.read(fileName);
        final String actual = postExpectingOk(restTemplate, URL, givenRequest, String.class);
        assertEquals(expected, actual, true);
    }

    private static Stream<Arguments> provideTrackFileNamesAndExpectedResponses() {
        return Stream.of(
                Arguments.of(
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
                Arguments.of(
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
                Arguments.of(
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
                Arguments.of(
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
                Arguments.of(
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
                Arguments.of(
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
                Arguments.of(
                        "track-7.csv",
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
                )
        );
    }

    private static final class RequestReader {
        private static final String FOLDER_PATH = "./src/test/resources/tracks";
        private static final int URBAN_SPEED_THRESHOLD = 75;

        private final PointParser pointParser = new PointParser();

        public ClassifyDistanceRequest read(final String fileName) {
            try (final CSVReader csvReader = createCSVReader(fileName)) {
                return csvReader.readAll()
                        .stream()
                        .map(pointParser::parse)
                        .collect(
                                collectingAndThen(
                                        toList(),
                                        points -> new ClassifyDistanceRequest(points, URBAN_SPEED_THRESHOLD)
                                )
                        );
            } catch (final IOException | CsvException cause) {
                throw new RuntimeException(cause);
            }
        }

        private static CSVReader createCSVReader(final String fileName)
                throws FileNotFoundException {
            final String filePath = FOLDER_PATH + "/" + fileName;
            return new CSVReader(new FileReader(filePath));
        }
    }

    private static final class PointParser {
        private static final int LATITUDE_INDEX = 0;
        private static final int LONGITUDE_INDEX = 1;
        private static final int SPEED_INDEX = 2;
        private static final int GPS_RELATIVE_INDEX = 3;
        private static final int ODOMETER_RELATIVE_INDEX = 5;

        public PointRequest parse(final String[] properties) {
            return new PointRequest(
                    parseLatitude(properties),
                    parseLongitude(properties),
                    parseSpeed(properties),
                    parseGpsDistance(properties),
                    parseOdometerDistance(properties)
            );
        }

        private static double parseLatitude(final String[] properties) {
            return parseDouble(properties[LATITUDE_INDEX]);
        }

        private static double parseLongitude(final String[] properties) {
            return parseDouble(properties[LONGITUDE_INDEX]);
        }

        private static int parseSpeed(final String[] properties) {
            return parseInt(properties[SPEED_INDEX]);
        }

        private static DistanceRequest parseGpsDistance(final String[] properties) {
            return parseDistance(properties, GPS_RELATIVE_INDEX);
        }

        private static DistanceRequest parseOdometerDistance(final String[] properties) {
            return parseDistance(properties, ODOMETER_RELATIVE_INDEX);
        }

        private static DistanceRequest parseDistance(final String[] properties, final int relativeIndex) {
            final double relative = parseDouble(properties[relativeIndex]);
            return new DistanceRequest(relative, 0.);
        }
    }
}

