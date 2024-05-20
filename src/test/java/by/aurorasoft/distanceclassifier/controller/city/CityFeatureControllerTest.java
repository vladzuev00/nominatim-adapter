package by.aurorasoft.distanceclassifier.controller.city;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.service.cityfeature.CityFeatureService;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.Map;

import static by.aurorasoft.distanceclassifier.testutil.GeometryUtil.createPolygon;
import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.getExpectingOk;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class CityFeatureControllerTest extends AbstractSpringBootTest {
    private static final String URL = "/api/v1/cityFeature";

    @MockBean
    private CityFeatureService mockedService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

    @Test
    public void featureCollectionShouldBeGot()
            throws Exception {
        final FeatureCollection givenCollection = new FeatureCollection(
                new Feature[]{
                        createFeature("POLYGON((1 1, 2 1, 2 2, 1 1))", Map.of("first-property", "first-value")),
                        createFeature("POLYGON((1 1, 3 1, 3 2, 1 1))", Map.of("second-property", "second-value", "third-property", "third-value"))
                }
        );
        when(mockedService.getAll()).thenReturn(givenCollection);

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
                      "properties": {
                        "first-property": "first-value"
                      }
                    },
                    {
                      "type": "Feature",
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
                      "properties": {
                        "third-property": "third-value",
                        "second-property": "second-value"
                      }
                    }
                  ]
                }""";
        assertEquals(expected, actual, true);
    }

    private Feature createFeature(final String polygonText, final Map<String, Object> properties) {
        final Polygon polygon = createPolygon(polygonText, geometryFactory);
        final Geometry featurePolygon = geoJSONWriter.write(polygon);
        return new Feature(featurePolygon, properties);
    }
}
