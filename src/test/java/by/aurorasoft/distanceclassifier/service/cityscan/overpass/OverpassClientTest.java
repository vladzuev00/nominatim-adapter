package by.aurorasoft.distanceclassifier.service.cityscan.overpass;

import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityQuery;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import static by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassClient.URL;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class OverpassClientTest {

    @Mock
    private OverpassSearchCityQueryFactory mockedQueryFactory;

    @Mock
    private RestTemplate mockedRestTemplate;

    private OverpassClient client;

    @Before
    public void initializeClient() {
        client = new OverpassClient(mockedQueryFactory, mockedRestTemplate);
    }

    @Test
    public void citiesShouldBeFound() {
        final AreaCoordinate givenAreaCoordinate = mock(AreaCoordinate.class);

        final String givenQueryText = "query-text";
        final OverpassSearchCityQuery givenQuery = createQuery(givenQueryText);
        when(mockedQueryFactory.create(same(givenAreaCoordinate))).thenReturn(givenQuery);

        final HttpEntity<String> expectedHttpEntity = new HttpEntity<>(givenQueryText);
        final OverpassSearchCityResponse givenResponse = mock(OverpassSearchCityResponse.class);
        when(mockedRestTemplate.postForObject(same(URL), eq(expectedHttpEntity), same(OverpassSearchCityResponse.class)))
                .thenReturn(givenResponse);

        final OverpassSearchCityResponse actual = client.findCities(givenAreaCoordinate);
        assertSame(givenResponse, actual);
    }

    @SuppressWarnings("SameParameterValue")
    private static OverpassSearchCityQuery createQuery(final String text) {
        final OverpassSearchCityQuery query = mock(OverpassSearchCityQuery.class);
        when(query.asText()).thenReturn(text);
        return query;
    }
}
