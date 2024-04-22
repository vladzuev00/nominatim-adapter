package by.aurorasoft.nominatim.model;

import by.aurorasoft.nominatim.model.OverpassSearchCityResponse.Tags;
import org.junit.Test;

import static by.aurorasoft.nominatim.model.CityType.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class CityTypeTest {

    @Test
    public void tagsShouldMatchCapital() {
        final Tags givenTags = Tags.builder()
                .capital("yes")
                .build();

        final boolean actual = CAPITAL.match(givenTags);
        assertTrue(actual);
    }

    @Test
    public void tagsShouldNotMatchCapital() {
        final Tags givenTags = Tags.builder()
                .capital("ye")
                .build();

        final boolean actual = CAPITAL.match(givenTags);
        assertFalse(actual);
    }

    @Test
    public void tagsShouldMatchCity() {
        final Tags givenTags = Tags.builder()
                .place("city")
                .build();

        final boolean actual = CITY.match(givenTags);
        assertTrue(actual);
    }

    @Test
    public void tagsShouldNotMatchCity() {
        final Tags givenTags = Tags.builder()
                .place("cit")
                .build();

        final boolean actual = CITY.match(givenTags);
        assertFalse(actual);
    }

    @Test
    public void tagsShouldNotMatchCityBecauseOfDefinedCapital() {
        final Tags givenTags = Tags.builder()
                .capital("some-value")
                .place("city")
                .build();

        final boolean actual = CITY.match(givenTags);
        assertFalse(actual);
    }

    @Test
    public void tagsShouldMatchTown() {
        final Tags givenTags = Tags.builder()
                .place("town")
                .build();

        final boolean actual = TOWN.match(givenTags);
        assertTrue(actual);
    }

    @Test
    public void tagsShouldNotMatchTown() {
        final Tags givenTags = Tags.builder()
                .place("tow")
                .build();

        final boolean actual = TOWN.match(givenTags);
        assertFalse(actual);
    }
}
