package by.aurorasoft.nominatim.rest.model;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.rest.model.MileageRequest.TrackPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class MileageRequestTest extends AbstractContextTest {

    @Autowired
    private Validator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void trackPointShouldBeValid() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfDateTimeIsNull() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals("не должно равняться null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfDateTimeIsFuture() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now().plus(10, SECONDS))
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "должно содержать прошедшую дату или сегодняшнее число",
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfLatitudeIsNull() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals("не должно равняться null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfLatitudeIsLessThanMinimalAllowable() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(-90.1F)
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "должно быть больше, чем или равно -90",
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfLatitudeIsBiggerThanMaximalAllowable() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(90.1F)
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "должно быть меньше, чем или равно 90",
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfLongitudeIsNull() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(45F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals("не должно равняться null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfLongitudeIsLessThanMinimalAllowable() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(-180.1F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals("должно быть больше, чем или равно -180", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfLongitudeIsMoreThanMaximalAllowalbe() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(180.1F)
                .altitude(15)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals("должно быть меньше, чем или равно 180", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfAltitudeIsNull() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(45F)
                .speed(500)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals("не должно равняться null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfSpeedIsNull() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals("не должно равняться null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfSpeedIsLessThanMinimalAllowable() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .speed(-1)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals("должно быть не меньше 0", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfSpeedIsMoreThanMaximalAllowable() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .speed(1001)
                .valid(true)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals("должно быть не больше 1000", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackPointShouldNotBeValidBecauseOfValidIsNull() {
        final TrackPoint givenTrackPoint = TrackPoint.builder()
                .datetime(now())
                .latitude(45F)
                .longitude(46F)
                .altitude(15)
                .speed(500)
                .build();

        final Set<ConstraintViolation<TrackPoint>> constraintViolations = this.validator.validate(givenTrackPoint);
        assertEquals(1, constraintViolations.size());
        assertEquals("не должно равняться null", constraintViolations.iterator().next().getMessage());
    }
}
