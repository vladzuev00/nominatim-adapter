package by.aurorasoft.nominatim.controller.city;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.controller.city.factory.CityFactory;
import by.aurorasoft.nominatim.controller.city.factory.CityResponseFactory;
import by.aurorasoft.nominatim.controller.city.model.CityRequest;
import by.aurorasoft.nominatim.controller.city.model.CityResponse;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.testutil.GeometryUtil;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.List;

import static by.aurorasoft.nominatim.model.CityType.*;
import static by.aurorasoft.nominatim.testutil.HttpUtil.*;
import static by.aurorasoft.nominatim.util.PageRequestUtil.createRequestSortingById;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
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

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

    @Test
    public void allCitiesShouldBeFound()
            throws Exception {
        final int givenPageNumber = 0;
        final int givenPageSize = 2;
        final String url = createUrlToFindAllCities(givenPageNumber, givenPageSize);

        final PageRequest expectedRequest = createRequestSortingById(givenPageNumber, givenPageSize);
        final City firstGivenCity = City.builder()
                .id(255L)
                .name("first-city")
                .geometry(createPolygon("POLYGON((1 1, 2 1, 2 2, 1 1))"))
                .type(CAPITAL)
                .build();
        final City secondGivenCity = City.builder()
                .id(256L)
                .name("second-city")
                .geometry(createPolygon("POLYGON((1 1, 3 1, 3 2, 1 1))"))
                .type(CITY)
                .build();
        final Page<City> givenPage = new PageImpl<>(List.of(firstGivenCity, secondGivenCity));
        when(mockedCityService.findAll(eq(expectedRequest))).thenReturn(givenPage);

        bindResponse(firstGivenCity);
        bindResponse(secondGivenCity);

        final String actual = getExpectingOk(restTemplate, url, String.class);
        final String expected = """
                {
                  "content": [
                    {
                      "id": 255,
                      "name": "first-city",
                      "geometry": {
                        "type": "Polygon",
                        "coordinates": [
                          [
                            [
                              1,
                              1
                            ],
                            [
                              2,
                              1
                            ],
                            [
                              2,
                              2
                            ],
                            [
                              1,
                              1
                            ]
                          ]
                        ]
                      },
                      "type": "CAPITAL"
                    },
                    {
                      "id": 256,
                      "name": "second-city",
                      "geometry": {
                        "type": "Polygon",
                        "coordinates": [
                          [
                            [
                              1,
                              1
                            ],
                            [
                              3,
                              1
                            ],
                            [
                              3,
                              2
                            ],
                            [
                              1,
                              1
                            ]
                          ]
                        ]
                      },
                      "type": "CITY"
                    }
                  ],
                  "pageable": "INSTANCE",
                  "totalElements": 2,
                  "totalPages": 1,
                  "last": true,
                  "size": 2,
                  "number": 0,
                  "sort": {
                    "empty": true,
                    "unsorted": true,
                    "sorted": false
                  },
                  "numberOfElements": 2,
                  "first": true,
                  "empty": false
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void allCitiesShouldNotBeFoundBecauseOfPageNumberIsLessThanMinimalAllowable()
            throws Exception {
        final String url = createUrlToFindAllCities(-1, 2);

        final String actual = getExpectingNotAcceptable(restTemplate, url, String.class);
        final String expected = """
                {
                  "status": "NOT_ACCEPTABLE",
                  "message": "findAll.pageNumber: должно быть не меньше 0",
                  "dateTime": "2024-04-24 10-05-00"
                }""";
        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void allCitiesShouldNotBeFoundBecauseOfPageSizeIsThanMinimalAllowable()
            throws Exception {
        final String url = createUrlToFindAllCities(0, 0);

        final String actual = getExpectingNotAcceptable(restTemplate, url, String.class);
        final String expected = """
                {
                  "status": "NOT_ACCEPTABLE",
                  "message": "findAll.pageSize: должно быть не меньше 1",
                  "dateTime": "2024-04-24 10-05-00"
                }""";
        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void cityShouldBeSaved()
            throws Exception {
        final CityRequest givenRequest = new CityRequest(
                "name",
                createWololoPolygon("POLYGON((1 1, 2 1, 2 2, 1 1))"),
                TOWN
        );

        final City givenCity = mock(City.class);
        when(mockedCityFactory.create(any(CityRequest.class))).thenReturn(givenCity);

        final City givenSavedCity = City.builder()
                .id(255L)
                .name("name")
                .geometry(createPolygon("POLYGON((1 1, 2 1, 2 2, 1 1))"))
                .type(TOWN)
                .build();
        when(mockedCityService.save(same(givenCity))).thenReturn(givenSavedCity);

        bindResponse(givenSavedCity);

        final String actual = postExpectingOk(restTemplate, URL, givenRequest, String.class);
        final String expected = """
                {
                  "id": 255,
                  "name": "name",
                  "geometry": {
                    "type": "Polygon",
                    "coordinates": [
                      [
                        [
                          1,
                          1
                        ],
                        [
                          2,
                          1
                        ],
                        [
                          2,
                          2
                        ],
                        [
                          1,
                          1
                        ]
                      ]
                    ]
                  },
                  "type": "TOWN"
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void cityShouldNotBeSavedBecauseOfNotValidRequest()
            throws Exception {
        final CityRequest givenRequest = new CityRequest(
                "    ",
                createWololoPolygon("POLYGON((1 1, 2 1, 2 2, 1 1))"),
                TOWN
        );

        final String actual = postExpectingNotAcceptable(restTemplate, URL, givenRequest, String.class);
        final String expected = """
                {
                  "status": "NOT_ACCEPTABLE",
                  "message": "name : не должно быть пустым",
                  "dateTime": "2024-04-24 10-37-17"
                }""";
        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    private static String createUrlToFindAllCities(final int pageNumber, final int pageSize) {
        return fromUriString(URL)
                .queryParam(PARAM_NAME_PAGE_NUMBER, pageNumber)
                .queryParam(PARAM_NAME_PAGE_SIZE, pageSize)
                .build()
                .toUriString();
    }

    private Polygon createPolygon(final String text) {
        return GeometryUtil.createPolygon(text, geometryFactory);
    }

    private Geometry createWololoPolygon(final String text) {
        final Polygon polygon = GeometryUtil.createPolygon(text, geometryFactory);
        return geoJSONWriter.write(polygon);
    }

    private void bindResponse(final City city) {
        final CityResponse response = createResponse(city);
        when(mockedResponseFactory.create(eq(city))).thenReturn(response);
    }

    private CityResponse createResponse(final City city) {
        final Geometry geometry = geoJSONWriter.write(city.getGeometry());
        return new CityResponse(city.getId(), city.getName(), geometry, city.getType());
    }
}
