package by.aurorasoft.distanceclassifier.service.cityscan.overpass;

import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public final class OverpassSearchCityQueryFactoryTest {
    private static final int GIVEN_TIMEOUT = 50;

    private final OverpassSearchCityQueryFactory factory = new OverpassSearchCityQueryFactory(GIVEN_TIMEOUT);

    @Test
    public void queryShouldBeCreated() {
        final AreaCoordinate givenAreaCoordinate = mock(AreaCoordinate.class);

        final OverpassSearchCityQuery actual = factory.create(givenAreaCoordinate);
        final OverpassSearchCityQuery expected = new OverpassSearchCityQuery(GIVEN_TIMEOUT, givenAreaCoordinate);
        assertEquals(expected, actual);
    }
}
