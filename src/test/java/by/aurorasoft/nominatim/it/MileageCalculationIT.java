package by.aurorasoft.nominatim.it;

import by.aurorasoft.nominatim.base.AbstractSpringBootTest;
import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest;
import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest.TrackPointRequest;
import by.aurorasoft.nominatim.model.Mileage;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static by.aurorasoft.nominatim.util.HttpUtil.postExpectingOk;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public abstract class MileageCalculationIT extends AbstractSpringBootTest {
    private static final String URL = "/api/v1/mileage";

    private final MileageRequestFactory requestFactory = new MileageRequestFactory();

    @Autowired
    private TestRestTemplate restTemplate;

    @ParameterizedTest
    @MethodSource("provideTrackFileNamesAndExpectedMileages")
    public final void mileageShouldBeFoundForTrackFromFile(final String fileName, final Mileage expected) {
        final MileageRequest givenRequest = requestFactory.create(fileName);
        final Mileage actual = findMileage(givenRequest);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideTrackFileNamesAndExpectedMileages() {
        return Stream.of(
                Arguments.of("2907_track-total_10.53_kobrin_2.9_country_7.63.csv", new Mileage(2.827594290976988, 7.27564640190909)),
                Arguments.of("track-minsk-8.25_km.csv", new Mileage(8.241159744065632, 0)),
                Arguments.of("track_460_40000.csv", new Mileage(1248.0929696134724, 3428.795708940143)),
                Arguments.of("track_460_64000.csv", new Mileage(1980.823770078472, 4869.945130974054)),
                Arguments.of("track_460_131000.csv", new Mileage(4211.268783970594, 9874.045762276834)),
                Arguments.of("unit_460_13000.csv", new Mileage(439.11730474078814, 1332.0917304848947))
        );
    }

    private Mileage findMileage(final MileageRequest request) {
        return postExpectingOk(restTemplate, URL, request, Mileage.class);
    }

    private static final class MileageRequestFactory {
        private static final String TRACK_FOLDER_PATH = "./src/test/resources/tracks";
        private static final String SLASH = "/";

        private static final int MIN_DETECTION_SPEED = 0;
        private static final int MAX_MESSAGE_TIMEOUT = 30;

        private final TrackPointParser pointParser = new TrackPointParser();

        public MileageRequest create(final String fileName) {
            final List<TrackPointRequest> trackPoints = readTrackPoints(fileName);
            return new MileageRequest(trackPoints, MIN_DETECTION_SPEED, MAX_MESSAGE_TIMEOUT);
        }

        private List<TrackPointRequest> readTrackPoints(final String fileName) {
            try (final CSVReader csvReader = createReader(fileName)) {
                return csvReader.readAll()
                        .stream()
                        .map(pointParser::parse)
                        .toList();
            } catch (final IOException | CsvException cause) {
                throw new RuntimeException(cause);
            }
        }

        private static CSVReader createReader(final String fileName)
                throws FileNotFoundException {
            final String filePath = TRACK_FOLDER_PATH + SLASH + fileName;
            return new CSVReader(new FileReader(filePath));
        }
    }

    private static final class TrackPointParser {
        private static final int DATE_TIME_INDEX = 1;
        private static final int LATITUDE_INDEX = 2;
        private static final int LONGITUDE_INDEX = 3;
        private static final int ALTITUDE_INDEX = 4;
        private static final int SPEED_INDEX = 5;
        private static final int VALID_INDEX = 6;

        private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
        private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern(DATE_TIME_PATTERN);

        private static final String VALID_TRUE_ALIAS = "VALID";

        public TrackPointRequest parse(final String[] properties) {
            return TrackPointRequest.builder()
                    .datetime(parseDateTime(properties))
                    .latitude(parseLatitude(properties))
                    .longitude(parseLongitude(properties))
                    .altitude(parseAltitude(properties))
                    .speed(parseSpeed(properties))
                    .valid(parseValid(properties))
                    .build();
        }

        private static Instant parseDateTime(final String[] properties) {
            return LocalDateTime.parse(properties[DATE_TIME_INDEX], DATE_TIME_FORMATTER).toInstant(UTC);
        }

        private static float parseLatitude(final String[] properties) {
            return parseFloat(properties[LATITUDE_INDEX]);
        }

        private static float parseLongitude(final String[] properties) {
            return parseFloat(properties[LONGITUDE_INDEX]);
        }

        private static int parseAltitude(final String[] properties) {
            return parseInt(properties[ALTITUDE_INDEX]);
        }

        private static int parseSpeed(final String[] properties) {
            return parseInt(properties[SPEED_INDEX]);
        }

        private static boolean parseValid(final String[] properties) {
            return VALID_TRUE_ALIAS.equals(properties[VALID_INDEX]);
        }
    }
}
