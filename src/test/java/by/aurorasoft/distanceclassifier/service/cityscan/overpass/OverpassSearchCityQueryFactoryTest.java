package by.aurorasoft.distanceclassifier.service.cityscan.overpass;

import by.aurorasoft.distanceclassifier.config.property.OverpassProperty;
import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.model.overpass.OverpassSearchCityQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class OverpassSearchCityQueryFactoryTest {

    @Mock
    private OverpassProperty mockedProperty;

    private OverpassSearchCityQueryFactory factory;

    @Before
    public void initializeFactory() {
        factory = new OverpassSearchCityQueryFactory(mockedProperty);
    }

    @Test
    public void queryShouldBeCreated() {
        final AreaCoordinate givenAreaCoordinate = mock(AreaCoordinate.class);

        final int givenTimeout = 50;
        when(mockedProperty.getTimeout()).thenReturn(givenTimeout);

        final OverpassSearchCityQuery actual = factory.create(givenAreaCoordinate);
        final OverpassSearchCityQuery expected = new OverpassSearchCityQuery(givenTimeout, givenAreaCoordinate);
        assertEquals(expected, actual);
    }
}
