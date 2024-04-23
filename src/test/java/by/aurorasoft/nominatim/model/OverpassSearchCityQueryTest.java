package by.aurorasoft.nominatim.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class OverpassSearchCityQueryTest {

    @Test
    public void queryShouldBePresentedAsText() {
        final OverpassSearchCityQuery givenQuery = new OverpassSearchCityQuery(
                50,
                new AreaCoordinate(
                        new Coordinate(5.5, 6.6),
                        new Coordinate(7.7, 8.8)
                )
        );

        final String actual = givenQuery.asText();
        final String expected = """
                [out:json][timeout:50];
                (
                  relation[place~"(city)|(town)"](5.5, 6.6, 7.7, 8.8);
                );
                out geom;""";
        assertEquals(expected, actual);
    }
}
