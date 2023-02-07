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


}
