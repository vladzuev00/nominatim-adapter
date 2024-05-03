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
import org.springframework.test.context.jdbc.Sql;

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

    private final RequestFactory requestFactory = new RequestFactory();

    @ParameterizedTest
    @Sql(statements = "DELETE FROM city")
    @Sql("classpath:sql/insert-belarus-cities.sql")
    @MethodSource("provideBelarusTrackFileNamesAndExpectedResponses")
    public final void distanceShouldBeClassifiedForBelarusTrackFromFile(final String fileName, final String expected)
            throws Exception {
        final ClassifyDistanceRequest givenRequest = requestFactory.create(fileName);
        final String actual = postExpectingOk(restTemplate, URL, givenRequest, String.class);
        assertEquals(expected, actual, true);
    }

    private static Stream<Arguments> provideBelarusTrackFileNamesAndExpectedResponses() {
        return Stream.of(
                Arguments.of(
                        "2907_track-total_10.53_kobrin_2.9_country_7.63.csv",
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
                        "track-minsk-8.25_km.csv",
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
                        "track_460_40000.csv",
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
                        "track_460_64000.csv",
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
                        "track_460_131000.csv",
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
                        "unit_460_13000.csv",
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

    private static final class RequestFactory {
        private static final String FOLDER_PATH = "./src/test/resources/tracks";
        private static final int URBAN_SPEED_THRESHOLD = 75;

        private final PointParser pointParser = new PointParser();

        public ClassifyDistanceRequest create(final String fileName) {
            try (final CSVReader csvReader = createReader(fileName)) {
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

        private static CSVReader createReader(final String fileName)
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
        private static final int GPS_ABSOLUTE_INDEX = 4;
        private static final int ODOMETER_RELATIVE_INDEX = 5;
        private static final int ODOMETER_ABSOLUTE_INDEX = 6;

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
            return parseDistance(properties, GPS_RELATIVE_INDEX, GPS_ABSOLUTE_INDEX);
        }

        private static DistanceRequest parseOdometerDistance(final String[] properties) {
            return parseDistance(properties, ODOMETER_RELATIVE_INDEX, ODOMETER_ABSOLUTE_INDEX);
        }

        private static DistanceRequest parseDistance(final String[] properties,
                                                     final int relativeIndex,
                                                     final int absoluteIndex) {
            final double relative = parseDouble(properties[relativeIndex]);
            final double absolute = parseDouble(properties[absoluteIndex]);
            return new DistanceRequest(relative, absolute);
        }
    }
}

