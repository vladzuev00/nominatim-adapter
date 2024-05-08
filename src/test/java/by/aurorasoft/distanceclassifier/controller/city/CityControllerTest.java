package by.aurorasoft.distanceclassifier.controller.city;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.controller.city.factory.CityResponseFactory;
import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse;
import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse.CityGeometryResponse;
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
import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.*;
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

        mockResponseFor(firstGivenCity);
        mockResponseFor(secondGivenCity);

        final String actual = getExpectingOk(restTemplate, url, String.class);
        final String expected = """
                {
                   "content": [
                     {
                       "id": 255,
                       "name": "first-city",
                       "type": "CAPITAL",
                       "geometry": {
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
                         "boundingBox": {
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
                                 2
                               ],
                               [
                                 1,
                                 1
                               ]
                             ]
                           ]
                         }
                       }
                     },
                     {
                       "id": 256,
                       "name": "second-city",
                       "type": "CITY",
                       "geometry": {
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
                         "boundingBox": {
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
                                 2,
                                 1
                               ],
                               [
                                 1,
                                 1
                               ]
                             ]
                           ]
                         }
                       }
                     }
                   ],
                   "pageable": "INSTANCE",
                   "last": true,
                   "totalElements": 2,
                   "totalPages": 1,
                   "size": 2,
                   "number": 0,
                   "sort": {
                     "empty": true,
                     "sorted": false,
                     "unsorted": true
                   },
                   "first": true,
                   "numberOfElements": 2,
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

        verifyNoInteractions(mockedCityService, mockedResponseFactory);
    }

    @Test
    public void allCitiesShouldNotBeFoundBecauseOfPageSizeIsLessThanMinimalAllowable()
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

        verifyNoInteractions(mockedCityService, mockedResponseFactory);
    }

    private String createUrlToFindAllCities(final int pageNumber, final int pageSize) {
        return fromUriString(URL)
                .queryParam(PARAM_NAME_PAGE_NUMBER, pageNumber)
                .queryParam(PARAM_NAME_PAGE_SIZE, pageSize)
                .build()
                .toUriString();
    }

    private Polygon createPolygon(final String text) {
        return GeometryUtil.createPolygon(text, geometryFactory);
    }

    private void mockResponseFor(final City city) {
        final CityResponse response = createResponse(city);
        when(mockedResponseFactory.create(eq(city))).thenReturn(response);
    }

    private CityResponse createResponse(final City city) {
        return new CityResponse(
                city.getId(),
                city.getName(),
                city.getType(),
                createGeometryResponse(city.getGeometry())
        );
    }

    private CityGeometryResponse createGeometryResponse(final CityGeometry source) {
        final Geometry geometry = geoJSONWriter.write(source.getGeometry());
        final Geometry boundingBox = geoJSONWriter.write(source.getBoundingBox());
        return new CityGeometryResponse(geometry, boundingBox);
    }
}
