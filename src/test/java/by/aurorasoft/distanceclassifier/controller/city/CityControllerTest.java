package by.aurorasoft.distanceclassifier.controller.city;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.controller.city.factory.CityFactory;
import by.aurorasoft.distanceclassifier.controller.city.factory.CityResponseFactory;
import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse;
import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
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

import static by.aurorasoft.distanceclassifier.model.CityType.CAPITAL;
import static by.aurorasoft.distanceclassifier.model.CityType.CITY;
import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.getExpectingOk;
import static by.aurorasoft.distanceclassifier.util.PageRequestUtil.createRequestSortingById;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class CityControllerTest extends AbstractSpringBootTest {
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
                .type(CAPITAL)
                .geometry(new CityGeometry(createPolygon("POLYGON((1 1, 2 1, 2 2, 1 1))"), createPolygon("POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))")))
                .build();
        final City secondGivenCity = City.builder()
                .id(256L)
                .name("second-city")
                .type(CITY)
                .geometry(new CityGeometry(createPolygon("POLYGON((1 1, 3 1, 3 2, 1 1))"), createPolygon("POLYGON((1 1, 3 1, 3 2, 2 1, 1 1))")))
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

        verifyNoInteractions(mockedCityFactory);
    }

//    @Test
//    public void allCitiesShouldNotBeFoundBecauseOfPageNumberIsLessThanMinimalAllowable()
//            throws Exception {
//        final String url = createUrlToFindAllCities(-1, 2);
//
//        final String actual = getExpectingNotAcceptable(restTemplate, url, String.class);
//        final String expected = """
//                {
//                  "status": "NOT_ACCEPTABLE",
//                  "message": "findAll.pageNumber: должно быть не меньше 0",
//                  "dateTime": "2024-04-24 10-05-00"
//                }""";
//        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
//
//        verifyNoInteractions(mockedCityService, mockedResponseFactory, mockedCityFactory);
//    }
//
//    @Test
//    public void allCitiesShouldNotBeFoundBecauseOfPageSizeIsThanMinimalAllowable()
//            throws Exception {
//        final String url = createUrlToFindAllCities(0, 0);
//
//        final String actual = getExpectingNotAcceptable(restTemplate, url, String.class);
//        final String expected = """
//                {
//                  "status": "NOT_ACCEPTABLE",
//                  "message": "findAll.pageSize: должно быть не меньше 1",
//                  "dateTime": "2024-04-24 10-05-00"
//                }""";
//        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
//
//        verifyNoInteractions(mockedCityService, mockedResponseFactory, mockedCityFactory);
//    }
//
//    @Test
//    public void cityShouldBeSaved()
//            throws Exception {
//        final String givenName = "name";
//        final String givenGeometryText = "POLYGON((1 1, 2 1, 2 2, 1 1))";
//        final CityType givenType = TOWN;
//        final CityRequest givenRequest = new CityRequest(givenName, createWololoPolygon(givenGeometryText), givenType);
//
//        final City givenCity = mock(City.class);
//        when(mockedCityFactory.create(any(CityRequest.class))).thenReturn(givenCity);
//
//        final City givenSavedCity = City.builder()
//                .id(255L)
//                .name(givenName)
//                .geometry(createPolygon(givenGeometryText))
//                .type(givenType)
//                .build();
//        when(mockedCityService.save(same(givenCity))).thenReturn(givenSavedCity);
//
//        bindResponse(givenSavedCity);
//
//        final String actual = postExpectingOk(restTemplate, URL, givenRequest, String.class);
//        final String expected = """
//                {
//                  "id": 255,
//                  "name": "name",
//                  "geometry": {
//                    "type": "Polygon",
//                    "coordinates": [
//                      [
//                        [
//                          1,
//                          1
//                        ],
//                        [
//                          2,
//                          1
//                        ],
//                        [
//                          2,
//                          2
//                        ],
//                        [
//                          1,
//                          1
//                        ]
//                      ]
//                    ]
//                  },
//                  "type": "TOWN"
//                }""";
//        assertEquals(expected, actual, true);
//    }
//
//    @Test
//    public void cityShouldNotBeSavedBecauseOfNotValidRequest()
//            throws Exception {
//        final CityRequest givenRequest = new CityRequest(
//                "    ",
//                createWololoPolygon("POLYGON((1 1, 2 1, 2 3, 1 1))"),
//                TOWN
//        );
//
//        final String actual = postExpectingNotAcceptable(restTemplate, URL, givenRequest, String.class);
//        final String expected = """
//                {
//                  "status": "NOT_ACCEPTABLE",
//                  "message": "name : не должно быть пустым",
//                  "dateTime": "2024-04-24 10-37-17"
//                }""";
//        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
//
//        verifyNoInteractions(mockedCityService, mockedResponseFactory, mockedCityFactory);
//    }
//
//    @Test
//    public void cityShouldBeUpdated()
//            throws Exception {
//        final Long givenId = 255L;
//        final String givenName = "name";
//        final String givenGeometryText = "POLYGON((1 1, 2 1, 2 2, 1 1))";
//        final CityType givenType = TOWN;
//        final CityRequest givenRequest = new CityRequest(givenName, createWololoPolygon(givenGeometryText), givenType);
//
//        final City givenCity = mock(City.class);
//        when(mockedCityFactory.create(eq(givenId), any(CityRequest.class))).thenReturn(givenCity);
//
//        final City givenUpdatedCity = City.builder()
//                .id(givenId)
//                .name(givenName)
//                .geometry(createPolygon(givenGeometryText))
//                .type(givenType)
//                .build();
//        when(mockedCityService.update(same(givenCity))).thenReturn(givenUpdatedCity);
//
//        bindResponse(givenUpdatedCity);
//
//        final String url = createUrlWithPathVariable(givenId);
//        final String actual = putExpectingOk(restTemplate, url, givenRequest, String.class);
//        final String expected = """
//                {
//                  "id": 255,
//                  "name": "name",
//                  "geometry": {
//                    "type": "Polygon",
//                    "coordinates": [
//                      [
//                        [
//                          1,
//                          1
//                        ],
//                        [
//                          2,
//                          1
//                        ],
//                        [
//                          2,
//                          2
//                        ],
//                        [
//                          1,
//                          1
//                        ]
//                      ]
//                    ]
//                  },
//                  "type": "TOWN"
//                }""";
//        assertEquals(expected, actual, true);
//    }
//
//    @Test
//    public void cityShouldNotBeUpdatedBecauseOfNotValidRequest()
//            throws Exception {
//        final CityRequest givenRequest = new CityRequest(
//                "   ",
//                createWololoPolygon("POLYGON((1 1, 2 1, 2 2, 1 1))"),
//                TOWN
//        );
//
//        final String url = createUrlWithPathVariable(255L);
//        final String actual = putExpectingNotAcceptable(restTemplate, url, givenRequest, String.class);
//        final String expected = """
//                {
//                  "status": "NOT_ACCEPTABLE",
//                  "message": "name : не должно быть пустым",
//                  "dateTime": "2024-04-24 12-13-19"
//                }""";
//        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
//
//        verifyNoInteractions(mockedCityService, mockedResponseFactory, mockedCityFactory);
//    }
//
//    @Test
//    public void cityShouldBeDeleted() {
//        final Long givenId = 255L;
//
//        final String url = createUrlWithPathVariable(givenId);
//        deleteExpectingNoContent(restTemplate, url);
//
//        verify(mockedCityService, times(1)).delete(eq(givenId));
//        verifyNoInteractions(mockedResponseFactory, mockedCityFactory);
//    }

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
        return null;
//        final Geometry geometry = geoJSONWriter.write(city.getGeometry().getGeometry());
//        return new CityResponse(city.getId(), city.getName(), geometry, city.getType());
    }

    private static String createUrlWithPathVariable(final Object value) {
        return URL + "/" + value;
    }
}
