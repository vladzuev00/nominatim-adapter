package by.aurorasoft.distanceclassifier.controller.city.model;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
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
        final CityResponse givenRequest = new CityResponse(
                255L,
                "name",
                createGeometry(
                        new Coordinate(1, 1),
                        new Coordinate(2, 1),
                        new Coordinate(2, 2),
                        new Coordinate(1, 1)
                ),
                CAPITAL
        );

        final String actual = objectMapper.writeValueAsString(givenRequest);
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
                  "type": "CAPITAL"
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
                }""";

        final CityResponse actual = objectMapper.readValue(givenJson, CityResponse.class);
        final CityResponse expected = new CityResponse(
                255L,
                "name",
                createGeometry(
                        new Coordinate(1, 1),
                        new Coordinate(2, 1),
                        new Coordinate(2, 2),
                        new Coordinate(1, 1)
                ),
                CAPITAL
        );
        checkEquals(expected, actual, geoJSONReader);
    }

    private Geometry createGeometry(final Coordinate... coordinates) {
        return geoJSONWriter.write(geometryFactory.createPolygon(coordinates));
    }
}
