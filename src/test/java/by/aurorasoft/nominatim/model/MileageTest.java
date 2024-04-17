package by.aurorasoft.nominatim.model;

import by.aurorasoft.nominatim.base.AbstractSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class MileageTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void mileageShouldBeConvertedToJson()
            throws Exception {
        final Mileage givenMileage = new Mileage(5.5, 6.6);

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

        final Mileage actual = objectMapper.readValue(givenJson, Mileage.class);
        final Mileage expected = new Mileage(5.5, 6.6);
        assertEquals(expected, actual);
    }
}