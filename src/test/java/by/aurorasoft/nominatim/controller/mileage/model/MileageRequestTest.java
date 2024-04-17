package by.aurorasoft.nominatim.controller.mileage.model;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest.TrackPointRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static by.aurorasoft.nominatim.util.ConstraintViolationUtil.findFirstMessage;
import static java.time.Instant.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class MileageRequestTest extends AbstractJunitSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Test
    public void pointShouldBeConvertedToJson()
            throws Exception {
        final TrackPointRequest givenPoint = new TrackPointRequest(
                parse("2007-12-03T10:15:30.00Z"),
                5.5F,
                6.6F,
                10,
                15,
                true
        );

        final String actual = objectMapper.writeValueAsString(givenPoint);
        final String expected = """
                {
                  "datetime": "2007-12-03T10:15:30Z",
                  "latitude": 5.5,
                  "longitude": 6.6,
                  "altitude": 10,
                  "speed": 15,
                  "valid": true
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToPoint()
            throws Exception {
        final String givenJson = """
                {
                  "datetime": "2007-12-03T10:15:30Z",
                  "latitude": 5.5,
                  "longitude": 6.6,
                  "altitude": 10,
                  "speed": 15,
                  "valid": true
                }""";

        final TrackPointRequest actual = objectMapper.readValue(givenJson, TrackPointRequest.class);
        final TrackPointRequest expected = new TrackPointRequest(
                parse("2007-12-03T10:15:30.00Z"),
                5.5F,
                6.6F,
                10,
                15,
                true
        );
        assertEquals(expected, actual);
    }

    @Test
    public void pointShouldBeValid() {
        final TrackPointRequest givenPoint = new TrackPointRequest(
                parse("2007-12-03T10:15:30.00Z"),
                5.5F,
                6.6F,
                10,
                15,
                true
        );

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void pointShouldNotBeValidBecauseOfNullDateTime() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .latitude(5.5F)
                .longitude(6.6F)
                .altitude(10)
                .speed(15)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfFutureDateTime() {
        final TrackPointRequest givenPoint = new TrackPointRequest(MAX, 5.5F, 6.6F, 10, 15, true);

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно содержать прошедшую дату или сегодняшнее число", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLatitudeIsNull() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLatitudeIsLessThanMinimalAllowable() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .latitude(-90.1F)
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше, чем или равно -90", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLatitudeIsBiggerThanMaximalAllowable() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .latitude(90.1F)
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно быть меньше, чем или равно 90", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLongitudeIsNull() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .latitude(45F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLongitudeIsLessThanMinimalAllowable() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(-180.1F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше, чем или равно -180", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLongitudeIsMoreThanMaximalAllowable() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(180.1F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно быть меньше, чем или равно 180", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfAltitudeIsNull() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(45F)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfSpeedIsNull() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfSpeedIsLessThanMinimalAllowable() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .speed(-1)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно быть не меньше 0", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfSpeedIsMoreThanMaximalAllowable() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .speed(1001)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно быть не больше 1000", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfValidIsNull() {
        final TrackPointRequest givenPoint = TrackPointRequest.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .build();

        final Set<ConstraintViolation<TrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldBeConvertedToJson()
            throws Exception {
        final MileageRequest givenMileage = MileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TrackPointRequest(parse("2023-02-14T12:28:04Z"), 45F, 46F, 15, 500, true),
                                new TrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.001F, 46F, 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final String actual = objectMapper.writeValueAsString(givenMileage);
        final String expected = """
                {
                  "trackPoints": [
                    {
                      "datetime": "2023-02-14T12:28:04Z",
                      "latitude": 45,
                      "longitude": 46,
                      "altitude": 15,
                      "speed": 500,
                      "valid": true
                    },
                    {
                      "datetime": "2023-02-14T12:28:05Z",
                      "latitude": 45.001,
                      "longitude": 46,
                      "altitude": 15,
                      "speed": 500,
                      "valid": true
                    }
                  ],
                  "minDetectionSpeed": 10,
                  "maxMessageTimeout": 11
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToMileage()
            throws Exception {
        final String givenJson = """
                {
                  "trackPoints": [
                    {
                      "datetime": "2023-02-14T12:28:04Z",
                      "latitude": 45,
                      "longitude": 46,
                      "altitude": 15,
                      "speed": 500,
                      "valid": true
                    },
                    {
                      "datetime": "2023-02-14T12:28:05Z",
                      "latitude": 45.001,
                      "longitude": 46,
                      "altitude": 15,
                      "speed": 500,
                      "valid": true
                    }
                  ],
                  "minDetectionSpeed": 10,
                  "maxMessageTimeout": 11
                }""";

        final MileageRequest actual = objectMapper.readValue(givenJson, MileageRequest.class);
        final MileageRequest expected = MileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TrackPointRequest(parse("2023-02-14T12:28:04Z"), 45F, 46F, 15, 500, true),
                                new TrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.001F, 46F, 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void mileageShouldBeValid() {
        final MileageRequest givenMileage = MileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TrackPointRequest(parse("2023-02-14T12:28:04Z"), 45F, 46F, 15, 500, true),
                                new TrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.001F, 46F, 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<MileageRequest>> violations = validator.validate(givenMileage);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfTrackPointsIsNull() {
        final MileageRequest givenMileage = MileageRequest.builder()
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<MileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfTrackPointCountIsLessThanMinimalAllowable() {
        final MileageRequest givenMileage = MileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TrackPointRequest(parse("2023-02-14T12:28:04Z"), 45F, 46F, 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<MileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("размер должен находиться в диапазоне от 2 до 2147483647", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfNotValidTrackPoint() {
        final MileageRequest givenMileage = MileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TrackPointRequest(parse("2023-02-14T12:28:04Z"), 45F, 46F, 15, 500, true),
                                new TrackPointRequest(parse("2023-02-14T12:28:05Z"), -90.1F, 46F, 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<MileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше, чем или равно -90", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfMinDetectionSpeedIsNull() {
        final MileageRequest givenMileage = MileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TrackPointRequest(parse("2023-02-14T12:28:04Z"), 45F, 46F, 15, 500, true),
                                new TrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.01F, 46F, 15, 500, true)
                        )
                )
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<MileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfMinDetectionSpeedIsLessThanMinimalAllowable() {
        final MileageRequest givenMileage = MileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TrackPointRequest(parse("2023-02-14T12:28:04Z"), 45F, 46F, 15, 500, true),
                                new TrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.01F, 46F, 15, 500, true)
                        )
                )
                .minDetectionSpeed(-1)
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<MileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("должно быть не меньше 0", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfMaxMessageTimeoutIsNull() {
        final MileageRequest givenMileage = MileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TrackPointRequest(parse("2023-02-14T12:28:04Z"), 45F, 46F, 15, 500, true),
                                new TrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.01F, 46F, 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .build();

        final Set<ConstraintViolation<MileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfMaxMessageTimeoutIsLessThanMinimalAllowable() {
        final MileageRequest givenMileage = MileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TrackPointRequest(parse("2023-02-14T12:28:04Z"), 45F, 46F, 15, 500, true),
                                new TrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.01F, 46F, 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(-1)
                .build();

        final Set<ConstraintViolation<MileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("должно быть не меньше 0", findFirstMessage(violations));
    }

}
