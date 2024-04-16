package by.aurorasoft.nominatim.it;

import by.aurorasoft.nominatim.base.AbstractSpringBootTest;
import by.aurorasoft.nominatim.rest.model.Mileage;
import by.aurorasoft.nominatim.rest.model.MileageRequest;
import by.aurorasoft.nominatim.rest.model.MileageRequest.RequestTrackPoint;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@DirtiesContext
@Transactional(propagation = NOT_SUPPORTED)
public abstract class MileageCalculationIT extends AbstractSpringBootTest {
    private static final String URL = "/api/v1/mileage";
    private static final MediaType MEDIA_TYPE = APPLICATION_JSON;

    private final MileageRequestFactory requestFactory = new MileageRequestFactory();

    @Autowired
    private TestRestTemplate restTemplate;

    @ParameterizedTest
    @Sql("classpath:sql/insert-belarus-cities.sql")
    @MethodSource("provideTrackFileNamesAndExpectedMileage")
    public final void mileageShouldBeCalculatedForTrackFromFile(final String fileName, final Mileage expected) {
        final MileageRequest givenRequest = requestFactory.create(fileName);
        final Mileage actual = requestExpectingOkHttpStatus(givenRequest);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideTrackFileNamesAndExpectedMileage() {
        return Stream.of(
                Arguments.of("2907_track-total_10.53_kobrin_2.9_country_7.63.csv", new Mileage(2.827594290976988, 7.275646401909089)),
                Arguments.of("track-minsk-8.25_km.csv", new Mileage(0, 8.241159744065632)),
                Arguments.of("track_460_40000.csv", new Mileage(0, 4676.888678553615)),
                Arguments.of("track_460_64000.csv", new Mileage(0, 6850.768901052526)),
                Arguments.of("track_460_131000.csv", new Mileage(0, 14085.314546247428)),
                Arguments.of("unit_460_13000.csv", new Mileage(0, 1771.209035225683))
        );
    }

    private Mileage requestExpectingOkHttpStatus(final MileageRequest request) {
        final HttpEntity<MileageRequest> httpEntity = createHttpEntity(request);
        final ResponseEntity<Mileage> response = restTemplate.postForEntity(URL, httpEntity, Mileage.class);
        assertSame(OK, response.getStatusCode());
        return response.getBody();
    }

    private static HttpEntity<MileageRequest> createHttpEntity(final MileageRequest body) {
        final HttpHeaders headers = createHttpHeaders();
        return new HttpEntity<>(body, headers);
    }

    private static HttpHeaders createHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MEDIA_TYPE);
        headers.setAccept(singletonList(MEDIA_TYPE));
        return headers;
    }

    private static final class MileageRequestFactory {
        private static final String TRACK_FOLDER_PATH = "./src/test/resources/tracks";
        private static final String SLASH = "/";

        private static final int MIN_DETECTION_SPEED = 0;
        private static final int MAX_MESSAGE_TIMEOUT = 30;

        private final TrackPointParser pointParser = new TrackPointParser();

        public MileageRequest create(final String fileName) {
            final List<RequestTrackPoint> trackPoints = readTrackPoints(fileName);
            return new MileageRequest(trackPoints, MIN_DETECTION_SPEED, MAX_MESSAGE_TIMEOUT);
        }

        private List<RequestTrackPoint> readTrackPoints(final String fileName) {
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

        public RequestTrackPoint parse(final String[] properties) {
            return RequestTrackPoint.builder()
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
