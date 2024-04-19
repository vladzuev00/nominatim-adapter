package by.aurorasoft.nominatim.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class OverpassTurboSearchCityQueryTest {

    @Test
    public void queryShouldBePresentedAsText() {
        final OverpassTurboSearchCityQuery givenQuery = new OverpassTurboSearchCityQuery(
                50,
                new AreaCoordinate(
                        new Coordinate(5.5F, 6.6F),
                        new Coordinate(7.7F, 8.8F)
                )
        );

        final String actual = givenQuery.asText();
        final String expected = """
                [out:json][timeout:50];
                (
                  relation["place"~"(city)|(town)"](5.5, 6.6, 7.7, 8.8);
                );
                out geom;""";
        assertEquals(expected, actual);
    }
}
