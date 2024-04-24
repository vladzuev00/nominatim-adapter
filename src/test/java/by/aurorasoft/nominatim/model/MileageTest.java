package by.aurorasoft.nominatim.model;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class MileageTest extends AbstractJunitSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void mileageShouldBeConvertedToJson()
            throws Exception {
        final MileagePercentage givenMileage = new MileagePercentage(5.5, 6.6);

        final String actual = objectMapper.writeValueAsString(givenMileage);
        final String expected = """
                {
                  "urban": 5.5,
                  "country": 6.6
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToMileage()
            throws Exception {
        final String givenJson = """
                {
                  "urban": 5.5,
                  "country": 6.6
                }""";

        final MileagePercentage actual = objectMapper.readValue(givenJson, MileagePercentage.class);
        final MileagePercentage expected = new MileagePercentage(5.5, 6.6);
        assertEquals(expected, actual);
    }
}
