package by.aurorasoft.distanceclassifier.it.citycontroller;

import by.aurorasoft.distanceclassifier.it.base.AbstractIT;
import org.junit.Test;

import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.getExpectingOk;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public final class CityControllerIT extends AbstractIT {
    private static final String URL = "/api/v1/city";
    private static final String PARAM_NAME_PAGE_NUMBER = "pageNumber";
    private static final String PARAM_NAME_PAGE_SIZE = "pageSize";

    @Test
    public void allCitiesShouldBeFound()
            throws Exception {
        final String url = createUrlToFindAllCities(0, 2);
        final String actual = getExpectingOk(restTemplate, url, String.class);
        final String expected = """
                {
                   "content": [
                     {
                       "id": 255,
                       "name": "First",
                       "type": "CAPITAL",
                       "geometry": {
                         "geometry": {
                           "type": "Polygon",
                           "coordinates": [
                             [
                               [
                                 3,
                                 2
                               ],
                               [
                                 2.5,
                                 5
                               ],
                               [
                                 6,
                                 5
                               ],
                               [
                                 5,
                                 3
                               ],
                               [
                                 3,
                                 2
                               ]
                             ]
                           ]
                         },
                         "boundingBox": {
                           "type": "Polygon",
                           "coordinates": [
                             [
                               [
                                 2.5,
                                 2
                               ],
                               [
                                 2.5,
                                 5
                               ],
                               [
                                 6,
                                 5
                               ],
                               [
                                 6,
                                 2
                               ],
                               [
                                 2.5,
                                 2
                               ]
                             ]
                           ]
                         }
                       }
                     },
                     {
                       "id": 256,
                       "name": "Second",
                       "type": "CITY",
                       "geometry": {
                         "geometry": {
                           "type": "Polygon",
                           "coordinates": [
                             [
                               [
                                 4,
                                 7.5
                               ],
                               [
                                 4,
                                 8
                               ],
                               [
                                 5,
                                 10.5
                               ],
                               [
                                 7.5,
                                 11.5
                               ],
                               [
                                 6.5,
                                 8.5
                               ],
                               [
                                 4,
                                 7.5
                               ]
                             ]
                           ]
                         },
                         "boundingBox": {
                           "type": "Polygon",
                           "coordinates": [
                             [
                               [
                                 4,
                                 7.5
                               ],
                               [
                                 4,
                                 11.5
                               ],
                               [
                                 7.5,
                                 11.5
                               ],
                               [
                                 7.5,
                                 7.5
                               ],
                               [
                                 4,
                                 7.5
                               ]
                             ]
                           ]
                         }
                       }
                     }
                   ],
                   "pageable": {
                     "sort": {
                       "empty": false,
                       "unsorted": false,
                       "sorted": true
                     },
                     "offset": 0,
                     "pageSize": 2,
                     "pageNumber": 0,
                     "unpaged": false,
                     "paged": true
                   },
                   "last": false,
                   "totalPages": 2,
                   "totalElements": 4,
                   "size": 2,
                   "number": 0,
                   "sort": {
                     "empty": false,
                     "unsorted": false,
                     "sorted": true
                   },
                   "first": true,
                   "numberOfElements": 2,
                   "empty": false
                }""";
        assertEquals(expected, actual, true);
    }

    @SuppressWarnings("SameParameterValue")
    private String createUrlToFindAllCities(final int pageNumber, final int pageSize) {
        return fromUriString(URL)
                .queryParam(PARAM_NAME_PAGE_NUMBER, pageNumber)
                .queryParam(PARAM_NAME_PAGE_SIZE, pageSize)
                .build()
                .toUriString();
    }
}
