package by.aurorasoft.nominatim.model;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Bounds;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Coordinate;
import static org.junit.Assert.assertEquals;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class OverpassTurboSearchCityResponseTest extends AbstractJunitSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void boundsShouldBeConvertedToJson()
            throws Exception {
        final Bounds givenBounds = new Bounds(5.5, 6.6, 7.7, 8.8);

        final String actual = objectMapper.writeValueAsString(givenBounds);
        final String expected = """
                {
                  "minLatitude": 5.5,
                  "minLongitude": 6.6,
                  "maxLatitude": 7.7,
                  "maxLongitude": 8.8
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToBounds()
            throws Exception {
        final String givenJson = """
                {
                  "minlat": 51.9708119,
                  "minlon": 26.9705769,
                  "maxlat": 52.0066038,
                  "maxlon": 27.0139200
                }""";

        final Bounds actual = objectMapper.readValue(givenJson, Bounds.class);
        final Bounds expected = new Bounds(51.9708119, 26.9705769, 52.0066038, 27.0139200);
        assertEquals(expected, actual);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToBoundsBecauseOfMinLatitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "minlon": 26.9705769,
                  "maxlat": 52.0066038,
                  "maxlon": 27.0139200
                }""";

        objectMapper.readValue(givenJson, Bounds.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToBoundsBecauseOfMinLongitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "minlat": 51.9708119,
                  "maxlat": 52.0066038,
                  "maxlon": 27.0139200
                }""";

        objectMapper.readValue(givenJson, Bounds.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToBoundsBecauseOfMaxLatitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "minlat": 51.9708119,
                  "minlon": 26.9705769,
                  "maxlon": 27.0139200
                }""";

        objectMapper.readValue(givenJson, Bounds.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToBoundsBecauseOfMaxLongitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "minlat": 51.9708119,
                  "minlon": 26.9705769,
                  "maxlat": 52.0066038
                }""";

        objectMapper.readValue(givenJson, Bounds.class);
    }

    @Test
    public void coordinateShouldBeMappedToJson()
            throws Exception {
        final Coordinate givenCoordinate = new Coordinate(5.5, 6.6);

        final String actual = objectMapper.writeValueAsString(givenCoordinate);
        final String expected = """
                {
                  "latitude": 5.5,
                  "longitude": 6.6
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToCoordinate()
            throws Exception {
        final String givenJson = """
                {
                  "lat": 5.5,
                  "lon": 6.6
                }""";

        final Coordinate actual = objectMapper.readValue(givenJson, Coordinate.class);
        final Coordinate expected = new Coordinate(5.5, 6.6);
        assertEquals(expected, actual);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToCoordinateBecauseOfLatitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "lon": 6.6
                }""";

        objectMapper.readValue(givenJson, Coordinate.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToCoordinateBecauseOfLongitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "lat": 5.5
                }""";

        objectMapper.readValue(givenJson, Coordinate.class);
    }
}
