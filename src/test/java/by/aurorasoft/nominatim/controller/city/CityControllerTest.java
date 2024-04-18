package by.aurorasoft.nominatim.controller.city;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.controller.city.factory.CityFactory;
import by.aurorasoft.nominatim.controller.city.factory.CityResponseFactory;
import by.aurorasoft.nominatim.crud.service.CityService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class CityControllerTest extends AbstractJunitSpringBootTest {
    private static final String URL = "/api/v1/city";
    private static final String PARAM_NAME_PAGE_NUMBER = "pageNumber";
    private static final String PARAM_NAME_PAGE_SIZE = "pageSize";

    @MockBean
    private CityService mockedCityService;

    @MockBean
    private CityResponseFactory mockedResponseFactory;

    @MockBean
    private CityFactory mockedCityFactory;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void allCitiesShouldBeFound() {
        final String url = createUrlToFindAllCities(0, 2);
        throw new RuntimeException();
    }

    private static String createUrlToFindAllCities(final int pageNumber, final int pageSize) {
        return fromUriString(URL)
                .queryParam(PARAM_NAME_PAGE_NUMBER, pageNumber)
                .queryParam(PARAM_NAME_PAGE_SIZE, pageSize)
                .build()
                .toUriString();
    }
}
