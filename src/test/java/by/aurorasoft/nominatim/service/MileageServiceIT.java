package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.rest.model.MileageRequest;
import by.aurorasoft.nominatim.rest.model.MileageRequest.TrackPoint;
import by.aurorasoft.nominatim.rest.model.MileageResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;

public final class MileageServiceIT extends AbstractContextTest {

    @Autowired
    private MileageService mileageService;

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Something', "
            + "ST_GeomFromText('POLYGON((1.01 2.01, 1.02 2.01, 1.02 2.02, 1.01 2.02, 1.01 2.01))', 4326), "
            + "'NOT_DEFINED')")
    public void case1() {
        final MileageRequest givenMileageRequest = MileageRequest.builder()
                .trackPoints(List.of(
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.015F)
                                .longitude(2.025F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.015F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.013F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.02F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
                                .latitude(1.015F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:32Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
                                .latitude(1.015F)
                                .longitude(2.018F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:32Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.013F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
                                .latitude(1.017F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:32Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
                                .latitude(1.013F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:32Z"))
                                .latitude(1.017F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:33Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
                                .latitude(1.013F)
                                .longitude(2.013F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:32Z"))
                                .latitude(1.017F)
                                .longitude(2.017F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:33Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
                                .latitude(1.015F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:32Z"))
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
                                .datetime(parse("2023-02-07T10:15:30Z"))
                                .latitude(1.005F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:31Z"))
                                .latitude(1.012F)
                                .longitude(2.018F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:32Z"))
                                .latitude(1.015F)
                                .longitude(2.015F)
                                .altitude(15)
                                .speed(15)
                                .valid(true)
                                .build(),
                        TrackPoint.builder()
                                .datetime(parse("2023-02-07T10:15:33Z"))
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
}
