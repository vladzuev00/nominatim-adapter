package by.aurorasoft.nominatim.controller.cityscan.model;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static by.aurorasoft.nominatim.testutil.ConstraintViolationUtil.findFirstMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class AreaCoordinateRequestTest extends AbstractJunitSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Test
    public void requestShouldBeMappedToJson()
            throws Exception {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(45., 46., 47., 48.);

        final String actual = objectMapper.writeValueAsString(givenRequest);
        final String expected = """
                {
                  "minLatitude": 45,
                  "minLongitude": 46,
                  "maxLatitude": 47,
                  "maxLongitude": 48
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeMappedToRequest()
            throws Exception {
        final String givenJson = """
                {
                  "minLatitude": 45,
                  "minLongitude": 46,
                  "maxLatitude": 47,
                  "maxLongitude": 48
                }""";

        final AreaCoordinateRequest actual = objectMapper.readValue(givenJson, AreaCoordinateRequest.class);
        final AreaCoordinateRequest expected = new AreaCoordinateRequest(45., 46., 47., 48.);
        assertEquals(expected, actual);
    }

    @Test
    public void requestShouldBeValid() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(45., 46., 47., 48.);

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMinLatitudeNotDefined() {
        final AreaCoordinateRequest givenRequest = AreaCoordinateRequest.builder()
                .minLongitude(46.)
                .maxLatitude(47.)
                .maxLongitude(48.)
                .build();

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMinLatitudeIsLessThanMinimalAllowable() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(-90.1, 46., 47., 48.);

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMinLatitudeIsMoreThanMaximalAllowable() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(90.1, 46., 47., 48.);

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMinLongitudeNotDefined() {
        final AreaCoordinateRequest givenRequest = AreaCoordinateRequest.builder()
                .minLatitude(46.)
                .maxLatitude(47.)
                .maxLongitude(48.)
                .build();

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMinLongitudeIsLessThanMinimalAllowable() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(45., -180.1, 47., 48.);

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMinLongitudeIsMoreThanMaximalAllowable() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(45., 180.1, 47., 48.);

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMaxLatitudeNotDefined() {
        final AreaCoordinateRequest givenRequest = AreaCoordinateRequest.builder()
                .minLatitude(45.)
                .minLongitude(46.)
                .maxLongitude(48.)
                .build();

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMaxLatitudeIsLessThanMinimalAllowable() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(45., 46., -90.1, 48.);

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMaxLatitudeIsMoreThanMaximalAllowable() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(45., 46., 90.1, 48.);

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMaxLongitudeNotDefined() {
        final AreaCoordinateRequest givenRequest = AreaCoordinateRequest.builder()
                .minLatitude(45.)
                .minLongitude(46.)
                .maxLatitude(48.)
                .build();

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMaxLongitudeIsLessThanMinimalAllowable() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(45., 46., 47., -180.1);

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfMaxLongitudeIsMoreThanMaximalAllowable() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(45., 46., 47., 180.1);

        final Set<ConstraintViolation<AreaCoordinateRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }
}
