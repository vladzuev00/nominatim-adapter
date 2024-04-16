package by.aurorasoft.nominatim.controller.mileage;

import by.aurorasoft.nominatim.base.AbstractSpringBootTest;
import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest.RequestTrackPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.time.Instant.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class MileageRequestTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

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
        throw new RuntimeException();
    }
}
