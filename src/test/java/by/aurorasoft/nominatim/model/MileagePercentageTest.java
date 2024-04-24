package by.aurorasoft.nominatim.model;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class MileagePercentageTest extends AbstractJunitSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void mileagePercentageShouldBeConvertedToJson()
            throws Exception {
        final MileagePercentage givenMileagePercentage = new MileagePercentage(0.7, 0.3);

        final String actual = objectMapper.writeValueAsString(givenMileagePercentage);
        final String expected = """
                {
                  "urban": 0.7,
                  "country": 0.3
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToMileagePercentage()
            throws Exception {
        final String givenJson = """
                {
                  "urban": 0.7,
                  "country": 0.3
                }""";

        final MileagePercentage actual = objectMapper.readValue(givenJson, MileagePercentage.class);
        final MileagePercentage expected = new MileagePercentage(0.7, 0.3);
        assertEquals(expected, actual);
    }
}
