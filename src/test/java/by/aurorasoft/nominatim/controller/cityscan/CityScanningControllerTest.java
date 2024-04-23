package by.aurorasoft.nominatim.controller.cityscan;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.controller.cityscan.factory.AreaCoordinateFactory;
import by.aurorasoft.nominatim.controller.cityscan.model.AreaCoordinateRequest;
import by.aurorasoft.nominatim.model.AreaCoordinate;
import by.aurorasoft.nominatim.service.cityscan.CityScanningService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static by.aurorasoft.nominatim.testutil.HttpUtil.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class CityScanningControllerTest extends AbstractJunitSpringBootTest {
    private static final String URL = "/api/v1/cityScan";

    @MockBean
    private AreaCoordinateFactory mockedAreaCoordinateFactory;

    @MockBean
    private CityScanningService mockedService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void citiesShouldBeScanned() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(5.5, 6.6, 7.7, 8.8);

        final AreaCoordinate givenAreaCoordinate = mock(AreaCoordinate.class);
        when(mockedAreaCoordinateFactory.create(eq(givenRequest))).thenReturn(givenAreaCoordinate);

        scanExpectingNoContent(givenRequest);

        verify(mockedService, times(1)).scan(same(givenAreaCoordinate));
    }

    @Test
    public void citiesShouldNotBeScannedBecauseOfRequestNotValid()
            throws Exception {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(-90.1, 6.6, 7.7, 8.8);

        final String actual = scanExpectingNotAcceptable(givenRequest);
        final String expected = """
                {
                  "httpStatus": "NOT_ACCEPTABLE",
                  "message": "minLatitude : Invalid latitude",
                  "dateTime": "2024-04-23 07-57-45"
                }""";
        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);

        verifyNoInteractions(mockedAreaCoordinateFactory, mockedService);
    }

    private void scanExpectingNoContent(final AreaCoordinateRequest request) {
        postExpectingNoContext(restTemplate, URL, request);
    }

    private String scanExpectingNotAcceptable(final AreaCoordinateRequest request) {
        return postExpectingNotAcceptable(restTemplate, URL, request, String.class);
    }
}
