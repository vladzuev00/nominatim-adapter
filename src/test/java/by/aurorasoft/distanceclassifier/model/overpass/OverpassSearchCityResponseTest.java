package by.aurorasoft.distanceclassifier.model.overpass;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static by.aurorasoft.distanceclassifier.model.overpass.OverpassSearchCityResponse.Coordinate;
import static by.aurorasoft.distanceclassifier.model.overpass.OverpassSearchCityResponse.*;
import static org.junit.Assert.assertEquals;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class OverpassSearchCityResponseTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void boundsShouldBeConvertedToJson()
            throws Exception {
        final Bounds givenBounds = new Bounds(5.5, 6.6, 7.7, 8.8);

        final String actual = objectMapper.writeValueAsString(givenBounds);
        final String expected = """
                {
                  "minLatitude": 5.5,
                  "minLongitude": 6.6,
                  "maxLatitude": 7.7,
                  "maxLongitude": 8.8
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToBounds()
            throws Exception {
        final String givenJson = """
                {
                  "minlat": 51.9708119,
                  "minlon": 26.9705769,
                  "maxlat": 52.0066038,
                  "maxlon": 27.0139200
                }""";

        final Bounds actual = objectMapper.readValue(givenJson, Bounds.class);
        final Bounds expected = new Bounds(51.9708119, 26.9705769, 52.0066038, 27.0139200);
        assertEquals(expected, actual);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToBoundsBecauseOfMinLatitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "minlon": 26.9705769,
                  "maxlat": 52.0066038,
                  "maxlon": 27.0139200
                }""";

        objectMapper.readValue(givenJson, Bounds.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToBoundsBecauseOfMinLongitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "minlat": 51.9708119,
                  "maxlat": 52.0066038,
                  "maxlon": 27.0139200
                }""";

        objectMapper.readValue(givenJson, Bounds.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToBoundsBecauseOfMaxLatitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "minlat": 51.9708119,
                  "minlon": 26.9705769,
                  "maxlon": 27.0139200
                }""";

        objectMapper.readValue(givenJson, Bounds.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToBoundsBecauseOfMaxLongitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "minlat": 51.9708119,
                  "minlon": 26.9705769,
                  "maxlat": 52.0066038
                }""";

        objectMapper.readValue(givenJson, Bounds.class);
    }

    @Test
    public void coordinateShouldBeMappedToJson()
            throws Exception {
        final Coordinate givenCoordinate = new Coordinate(5.5, 6.6);

        final String actual = objectMapper.writeValueAsString(givenCoordinate);
        final String expected = """
                {
                  "latitude": 5.5,
                  "longitude": 6.6
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToCoordinate()
            throws Exception {
        final String givenJson = """
                {
                  "lat": 5.5,
                  "lon": 6.6
                }""";

        final Coordinate actual = objectMapper.readValue(givenJson, Coordinate.class);
        final Coordinate expected = new Coordinate(5.5, 6.6);
        assertEquals(expected, actual);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToCoordinateBecauseOfNotDefinedLatitude()
            throws Exception {
        final String givenJson = """
                {
                  "lon": 6.6
                }""";

        objectMapper.readValue(givenJson, Coordinate.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToCoordinateBecauseOfNotDefinedLongitude()
            throws Exception {
        final String givenJson = """
                {
                  "lat": 5.5
                }""";

        objectMapper.readValue(givenJson, Coordinate.class);
    }

    @Test
    public void nodeShouldBeMappedToJson()
            throws Exception {
        final Node givenNode = new Node(5.5, 6.6);

        final String actual = objectMapper.writeValueAsString(givenNode);
        final String expected = """
                {
                   "type": "node",
                   "coordinate": {
                     "latitude": 5.5,
                     "longitude": 6.6
                   }
                 }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToNode()
            throws Exception {
        final String givenJson = """
                {
                   "type": "node",
                   "lat": 53.9090245,
                   "lon": 30.3429838
                }""";

        final Member actual = objectMapper.readValue(givenJson, Member.class);
        final Node expected = new Node(53.9090245, 30.3429838);
        assertEquals(expected, actual);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToNodeBecauseOfLatitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "type": "node",
                  "lon": 6.6
                }""";

        objectMapper.readValue(givenJson, Member.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToNodeBecauseOfLongitudeNotDefined()
            throws Exception {
        final String givenJson = """
                {
                  "type": "node",
                  "lat": 6.6
                }""";

        objectMapper.readValue(givenJson, Member.class);
    }

    @Test
    public void wayShouldBeConvertedToJson()
            throws Exception {
        final Way givenWay = new Way(List.of(new Coordinate(5.5, 6.6), new Coordinate(7.7, 8.8)));

        final String actual = objectMapper.writeValueAsString(givenWay);
        final String expected = """
                {
                   "type": "way",
                   "coordinates": [
                     {
                       "latitude": 5.5,
                       "longitude": 6.6
                     },
                     {
                       "latitude": 7.7,
                       "longitude": 8.8
                     }
                   ]
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToWay()
            throws Exception {
        final String givenJson = """
                {
                   "type": "way",
                   "geometry": [
                     {
                       "lat": 5.5,
                       "lon": 6.6
                     },
                     {
                       "lat": 7.7,
                       "lon": 8.8
                     }
                   ]
                }""";

        final Member actual = objectMapper.readValue(givenJson, Member.class);
        final Way expected = new Way(List.of(new Coordinate(5.5, 6.6), new Coordinate(7.7, 8.8)));
        assertEquals(expected, actual);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToWayBecauseOfGeometryNotDefined()
            throws Exception {
        final String givenJson = """
                {
                    "type": "way"
                }""";

        objectMapper.readValue(givenJson, Member.class);
    }

    @Test
    public void tagsShouldBeConvertedToJson()
            throws Exception {
        final Tags givenTags = new Tags("yes", "Minsk", "city");

        final String actual = objectMapper.writeValueAsString(givenTags);
        final String expected = """
                {
                   "capital": "yes",
                   "name": "Minsk",
                   "place": "city"
                 }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToTags()
            throws Exception {
        final String givenJson = """
                {
                   "capital": "yes",
                   "name": "Minsk",
                   "place": "city"
                 }""";

        final Tags actual = objectMapper.readValue(givenJson, Tags.class);
        final Tags expected = new Tags("yes", "Minsk", "city");
        assertEquals(expected, actual);
    }

    @Test
    public void jsonWithNotDefinedCapitalShouldBeConvertedToTags()
            throws Exception {
        final String givenJson = """
                {
                   "name": "Minsk",
                   "place": "city"
                 }""";

        final Tags actual = objectMapper.readValue(givenJson, Tags.class);
        final Tags expected = new Tags(null, "Minsk", "city");
        assertEquals(expected, actual);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToTagsBecauseOfNameNotDefined()
            throws Exception {
        final String givenJson = """
                {
                   "capital": "yes",
                   "place": "city"
                 }""";

        objectMapper.readValue(givenJson, Tags.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToTagsBecauseOfPlaceNotDefined()
            throws Exception {
        final String givenJson = """
                {
                   "capital": "yes",
                   "name": "Minsk"
                 }""";

        objectMapper.readValue(givenJson, Tags.class);
    }

    @Test
    public void relationShouldBeConvertedToJson()
            throws Exception {
        final Relation givenRelation = new Relation(
                new Bounds(53.0231471, 24.0772654, 53.0474761, 24.1242676),
                List.of(
                        new Node(53.030599, 24.0823296),
                        new Way(
                                List.of(
                                        new Coordinate(53.0336909, 24.0807023),
                                        new Coordinate(53.030599, 24.0823296),
                                        new Coordinate(53.0307359, 24.0836708),
                                        new Coordinate(53.0277788, 24.086618),
                                        new Coordinate(53.0263041, 24.0887068)
                                )
                        ),
                        new Way(
                                List.of(
                                        new Coordinate(53.0449014, 24.0838195),
                                        new Coordinate(53.0449475, 24.0839479),
                                        new Coordinate(53.0452405, 24.0836663)
                                )
                        )
                ),
                new Tags(null, "Свіслач", "town")
        );

        final String actual = objectMapper.writeValueAsString(givenRelation);
        final String expected = """
                {
                   "bounds": {
                     "minLatitude": 53.0231471,
                     "minLongitude": 24.0772654,
                     "maxLatitude": 53.0474761,
                     "maxLongitude": 24.1242676
                   },
                   "members": [
                     {
                       "type": "node",
                       "coordinate": {
                         "latitude": 53.030599,
                         "longitude": 24.0823296
                       }
                     },
                     {
                       "type": "way",
                       "coordinates": [
                         {
                           "latitude": 53.0336909,
                           "longitude": 24.0807023
                         },
                         {
                           "latitude": 53.030599,
                           "longitude": 24.0823296
                         },
                         {
                           "latitude": 53.0307359,
                           "longitude": 24.0836708
                         },
                         {
                           "latitude": 53.0277788,
                           "longitude": 24.086618
                         },
                         {
                           "latitude": 53.0263041,
                           "longitude": 24.0887068
                         }
                       ]
                     },
                     {
                       "type": "way",
                       "coordinates": [
                         {
                           "latitude": 53.0449014,
                           "longitude": 24.0838195
                         },
                         {
                           "latitude": 53.0449475,
                           "longitude": 24.0839479
                         },
                         {
                           "latitude": 53.0452405,
                           "longitude": 24.0836663
                         }
                       ]
                     }
                   ],
                   "tags": {
                     "capital": null,
                     "name": "Свіслач",
                     "place": "town"
                   }
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToRelation()
            throws Exception {
        final String givenJson = """
                {
                     "bounds": {
                       "minlat": 53.0231471,
                       "minlon": 24.0772654,
                       "maxlat": 53.0474761,
                       "maxlon": 24.1242676
                     },
                     "members": [
                       {
                         "type": "node",
                         "lat": 53.9090245,
                         "lon": 30.3429838
                       },
                       {
                         "type": "way",
                         "geometry": [
                           {
                             "lat": 53.0336909,
                             "lon": 24.0807023
                           },
                           {
                             "lat": 53.030599,
                             "lon": 24.0823296
                           },
                           {
                             "lat": 53.0307359,
                             "lon": 24.0836708
                           },
                           {
                             "lat": 53.0277788,
                             "lon": 24.086618
                           },
                           {
                             "lat": 53.0263041,
                             "lon": 24.0887068
                           }
                         ]
                       },
                       {
                         "type": "way",
                         "geometry": [
                           {
                             "lat": 53.0449014,
                             "lon": 24.0838195
                           },
                           {
                             "lat": 53.0449475,
                             "lon": 24.0839479
                           },
                           {
                             "lat": 53.0452405,
                             "lon": 24.0836663
                           }
                         ]
                       }
                     ],
                     "tags": {
                       "name": "Свіслач",
                       "place": "town"
                     }
                  }""";

        final Relation actual = objectMapper.readValue(givenJson, Relation.class);
        final Relation expected = new Relation(
                new Bounds(53.0231471, 24.0772654, 53.0474761, 24.1242676),
                List.of(
                        new Node(53.9090245, 30.3429838),
                        new Way(
                                List.of(
                                        new Coordinate(53.0336909, 24.0807023),
                                        new Coordinate(53.030599, 24.0823296),
                                        new Coordinate(53.0307359, 24.0836708),
                                        new Coordinate(53.0277788, 24.086618),
                                        new Coordinate(53.0263041, 24.0887068)
                                )
                        ),
                        new Way(
                                List.of(
                                        new Coordinate(53.0449014, 24.0838195),
                                        new Coordinate(53.0449475, 24.0839479),
                                        new Coordinate(53.0452405, 24.0836663)
                                )
                        )
                ),
                new Tags(null, "Свіслач", "town")
        );
        assertEquals(expected, actual);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToRelationBecauseOfNotDefinedBounds()
            throws Exception {
        final String givenJson = """
                {
                     "members": [
                       {
                         "type": "node",
                         "lat": 53.9090245,
                         "lon": 30.3429838
                       },
                       {
                         "type": "way",
                         "geometry": [
                           {
                             "lat": 53.0336909,
                             "lon": 24.0807023
                           },
                           {
                             "lat": 53.030599,
                             "lon": 24.0823296
                           },
                           {
                             "lat": 53.0307359,
                             "lon": 24.0836708
                           },
                           {
                             "lat": 53.0277788,
                             "lon": 24.086618
                           },
                           {
                             "lat": 53.0263041,
                             "lon": 24.0887068
                           }
                         ]
                       },
                       {
                         "type": "way",
                         "geometry": [
                           {
                             "lat": 53.0449014,
                             "lon": 24.0838195
                           },
                           {
                             "lat": 53.0449475,
                             "lon": 24.0839479
                           },
                           {
                             "lat": 53.0452405,
                             "lon": 24.0836663
                           }
                         ]
                       }
                     ],
                     "tags": {
                       "name": "Свіслач",
                       "place": "town"
                     }
                 }""";

        objectMapper.readValue(givenJson, Relation.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToRelationBecauseOfNotDefinedMembers()
            throws Exception {
        final String givenJson = """
                {
                     "bounds": {
                       "minlat": 53.0231471,
                       "minlon": 24.0772654,
                       "maxlat": 53.0474761,
                       "maxlon": 24.1242676
                     },
                     "tags": {
                       "name": "Свіслач",
                       "place": "town"
                     }
                 }""";

        objectMapper.readValue(givenJson, Relation.class);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToRelationBecauseOfNotDefinedTags()
            throws Exception {
        final String givenJson = """
                {
                     "bounds": {
                       "minlat": 53.0231471,
                       "minlon": 24.0772654,
                       "maxlat": 53.0474761,
                       "maxlon": 24.1242676
                     },
                     "members": [
                       {
                         "type": "node",
                         "lat": 53.9090245,
                         "lon": 30.3429838
                       },
                       {
                         "type": "way",
                         "geometry": [
                           {
                             "lat": 53.0336909,
                             "lon": 24.0807023
                           },
                           {
                             "lat": 53.030599,
                             "lon": 24.0823296
                           },
                           {
                             "lat": 53.0307359,
                             "lon": 24.0836708
                           },
                           {
                             "lat": 53.0277788,
                             "lon": 24.086618
                           },
                           {
                             "lat": 53.0263041,
                             "lon": 24.0887068
                           }
                         ]
                       },
                       {
                         "type": "way",
                         "geometry": [
                           {
                             "lat": 53.0449014,
                             "lon": 24.0838195
                           },
                           {
                             "lat": 53.0449475,
                             "lon": 24.0839479
                           },
                           {
                             "lat": 53.0452405,
                             "lon": 24.0836663
                           }
                         ]
                       }
                     ]
                 }""";

        objectMapper.readValue(givenJson, Relation.class);
    }

    @Test
    public void responseShouldBeConvertedToJson()
            throws Exception {
        final OverpassSearchCityResponse givenResponse = new OverpassSearchCityResponse(
                List.of(
                        new Relation(
                                new Bounds(53.5833949, 24.9732414, 53.6059, 24.9957),
                                List.of(
                                        new Node(53.5864079, 24.9774542),
                                        new Way(
                                                List.of(
                                                        new Coordinate(53.5866941, 24.9764369),
                                                        new Coordinate(53.5864079, 24.9774542)
                                                )
                                        ),
                                        new Way(
                                                List.of(
                                                        new Coordinate(53.6001648, 24.9747556),
                                                        new Coordinate(53.5996087, 24.9746791),
                                                        new Coordinate(53.5968642, 24.9774069)
                                                )
                                        )
                                ),
                                new Tags(null, "Жалудок", "town")
                        ),
                        new Relation(
                                new Bounds(54.0213346, 25.9151011, 54.0410501, 25.9469228),
                                List.of(
                                        new Node(54.0283401, 25.9267536),
                                        new Way(
                                                List.of(
                                                        new Coordinate(54.0283401, 25.9267536),
                                                        new Coordinate(54.0266101, 25.9310107)
                                                )
                                        )
                                ),
                                new Tags(null, "Юрацішкі", "town")
                        )
                )
        );

        final String actual = objectMapper.writeValueAsString(givenResponse);
        final String expected = """
                {
                   "relations": [
                     {
                       "bounds": {
                         "minLatitude": 53.5833949,
                         "minLongitude": 24.9732414,
                         "maxLatitude": 53.6059,
                         "maxLongitude": 24.9957
                       },
                       "members": [
                         {
                           "type": "node",
                           "coordinate": {
                             "latitude": 53.5864079,
                             "longitude": 24.9774542
                           }
                         },
                         {
                           "type": "way",
                           "coordinates": [
                             {
                               "latitude": 53.5866941,
                               "longitude": 24.9764369
                             },
                             {
                               "latitude": 53.5864079,
                               "longitude": 24.9774542
                             }
                           ]
                         },
                         {
                           "type": "way",
                           "coordinates": [
                             {
                               "latitude": 53.6001648,
                               "longitude": 24.9747556
                             },
                             {
                               "latitude": 53.5996087,
                               "longitude": 24.9746791
                             },
                             {
                               "latitude": 53.5968642,
                               "longitude": 24.9774069
                             }
                           ]
                         }
                       ],
                       "tags": {
                         "capital": null,
                         "name": "Жалудок",
                         "place": "town"
                       }
                     },
                     {
                       "bounds": {
                         "minLatitude": 54.0213346,
                         "minLongitude": 25.9151011,
                         "maxLatitude": 54.0410501,
                         "maxLongitude": 25.9469228
                       },
                       "members": [
                         {
                           "type": "node",
                           "coordinate": {
                             "latitude": 54.0283401,
                             "longitude": 25.9267536
                           }
                         },
                         {
                           "type": "way",
                           "coordinates": [
                             {
                               "latitude": 54.0283401,
                               "longitude": 25.9267536
                             },
                             {
                               "latitude": 54.0266101,
                               "longitude": 25.9310107
                             }
                           ]
                         }
                       ],
                       "tags": {
                         "capital": null,
                         "name": "Юрацішкі",
                         "place": "town"
                       }
                     }
                   ]
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToResponse()
            throws Exception {
        final String givenJson = """
                {
                   "elements": [
                     {
                       "bounds": {
                         "minlat": 53.5833949,
                         "minlon": 24.9732414,
                         "maxlat": 53.6059,
                         "maxlon": 24.9957
                       },
                       "members": [
                         {
                           "type": "node",
                           "lat": 53.9090245,
                           "lon": 30.3429838
                         },
                         {
                           "type": "way",
                           "geometry": [
                             {
                               "lat": 53.5866941,
                               "lon": 24.9764369
                             },
                             {
                               "lat": 53.5864079,
                               "lon": 24.9774542
                             }
                           ]
                         },
                         {
                           "type": "way",
                           "geometry": [
                             {
                               "lat": 53.6001648,
                               "lon": 24.9747556
                             },
                             {
                               "lat": 53.5996087,
                               "lon": 24.9746791
                             },
                             {
                               "lat": 53.5968642,
                               "lon": 24.9774069
                             }
                           ]
                         }
                       ],
                       "tags": {
                         "capital": null,
                         "name": "Жалудок",
                         "place": "town"
                       }
                     },
                     {
                       "bounds": {
                         "minlat": 54.0213346,
                         "minlon": 25.9151011,
                         "maxlat": 54.0410501,
                         "maxlon": 25.9469228
                       },
                       "members": [
                         {
                           "type": "node",
                           "lat": 53.9090245,
                           "lon": 30.3429838
                         },
                         {
                           "type": "way",
                           "geometry": [
                             {
                               "lat": 54.0283401,
                               "lon": 25.9267536
                             },
                             {
                               "lat": 54.0266101,
                               "lon": 25.9310107
                             }
                           ]
                         }
                       ],
                       "tags": {
                         "capital": null,
                         "name": "Юрацішкі",
                         "place": "town"
                       }
                     }
                   ]
                }""";

        final OverpassSearchCityResponse actual = objectMapper.readValue(givenJson, OverpassSearchCityResponse.class);
        final OverpassSearchCityResponse expected = new OverpassSearchCityResponse(
                List.of(
                        new Relation(
                                new Bounds(53.5833949, 24.9732414, 53.6059, 24.9957),
                                List.of(
                                        new Node(53.9090245, 30.3429838),
                                        new Way(
                                                List.of(
                                                        new Coordinate(53.5866941, 24.9764369),
                                                        new Coordinate(53.5864079, 24.9774542)
                                                )
                                        ),
                                        new Way(
                                                List.of(
                                                        new Coordinate(53.6001648, 24.9747556),
                                                        new Coordinate(53.5996087, 24.9746791),
                                                        new Coordinate(53.5968642, 24.9774069)
                                                )
                                        )
                                ),
                                new Tags(null, "Жалудок", "town")
                        ),
                        new Relation(
                                new Bounds(54.0213346, 25.9151011, 54.0410501, 25.9469228),
                                List.of(
                                        new Node(53.9090245, 30.3429838),
                                        new Way(
                                                List.of(
                                                        new Coordinate(54.0283401, 25.9267536),
                                                        new Coordinate(54.0266101, 25.9310107)
                                                )
                                        )
                                ),
                                new Tags(null, "Юрацішкі", "town")
                        )
                )
        );
        assertEquals(expected, actual);
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonShouldNotBeConvertedToResponseBecauseOfRelationsNotDefined()
            throws Exception {
        final String givenJson = """
                {

                }""";

        objectMapper.readValue(givenJson, OverpassSearchCityResponse.class);
    }
}
