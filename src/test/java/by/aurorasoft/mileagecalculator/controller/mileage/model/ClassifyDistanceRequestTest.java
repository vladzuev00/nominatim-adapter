package by.aurorasoft.mileagecalculator.controller.mileage.model;

import by.aurorasoft.mileagecalculator.base.AbstractJunitSpringBootTest;
import by.aurorasoft.mileagecalculator.controller.mileage.model.ClassifyDistanceRequest.DistanceRequest;
import by.aurorasoft.mileagecalculator.controller.mileage.model.ClassifyDistanceRequest.PointRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static by.aurorasoft.mileagecalculator.testutil.ConstraintViolationUtil.findFirstMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class ClassifyDistanceRequestTest extends AbstractJunitSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Test
    public void distanceShouldBeMappedToJson()
            throws Exception {
        final DistanceRequest givenDistance = new DistanceRequest(5.5, 6.6);

        final String actual = objectMapper.writeValueAsString(givenDistance);
        final String expected = """
                {
                  "relative": 5.5,
                  "absolute": 6.6
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeMappedToDistance()
            throws Exception {
        final String givenJson = """
                {
                  "relative": 5.5,
                  "absolute": 6.6
                }""";

        final DistanceRequest actual = objectMapper.readValue(givenJson, DistanceRequest.class);
        final DistanceRequest expected = new DistanceRequest(5.5, 6.6);
        assertEquals(expected, actual);
    }

    @Test
    public void distanceShouldBeValid() {
        final DistanceRequest givenDistance = new DistanceRequest(0., 6.6);

        final Set<ConstraintViolation<DistanceRequest>> violations = validator.validate(givenDistance);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void distanceShouldNotBeValidBecauseOfRelativeIsNull() {
        final DistanceRequest givenDistance = DistanceRequest.builder()
                .absolute(6.6)
                .build();

        final Set<ConstraintViolation<DistanceRequest>> violations = validator.validate(givenDistance);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void distanceShouldNotBeValidBecauseOfRelativeIsNegative() {
        final DistanceRequest givenDistance = new DistanceRequest(-0.0000000000001, 6.6);

        final Set<ConstraintViolation<DistanceRequest>> violations = validator.validate(givenDistance);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше или равно 0", findFirstMessage(violations));
    }

    @Test
    public void distanceShouldNotBeValidBecauseOfAbsoluteIsNull() {
        final DistanceRequest givenDistance = DistanceRequest.builder()
                .relative(0.)
                .build();

        final Set<ConstraintViolation<DistanceRequest>> violations = validator.validate(givenDistance);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void distanceShouldNotBeValidBecauseOfAbsoluteIsNegative() {
        final DistanceRequest givenDistance = new DistanceRequest(0.0000000000001, -0.0000000000001);

        final Set<ConstraintViolation<DistanceRequest>> violations = validator.validate(givenDistance);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше или равно 0", findFirstMessage(violations));
    }

    @Test
    public void pointShouldBeConvertedToJson()
            throws Exception {
        final PointRequest givenPoint = new PointRequest(
                5.5,
                6.6,
                50,
                new DistanceRequest(10., 11.),
                new DistanceRequest(12., 13.)
        );

        final String actual = objectMapper.writeValueAsString(givenPoint);
        final String expected = """
                {
                  "latitude": 5.5,
                  "longitude": 6.6,
                  "speed": 50,
                  "gpsDistance": {
                    "relative": 10,
                    "absolute": 11
                  },
                  "odometerDistance": {
                    "relative": 12,
                    "absolute": 13
                  }
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToPoint()
            throws Exception {
        final String givenJson = """
                {
                  "latitude": 5.5,
                  "longitude": 6.6,
                  "speed": 50,
                  "gpsDistance": {
                    "relative": 10,
                    "absolute": 11
                  },
                  "odometerDistance": {
                    "relative": 12,
                    "absolute": 13
                  }
                }""";

        final PointRequest actual = objectMapper.readValue(givenJson, PointRequest.class);
        final PointRequest expected = new PointRequest(
                5.5,
                6.6,
                50,
                new DistanceRequest(10., 11.),
                new DistanceRequest(12., 13.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void pointShouldBeValid() {
        final PointRequest givenPoint = new PointRequest(
                5.5,
                6.6,
                50,
                new DistanceRequest(10., 11.),
                new DistanceRequest(12., 13.)
        );

        final Set<ConstraintViolation<PointRequest>> violations = validator.validate(givenPoint);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLatitudeIsNotValid() {
        final PointRequest givenPoint = PointRequest.builder()
                .longitude(6.6)
                .speed(50)
                .gpsDistance(new DistanceRequest(10., 11.))
                .odometerDistance(new DistanceRequest(12., 13.))
                .build();

        final Set<ConstraintViolation<PointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfLongitudeIsNotValid() {
        final PointRequest givenPoint = PointRequest.builder()
                .latitude(5.5)
                .speed(50)
                .gpsDistance(new DistanceRequest(10., 11.))
                .odometerDistance(new DistanceRequest(12., 13.))
                .build();

        final Set<ConstraintViolation<PointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfSpeedIsNull() {
        final PointRequest givenPoint = PointRequest.builder()
                .latitude(5.5)
                .longitude(6.6)
                .gpsDistance(new DistanceRequest(10., 11.))
                .odometerDistance(new DistanceRequest(12., 13.))
                .build();

        final Set<ConstraintViolation<PointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfSpeedIsNegative() {
        final PointRequest givenPoint = new PointRequest(
                5.5,
                6.6,
                -1,
                new DistanceRequest(10., 11.),
                new DistanceRequest(12., 13.)
        );

        final Set<ConstraintViolation<PointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше или равно 0", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfGpsDistanceIsNull() {
        final PointRequest givenPoint = PointRequest.builder()
                .latitude(5.5)
                .longitude(6.6)
                .speed(50)
                .odometerDistance(new DistanceRequest(12., 13.))
                .build();

        final Set<ConstraintViolation<PointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfOdometerDistanceIsNull() {
        final PointRequest givenPoint = PointRequest.builder()
                .latitude(5.5)
                .longitude(6.6)
                .speed(50)
                .gpsDistance(new DistanceRequest(10., 11.))
                .build();

        final Set<ConstraintViolation<PointRequest>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void requestShouldBeConvertedToJson()
            throws Exception {
        final ClassifyDistanceRequest givenRequest = new ClassifyDistanceRequest(
                List.of(
                        new PointRequest(
                                5.5,
                                6.6,
                                50,
                                new DistanceRequest(10., 11.),
                                new DistanceRequest(12., 13.)
                        ),
                        new PointRequest(
                                8.8,
                                9.9,
                                85,
                                new DistanceRequest(20., 21.),
                                new DistanceRequest(22., 23.)
                        )
                ),
                60
        );

        final String actual = objectMapper.writeValueAsString(givenRequest);
        final String expected = """
                {
                  "trackPoints": [
                    {
                      "latitude": 5.5,
                      "longitude": 6.6,
                      "speed": 50,
                      "gpsDistance": {
                        "relative": 10,
                        "absolute": 11
                      },
                      "odometerDistance": {
                        "relative": 12,
                        "absolute": 13
                      }
                    },
                    {
                      "latitude": 8.8,
                      "longitude": 9.9,
                      "speed": 85,
                      "gpsDistance": {
                        "relative": 20,
                        "absolute": 21
                      },
                      "odometerDistance": {
                        "relative": 22,
                        "absolute": 23
                      }
                    }
                  ],
                  "urbanSpeedThreshold": 60
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToRequest()
            throws Exception {
        final String givenJson = """
                {
                  "trackPoints": [
                    {
                      "latitude": 5.5,
                      "longitude": 6.6,
                      "speed": 50,
                      "gpsDistance": {
                        "relative": 10,
                        "absolute": 11
                      },
                      "odometerDistance": {
                        "relative": 12,
                        "absolute": 13
                      }
                    },
                    {
                      "latitude": 8.8,
                      "longitude": 9.9,
                      "speed": 85,
                      "gpsDistance": {
                        "relative": 20,
                        "absolute": 21
                      },
                      "odometerDistance": {
                        "relative": 22,
                        "absolute": 23
                      }
                    }
                  ],
                  "urbanSpeedThreshold": 60
                }""";

        final ClassifyDistanceRequest actual = objectMapper.readValue(givenJson, ClassifyDistanceRequest.class);
        final ClassifyDistanceRequest expected = new ClassifyDistanceRequest(
                List.of(
                        new PointRequest(
                                5.5,
                                6.6,
                                50,
                                new DistanceRequest(10., 11.),
                                new DistanceRequest(12., 13.)
                        ),
                        new PointRequest(
                                8.8,
                                9.9,
                                85,
                                new DistanceRequest(20., 21.),
                                new DistanceRequest(22., 23.)
                        )
                ),
                60
        );
        assertEquals(expected, actual);
    }

    @Test
    public void requestShouldBeValid() {
        final ClassifyDistanceRequest givenRequest = new ClassifyDistanceRequest(
                List.of(
                        new PointRequest(
                                5.5,
                                6.6,
                                50,
                                new DistanceRequest(10., 11.),
                                new DistanceRequest(12., 13.)
                        ),
                        new PointRequest(
                                8.8,
                                9.9,
                                85,
                                new DistanceRequest(20., 21.),
                                new DistanceRequest(22., 23.)
                        )
                ),
                60
        );

        final Set<ConstraintViolation<ClassifyDistanceRequest>> violations = validator.validate(givenRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfPointsIsNull() {
        final ClassifyDistanceRequest givenRequest = ClassifyDistanceRequest.builder()
                .urbanSpeedThreshold(0)
                .build();

        final Set<ConstraintViolation<ClassifyDistanceRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfPointCountIsLessThanMinimalAllowable() {
        final ClassifyDistanceRequest givenRequest = new ClassifyDistanceRequest(
                List.of(
                        new PointRequest(
                                5.5,
                                6.6,
                                50,
                                new DistanceRequest(10., 11.),
                                new DistanceRequest(12., 13.)
                        )
                ),
                60
        );

        final Set<ConstraintViolation<ClassifyDistanceRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("размер должен находиться в диапазоне от 2 до 2147483647", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfPointIsNotValid() {
        final ClassifyDistanceRequest givenRequest = new ClassifyDistanceRequest(
                List.of(
                        new PointRequest(
                                90.0000000001,
                                6.6,
                                50,
                                new DistanceRequest(10., 11.),
                                new DistanceRequest(12., 13.)
                        ),
                        new PointRequest(
                                8.8,
                                9.9,
                                85,
                                new DistanceRequest(20., 21.),
                                new DistanceRequest(22., 23.)
                        )
                ),
                60
        );

        final Set<ConstraintViolation<ClassifyDistanceRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfUrbanSpeedThresholdIsNull() {
        final ClassifyDistanceRequest givenRequest = ClassifyDistanceRequest.builder()
                .trackPoints(
                        List.of(
                                new PointRequest(
                                        5.5,
                                        6.6,
                                        50,
                                        new DistanceRequest(10., 11.),
                                        new DistanceRequest(12., 13.)
                                ),
                                new PointRequest(
                                        8.8,
                                        9.9,
                                        85,
                                        new DistanceRequest(20., 21.),
                                        new DistanceRequest(22., 23.)
                                )
                        )
                )
                .build();

        final Set<ConstraintViolation<ClassifyDistanceRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfUrbanSpeedThresholdIsNegative() {
        final ClassifyDistanceRequest givenRequest = new ClassifyDistanceRequest(
                List.of(
                        new PointRequest(
                                5.5,
                                6.6,
                                50,
                                new DistanceRequest(10., 11.),
                                new DistanceRequest(12., 13.)
                        ),
                        new PointRequest(
                                8.8,
                                9.9,
                                85,
                                new DistanceRequest(20., 21.),
                                new DistanceRequest(22., 23.)
                        )
                ),
                -1
        );

        final Set<ConstraintViolation<ClassifyDistanceRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше или равно 0", findFirstMessage(violations));
    }
}
