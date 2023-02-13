package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.rest.model.MileageRequest;
import by.aurorasoft.nominatim.rest.model.MileageRequest.TrackPoint;
import by.aurorasoft.nominatim.rest.model.MileageResponse;
import by.nhorushko.distancecalculator.*;
import com.opencsv.CSVReader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.io.FileReader;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.parse;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public final class MileageServiceIT extends AbstractContextTest {
    private static final String FOLDER_PATH_WITH_TRACK_POINTS = "./src/test/resources/tracks";
    private static final String SLASH = "/";

    private static final String FILE_NAME_WITH_FIRST_TRACK_POINTS = "track_460_40000.csv";
    private static final String FILE_NAME_WITH_SECOND_TRACK_POINTS = "track_460_64000.csv";
    private static final String FILE_NAME_WITH_THIRD_TRACK_POINTS = "track_460_131000.csv";
    private static final String FILE_NAME_WITH_FOURTH_TRACK_POINTS = "unit_460_13000.csv";

    private static final int MIN_DETECTION_SPEED = 0;
    private static final int MAX_MESSAGE_TIMEOUT = 10;


    private static final double ALLOWABLE_INACCURACY_OF_DISTANCE = 0.00001;

    @Autowired
    private MileageService mileageService;

    @Autowired
    private DistanceCalculator distanceCalculator;

    private final TrackPointFactory trackPointFactory;

    public MileageServiceIT() {
        this.trackPointFactory = new TrackPointFactory();
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case1() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.015F)
                                .longitude(2.025F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.025F)
                                .longitude(2.025F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(0, 1.111948206008131);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case2() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.015F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(1.111948206008131, 0);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case3() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.025F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(0, 2.223896412016262);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case4() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.015F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.025F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(0., 1.111948206008131);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case5() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.013F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.016F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(0.3335871128960806, 0.);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case6() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.02F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.03F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(0, 1.111948206008131);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case7() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.01F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(0, 0.5559741030040655);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case8() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.015F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:32Z"))
                                .latitude(1.025F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(1.111948206008131,
                1.111948206008131);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case9() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.015F)
                                .longitude(2.018F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:32Z"))
                                .latitude(1.025F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(1.1608862638235358,
                1.1608859675523793);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case10() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.013F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.017F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:32Z"))
                                .latitude(1.025F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(0.44477398021596953,
                0.8895612159001461);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case11() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.013F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:32Z"))
                                .latitude(1.017F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:33Z"))
                                .latitude(1.025F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(1.3343351961161156,
                0.8895612159001461);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case12() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.013F)
                                .longitude(2.013F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:32Z"))
                                .latitude(1.017F)
                                .longitude(2.017F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:33Z"))
                                .latitude(1.025F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(1.545888809855105,
                0.9169261331290726);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'First', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(256, 'Second', "
            + "ST_GeomFromText('POLYGON((1.005 2.01, 1.01 2, 1.025 2.02, 1.02 2.03, 1.005 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case13() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.015F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:32Z"))
                                .latitude(1.025F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(1.111948206008131,
                1.111948206008131);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'First', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(256, 'Second', "
            + "ST_GeomFromText('POLYGON((1.005 2.01, 1.01 2, 1.025 2.02, 1.02 2.03, 1.005 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case14() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:31Z"))
                                .latitude(1.012F)
                                .longitude(2.018F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:32Z"))
                                .latitude(1.015F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(Instant.parse("2023-02-07T10:15:33Z"))
                                .latitude(1.025F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build()
                ))
                .minDetectionSpeed(1)
                .maxMessageTimeout(15)
                .build();

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(1.3185101144130753,
                1.111948206008131);
        assertEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/insert-belarus-city.sql")
    public void mileageShouldBeCalculatedForFirstTrackPoints()
            throws Exception {
        final List<TrackPoint> givenTrackPoints = this.readTrackPoints(FILE_NAME_WITH_FIRST_TRACK_POINTS);
        final MileageRequest givenMileageRequest = createMileageRequest(givenTrackPoints);

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(355.72511991398096,
                357.73277661649564);
        assertEquals(expected, actual);

        final double actualAllDistance = actual.getCityMileage() + actual.getCountryMileage();
        final double expectedAllDistance = this.findExpectedAllDistance(givenTrackPoints);
        assertEquals(expectedAllDistance, actualAllDistance, ALLOWABLE_INACCURACY_OF_DISTANCE);
    }

    @Test
    @Sql("classpath:sql/insert-belarus-city.sql")
    public void mileageShouldBeCalculatedForSecondTrackPoints()
            throws Exception {
        final List<TrackPoint> givenTrackPoints = this.readTrackPoints(FILE_NAME_WITH_SECOND_TRACK_POINTS);
        final MileageRequest givenMileageRequest = createMileageRequest(givenTrackPoints);

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(562.8989367618942,
                518.031547816519);
        assertEquals(expected, actual);

        final double actualAllDistance = actual.getCityMileage() + actual.getCountryMileage();
        final double expectedAllDistance = this.findExpectedAllDistance(givenTrackPoints);
        assertEquals(expectedAllDistance, actualAllDistance, ALLOWABLE_INACCURACY_OF_DISTANCE);
    }

    @Test
    @Sql("classpath:sql/insert-belarus-city.sql")
    public void mileageShouldBeCalculatedForThirdTrackPoints()
            throws Exception {
        final List<TrackPoint> givenTrackPoints = this.readTrackPoints(FILE_NAME_WITH_THIRD_TRACK_POINTS);
        final MileageRequest givenMileageRequest = createMileageRequest(givenTrackPoints);

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(1207.6871617415154,
                1241.2063545061649);
        assertEquals(expected, actual);

        final double actualAllDistance = actual.getCityMileage() + actual.getCountryMileage();
        final double expectedAllDistance = this.findExpectedAllDistance(givenTrackPoints);
        assertEquals(expectedAllDistance, actualAllDistance, ALLOWABLE_INACCURACY_OF_DISTANCE);
    }

    @Test
    @Sql("classpath:sql/insert-belarus-city.sql")
    public void mileageShouldBeCalculatedByForFourthTrackPoints()
            throws Exception {
        final List<TrackPoint> givenTrackPoints = this.readTrackPoints(FILE_NAME_WITH_FOURTH_TRACK_POINTS);
        final MileageRequest givenMileageRequest = createMileageRequest(givenTrackPoints);

        final MileageResponse actual = this.mileageService.findMileage(givenMileageRequest);
        final MileageResponse expected = new MileageResponse(125.27265649913504,
                117.8709566688135);
        assertEquals(expected, actual);

        final double actualAllDistance = actual.getCityMileage() + actual.getCountryMileage();
        final double expectedAllDistance = this.findExpectedAllDistance(givenTrackPoints);
        assertEquals(expectedAllDistance, actualAllDistance, ALLOWABLE_INACCURACY_OF_DISTANCE);
    }

    private List<TrackPoint> readTrackPoints(String fileName)
            throws Exception {
        final String filePath = FOLDER_PATH_WITH_TRACK_POINTS + SLASH + fileName;
        try (final CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            final List<String[]> readTrackPointsProperties = csvReader.readAll();
            return readTrackPointsProperties.stream()
                    .map(this.trackPointFactory::create)
                    .collect(toList());
        }
    }

    private static MileageRequest createMileageRequest(List<TrackPoint> trackPoints) {
        return MileageRequest.builder()
                .trackPoints(trackPoints)
                .minDetectionSpeed(MIN_DETECTION_SPEED)
                .maxMessageTimeout(MAX_MESSAGE_TIMEOUT)
                .build();
    }

    private double findExpectedAllDistance(List<TrackPoint> trackPoints) {
        final List<LatLngAlt> latLngAlts = trackPoints.stream()
                .map(MileageServiceIT::mapToLatLngAlt)
                .collect(toList());
        final DistanceCalculatorSettings distanceCalculatorSettings = new DistanceCalculatorSettingsImpl(
                MIN_DETECTION_SPEED, MAX_MESSAGE_TIMEOUT);
        return this.distanceCalculator.calculateDistance(latLngAlts, distanceCalculatorSettings);
    }

    private static LatLngAlt mapToLatLngAlt(TrackPoint trackPoint) {
        return new LatLngAltImpl(trackPoint.getDatetime(), trackPoint.getLatitude(), trackPoint.getLongitude(),
                trackPoint.getAltitude(), trackPoint.getSpeed(), trackPoint.isValid());
    }

    private static final class TrackPointFactory {
        private static final int INDEX_READ_PROPERTY_DATE_TIME = 1;
        private static final int INDEX_READ_PROPERTY_LATITUDE = 2;
        private static final int INDEX_READ_PROPERTY_LONGITUDE = 3;
        private static final int INDEX_READ_PROPERTY_ALTITUDE = 4;
        private static final int INDEX_READ_PROPERTY_SPEED = 5;
        private static final int INDEX_READ_PROPERTY_VALID = 6;

        private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
        private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern(DATE_TIME_PATTERN);

        private static final String READ_PROPERTY_VALUE_OF_VALID_TO_BE_TRUE = "VALID";

        public TrackPoint create(String[] readProperties) {
            return TrackPoint.builder()
                    .datetime(parseDateTime(readProperties))
                    .latitude(parseLatitude(readProperties))
                    .longitude(parseLongitude(readProperties))
                    .altitude(parseAltitude(readProperties))
                    .speed(parseSpeed(readProperties))
                    .valid(parseValid(readProperties))
                    .build();
        }

        private static Instant parseDateTime(String[] readProperties) {
            return parse(readProperties[INDEX_READ_PROPERTY_DATE_TIME], DATE_TIME_FORMATTER).toInstant(UTC);
        }

        private static float parseLatitude(String[] readProperties) {
            return parseFloat(readProperties[INDEX_READ_PROPERTY_LATITUDE]);
        }

        private static float parseLongitude(String[] readProperties) {
            return parseFloat(readProperties[INDEX_READ_PROPERTY_LONGITUDE]);
        }

        private static int parseAltitude(String[] readProperties) {
            return parseInt(readProperties[INDEX_READ_PROPERTY_ALTITUDE]);
        }

        private static int parseSpeed(String[] readProperties) {
            return parseInt(readProperties[INDEX_READ_PROPERTY_SPEED]);
        }

        private static boolean parseValid(String[] readProperties) {
            return READ_PROPERTY_VALUE_OF_VALID_TO_BE_TRUE.equals(readProperties[INDEX_READ_PROPERTY_VALID]);
        }
    }
}
