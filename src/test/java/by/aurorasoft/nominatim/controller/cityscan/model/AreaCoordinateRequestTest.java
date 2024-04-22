package by.aurorasoft.nominatim.controller.cityscan.model;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

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
}
