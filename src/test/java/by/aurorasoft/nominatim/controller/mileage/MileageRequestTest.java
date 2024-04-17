package by.aurorasoft.nominatim.controller.mileage;

import by.aurorasoft.nominatim.base.AbstractSpringBootTest;
import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest.RequestTrackPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static by.aurorasoft.nominatim.util.ConstraintViolationUtil.findFirstMessage;
import static java.time.Instant.MAX;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class MileageRequestTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Test
    public void pointShouldBeConvertedToJson()
            throws Exception {
        final RequestTrackPoint givenPoint = new RequestTrackPoint(
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

        final RequestTrackPoint actual = objectMapper.readValue(givenJson, RequestTrackPoint.class);
        final RequestTrackPoint expected = new RequestTrackPoint(
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
        final RequestTrackPoint givenPoint = new RequestTrackPoint(
                parse("2007-12-03T10:15:30.00Z"),
                5.5F,
                6.6F,
                10,
                15,
                true
        );

        final Set<ConstraintViolation<RequestTrackPoint>> violations = validator.validate(givenPoint);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void pointShouldNotBeValidBecauseOfNullDateTime() {
        final RequestTrackPoint givenPoint = RequestTrackPoint.builder()
                .latitude(5.5F)
                .longitude(6.6F)
                .altitude(10)
                .speed(15)
                .valid(true)
                .build();

        final Set<ConstraintViolation<RequestTrackPoint>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void pointShouldNotBeValidBecauseOfFutureDateTime() {
        final RequestTrackPoint givenPoint = new RequestTrackPoint(MAX, 5.5F, 6.6F, 10, 15, true);

        final Set<ConstraintViolation<RequestTrackPoint>> violations = validator.validate(givenPoint);
        assertEquals(1, violations.size());
        assertEquals("должно содержать прошедшую дату или сегодняшнее число", findFirstMessage(violations));
    }
}
