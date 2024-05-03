package by.aurorasoft.distanceclassifier.it.citycrud;

import by.aurorasoft.distanceclassifier.controller.city.model.CityRequest;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity.CityGeometry;
import by.aurorasoft.distanceclassifier.it.base.AbstractIT;
import by.aurorasoft.distanceclassifier.model.CityType;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

import static by.aurorasoft.distanceclassifier.model.CityType.TOWN;
import static by.aurorasoft.distanceclassifier.testutil.CityEntityUtil.checkEquals;
import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public final class CityCrudIT extends AbstractIT {
    private static final String URL = "/api/v1/city";
    private static final String PARAM_NAME_PAGE_NUMBER = "pageNumber";
    private static final String PARAM_NAME_PAGE_SIZE = "pageSize";

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

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
                                2,
                                3
                              ],
                              [
                                5,
                                2.5
                              ],
                              [
                                5,
                                6
                              ],
                              [
                                3,
                                5
                              ],
                              [
                                2,
                                3
                              ]
                            ]
                          ]
                        },
                        "boundingBox": {
                          "type": "Polygon",
                          "coordinates": [
                            [
                              [
                                2,
                                2.5
                              ],
                              [
                                5,
                                2.5
                              ],
                              [
                                5,
                                6
                              ],
                              [
                                2,
                                6
                              ],
                              [
                                2,
                                2.5
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
                                7.5,
                                4
                              ],
                              [
                                8,
                                4
                              ],
                              [
                                10.5,
                                5
                              ],
                              [
                                11.5,
                                7.5
                              ],
                              [
                                8.5,
                                6.5
                              ],
                              [
                                7.5,
                                4
                              ]
                            ]
                          ]
                        },
                        "boundingBox": {
                          "type": "Polygon",
                          "coordinates": [
                            [
                              [
                                7.5,
                                4
                              ],
                              [
                                11.5,
                                4
                              ],
                              [
                                11.5,
                                7.5
                              ],
                              [
                                7.5,
                                7.5
                              ],
                              [
                                7.5,
                                4
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
                      "sorted": true,
                      "unsorted": false
                    },
                    "offset": 0,
                    "pageSize": 2,
                    "pageNumber": 0,
                    "paged": true,
                    "unpaged": false
                  },
                  "last": false,
                  "totalPages": 2,
                  "totalElements": 3,
                  "size": 2,
                  "number": 0,
                  "sort": {
                    "empty": false,
                    "sorted": true,
                    "unsorted": false
                  },
                  "first": true,
                  "numberOfElements": 2,
                  "empty": false
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void cityShouldBeSaved()
            throws Exception {
        final String givenName = "name";
        final String givenGeometryText = "POLYGON((1 1, 2 1, 2 2, 1 1))";
        final CityType givenType = TOWN;
        final CityRequest givenRequest = new CityRequest(givenName, createWololoPolygon(givenGeometryText), givenType);

        final String actual = postExpectingOk(restTemplate, URL, givenRequest, String.class);
        final String expected = """
                {
                   "id": 1,
                   "name": "name",
                   "type": "TOWN",
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
                             1,
                             2
                           ],
                           [
                             2,
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
                }""";
        assertEquals(expected, actual, true);

        final Long expectedId = 1L;
        final CityEntity actualCity = findCity(expectedId);
        final CityEntity expectedCity = CityEntity.builder()
                .id(expectedId)
                .name(givenName)
                .type(givenType)
                .geometry(new CityGeometry(createPolygon(givenGeometryText), createPolygon("POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))")))
                .build();
        checkEquals(expectedCity, actualCity);
    }

    @Test
    public void cityShouldBeUpdated()
            throws Exception {
        final Long givenId = 255L;
        final String givenName = "new-name";
        final String givenGeometryText = "POLYGON((1 1, 2 1, 2 2, 1 1))";
        final CityType givenType = TOWN;
        final CityRequest givenRequest = new CityRequest(givenName, createWololoPolygon(givenGeometryText), givenType);

        final String url = createUrlWithPathVariable(givenId);
        final String actual = putExpectingOk(restTemplate, url, givenRequest, String.class);
        final String expected = """
                {
                   "id": 255,
                   "name": "new-name",
                   "type": "TOWN",
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
                             1,
                             2
                           ],
                           [
                             2,
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
                }""";
        assertEquals(expected, actual, true);

        final CityEntity actualCity = findCity(givenId);
        final CityEntity expectedCity = CityEntity.builder()
                .id(givenId)
                .name(givenName)
                .type(givenType)
                .geometry(new CityGeometry(createPolygon(givenGeometryText), createPolygon("POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))")))
                .build();
        checkEquals(expectedCity, actualCity);
    }

    @Test
    public void cityShouldBeDeleted() {
        final Long givenId = 255L;

        final String url = createUrlWithPathVariable(givenId);
        deleteExpectingNoContent(restTemplate, url);

        assertFalse(isCityExist(givenId));
    }

    @SuppressWarnings("SameParameterValue")
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

    @SuppressWarnings("SameParameterValue")
    private Geometry createWololoPolygon(final String text) {
        final Polygon polygon = createPolygon(text);
        return geoJSONWriter.write(polygon);
    }

    private CityEntity findCity(final Long id) {
        return entityManager.find(CityEntity.class, id);
    }

    private boolean isCityExist(final Long id) {
        return findCity(id) != null;
    }

    private static String createUrlWithPathVariable(final Object value) {
        return URL + "/" + value;
    }
}
