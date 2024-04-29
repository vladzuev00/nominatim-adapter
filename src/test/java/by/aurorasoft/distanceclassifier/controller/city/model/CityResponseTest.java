package by.aurorasoft.distanceclassifier.controller.city.model;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse.CityGeometryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import static by.aurorasoft.distanceclassifier.model.CityType.CAPITAL;
import static by.aurorasoft.distanceclassifier.testutil.CityResponseUtil.checkEquals;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class CityResponseTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

    @Autowired
    private GeoJSONReader geoJSONReader;

    @Test
    public void geometryShouldBeConvertedToJson()
            throws Exception {
        final CityGeometryResponse givenGeometry = new CityGeometryResponse(
                createGeometry(
                        new Coordinate(1, 1),
                        new Coordinate(2, 1),
                        new Coordinate(2, 2),
                        new Coordinate(1, 1)
                ),
                createGeometry(
                        new Coordinate(1, 1),
                        new Coordinate(2, 1),
                        new Coordinate(2, 3),
                        new Coordinate(1, 1)
                )
        );

        final String actual = objectMapper.writeValueAsString(givenGeometry);
        final String expected = """
                {
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
                          3
                        ],
                        [
                          1,
                          1
                        ]
                      ]
                    ]
                  }
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToGeometry()
            throws Exception {
        final String givenJson = """
                {
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
                          3
                        ],
                        [
                          1,
                          1
                        ]
                      ]
                    ]
                  }
                }""";

        final CityGeometryResponse actual = objectMapper.readValue(givenJson, CityGeometryResponse.class);
        final CityGeometryResponse expected = new CityGeometryResponse(
                createGeometry(
                        new Coordinate(1, 1),
                        new Coordinate(2, 1),
                        new Coordinate(2, 2),
                        new Coordinate(1, 1)
                ),
                createGeometry(
                        new Coordinate(1, 1),
                        new Coordinate(2, 1),
                        new Coordinate(2, 3),
                        new Coordinate(1, 1)
                )
        );
        checkEquals(expected, actual, geoJSONReader);
    }

    @Test
    public void responseShouldBeConvertedToJson()
            throws Exception {
        final CityResponse givenResponse = new CityResponse(
                255L,
                "name",
                CAPITAL,
                new CityGeometryResponse(
                        createGeometry(
                                new Coordinate(1, 1),
                                new Coordinate(2, 1),
                                new Coordinate(2, 2),
                                new Coordinate(1, 1)
                        ),
                        createGeometry(
                                new Coordinate(1, 1),
                                new Coordinate(2, 1),
                                new Coordinate(2, 3),
                                new Coordinate(1, 1)
                        )
                )
        );

        final String actual = objectMapper.writeValueAsString(givenResponse);
        final String expected = """
                {
                   "id": 255,
                   "name": "name",
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
                             3
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
    }

    @Test
    public void jsonShouldBeConvertedToResponse()
            throws Exception {
        final String givenJson = """
                {
                   "id": 255,
                   "name": "name",
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
                             3
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

        final CityResponse actual = objectMapper.readValue(givenJson, CityResponse.class);
        final CityResponse expected = new CityResponse(
                255L,
                "name",
                CAPITAL,
                new CityGeometryResponse(
                        createGeometry(
                                new Coordinate(1, 1),
                                new Coordinate(2, 1),
                                new Coordinate(2, 2),
                                new Coordinate(1, 1)
                        ),
                        createGeometry(
                                new Coordinate(1, 1),
                                new Coordinate(2, 1),
                                new Coordinate(2, 3),
                                new Coordinate(1, 1)
                        )
                )
        );
        checkEquals(expected, actual, geoJSONReader);
    }

    private Geometry createGeometry(final Coordinate... coordinates) {
        return geoJSONWriter.write(geometryFactory.createPolygon(coordinates));
    }
}
