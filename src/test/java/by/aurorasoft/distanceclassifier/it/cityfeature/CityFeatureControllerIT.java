package by.aurorasoft.distanceclassifier.it.cityfeature;

import by.aurorasoft.distanceclassifier.it.base.AbstractIT;
import org.junit.Test;

import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.getExpectingOk;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class CityFeatureControllerIT extends AbstractIT {
    private static final String URL = "/api/v1/cityFeature";

    @Test
    public void featureCollectionShouldBeGot()
            throws Exception {
        final String actual = getExpectingOk(restTemplate, URL, String.class);
        final String expected = """
                {
                  "type": "FeatureCollection",
                  "features": [
                    {
                      "type": "Feature",
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
                      "properties": {
                        "id": 255,
                        "name": "First",
                        "cityType": "CAPITAL"
                      }
                    },
                    {
                      "type": "Feature",
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
                      "properties": {
                        "name": "Second",
                        "cityType": "CITY",
                        "id": 256
                      }
                    },
                    {
                      "type": "Feature",
                      "geometry": {
                        "type": "Polygon",
                        "coordinates": [
                          [
                            [
                              8,
                              3
                            ],
                            [
                              8,
                              6
                            ],
                            [
                              11,
                              6
                            ],
                            [
                              11,
                              3
                            ],
                            [
                              8,
                              3
                            ]
                          ]
                        ]
                      },
                      "properties": {
                        "name": "Third",
                        "cityType": "TOWN",
                        "id": 257
                      }
                    },
                    {
                      "type": "Feature",
                      "geometry": {
                        "type": "Polygon",
                        "coordinates": [
                          [
                            [
                              11,
                              9.5
                            ],
                            [
                              11,
                              12
                            ],
                            [
                              13,
                              12
                            ],
                            [
                              13,
                              9.5
                            ],
                            [
                              11,
                              9.5
                            ]
                          ]
                        ]
                      },
                      "properties": {
                        "name": "Fourth",
                        "cityType": "TOWN",
                        "id": 258
                      }
                    }
                  ]
                }""";
        assertEquals(expected, actual, true);
    }
}
