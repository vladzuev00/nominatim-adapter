package by.aurorasoft.mileagecalculator.controller.mileage.model;

import by.aurorasoft.mileagecalculator.base.AbstractJunitSpringBootTest;
import by.aurorasoft.mileagecalculator.controller.mileage.model.TempMileageRequest.TempTrackPointRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static by.aurorasoft.mileagecalculator.testutil.ConstraintViolationUtil.findFirstMessage;
import static java.time.Instant.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

//TODO: remove
public final class TempMileageRequestTest extends AbstractJunitSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Test
    public void pointShouldBeConvertedToJson()
            throws Exception {
        final TempTrackPointRequest givenPoint = new TempTrackPointRequest(
                parse("2007-12-03T10:15:30.00Z"),
                5.5,
                6.6,
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

        final TempTrackPointRequest actual = objectMapper.readValue(givenJson, TempTrackPointRequest.class);
        final TempTrackPointRequest expected = new TempTrackPointRequest(
                parse("2007-12-03T10:15:30.00Z"),
                5.5,
                6.6,
                10,
                15,
                true
        );
        assertEquals(expected, actual);
    }

    @Test
    public void pointShouldBeValid() {
        final TempTrackPointRequest givenPoint = new TempTrackPointRequest(
                parse("2007-12-03T10:15:30.00Z"),
                5.5,
                6.6,
                10,
                15,
                true
        );

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void pointShouldNotBeValidBecauseOfNullDateTime() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .latitude(5.5)
                .longitude(6.6)
                .altitude(10)
                .speed(15)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfFutureDateTime() {
        final TempTrackPointRequest givenPoint = new TempTrackPointRequest(MAX, 5.5, 6.6, 10, 15, true);

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно содержать прошедшую дату или сегодняшнее число", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLatitudeIsNull() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .longitude(46.)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLatitudeIsLessThanMinimalAllowable() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .latitude(-90.1)
                .longitude(46.)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLatitudeIsBiggerThanMaximalAllowable() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .latitude(90.1)
                .longitude(46.)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLongitudeIsNull() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .latitude(45.)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLongitudeIsLessThanMinimalAllowable() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .latitude(45.)
                .longitude(-180.1)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLongitudeIsMoreThanMaximalAllowable() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .latitude(45.)
                .longitude(180.1)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfAltitudeIsNull() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .latitude(45.)
                .longitude(45.)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfSpeedIsNull() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .latitude(45.)
                .longitude(46.)
                .altitude(15)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfSpeedIsLessThanMinimalAllowable() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .latitude(45.)
                .longitude(46.)
                .altitude(15)
                .speed(-1)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно быть не меньше 0", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfSpeedIsMoreThanMaximalAllowable() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .latitude(45.)
                .longitude(46.)
                .altitude(15)
                .speed(1001)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно быть не больше 1000", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfValidIsNull() {
        final TempTrackPointRequest givenPoint = TempTrackPointRequest.builder()
                .datetime(now())
                .latitude(45.)
                .longitude(46.)
                .altitude(15)
                .speed(500)
                .build();

        final Set<ConstraintViolation<TempTrackPointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldBeConvertedToJson()
            throws Exception {
        final TempMileageRequest givenMileage = TempMileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TempTrackPointRequest(parse("2023-02-14T12:28:04Z"), 45., 46., 15, 500, true),
                                new TempTrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.001, 46., 15, 500, true)
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

        final TempMileageRequest actual = objectMapper.readValue(givenJson, TempMileageRequest.class);
        final TempMileageRequest expected = TempMileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TempTrackPointRequest(parse("2023-02-14T12:28:04Z"), 45., 46., 15, 500, true),
                                new TempTrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.001, 46., 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void mileageShouldBeValid() {
        final TempMileageRequest givenMileage = TempMileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TempTrackPointRequest(parse("2023-02-14T12:28:04Z"), 45., 46., 15, 500, true),
                                new TempTrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.001, 46., 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<TempMileageRequest>> violations = validator.validate(givenMileage);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfTrackPointsIsNull() {
        final TempMileageRequest givenMileage = TempMileageRequest.builder()
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<TempMileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfTrackPointCountIsLessThanMinimalAllowable() {
        final TempMileageRequest givenMileage = TempMileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TempTrackPointRequest(parse("2023-02-14T12:28:04Z"), 45., 46., 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<TempMileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("размер должен находиться в диапазоне от 2 до 2147483647", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfNotValidTrackPoint() {
        final TempMileageRequest givenMileage = TempMileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TempTrackPointRequest(parse("2023-02-14T12:28:04Z"), 45., 46., 15, 500, true),
                                new TempTrackPointRequest(parse("2023-02-14T12:28:05Z"), -90.1, 46., 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<TempMileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfMinDetectionSpeedIsNull() {
        final TempMileageRequest givenMileage = TempMileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TempTrackPointRequest(parse("2023-02-14T12:28:04Z"), 45., 46., 15, 500, true),
                                new TempTrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.01, 46., 15, 500, true)
                        )
                )
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<TempMileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfMinDetectionSpeedIsLessThanMinimalAllowable() {
        final TempMileageRequest givenMileage = TempMileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TempTrackPointRequest(parse("2023-02-14T12:28:04Z"), 45., 46., 15, 500, true),
                                new TempTrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.01, 46., 15, 500, true)
                        )
                )
                .minDetectionSpeed(-1)
                .maxMessageTimeout(11)
                .build();

        final Set<ConstraintViolation<TempMileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("должно быть не меньше 0", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfMaxMessageTimeoutIsNull() {
        final TempMileageRequest givenMileage = TempMileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TempTrackPointRequest(parse("2023-02-14T12:28:04Z"), 45., 46., 15, 500, true),
                                new TempTrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.01, 46., 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .build();

        final Set<ConstraintViolation<TempMileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void mileageShouldNotBeValidBecauseOfMaxMessageTimeoutIsLessThanMinimalAllowable() {
        final TempMileageRequest givenMileage = TempMileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TempTrackPointRequest(parse("2023-02-14T12:28:04Z"), 45., 46., 15, 500, true),
                                new TempTrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.01, 46., 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(-1)
                .build();

        final Set<ConstraintViolation<TempMileageRequest>> violations = validator.validate(givenMileage);
        assertEquals(1, violations.size());
        assertEquals("должно быть не меньше 0", findFirstMessage(violations));
    }

}