package by.aurorasoft.nominatim.controller.city.model;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static by.aurorasoft.nominatim.model.CityType.CAPITAL;
import static by.aurorasoft.nominatim.testutil.ConstraintViolationUtil.findFirstMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static by.aurorasoft.nominatim.testutil.CityRequestUtil.checkEquals;

public final class CityRequestTest extends AbstractJunitSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

    @Autowired
    private GeoJSONReader geoJSONReader;

    @Test
    public void requestShouldBeConvertedToJson()
            throws Exception {
        final CityRequest givenRequest = new CityRequest(
                "name",
                createGeometry(
                        new Coordinate[]{
                                new Coordinate(1, 1),
                                new Coordinate(2, 1),
                                new Coordinate(2, 2),
                                new Coordinate(1, 1)
                        }
                ),
                CAPITAL
        );

        final String actual = objectMapper.writeValueAsString(givenRequest);
        final String expected = """
                {
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
    public void jsonShouldBeConvertedToRequest()
            throws Exception {
        final String givenJson = """
                {
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

        final CityRequest actual = objectMapper.readValue(givenJson, CityRequest.class);
        final CityRequest expected = new CityRequest(
                "name",
                createGeometry(
                        new Coordinate[]{
                                new Coordinate(1, 1),
                                new Coordinate(2, 1),
                                new Coordinate(2, 2),
                                new Coordinate(1, 1)
                        }
                ),
                CAPITAL
        );
        checkEquals(expected, actual, geoJSONReader);
    }

    @Test
    public void requestShouldBeValid() {
        final CityRequest givenRequest = new CityRequest(
                "name",
                createGeometry(
                        new Coordinate[]{
                                new Coordinate(1, 1),
                                new Coordinate(2, 1),
                                new Coordinate(2, 2),
                                new Coordinate(1, 1)
                        }
                ),
                CAPITAL
        );

        final Set<ConstraintViolation<CityRequest>> violations = validator.validate(givenRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfNameIsNull() {
        final CityRequest givenRequest = CityRequest.builder()
                .geometry(
                        createGeometry(
                                new Coordinate[]{
                                        new Coordinate(1, 1),
                                        new Coordinate(2, 1),
                                        new Coordinate(2, 2),
                                        new Coordinate(1, 1)
                                }
                        )
                )
                .type(CAPITAL)
                .build();

        final Set<ConstraintViolation<CityRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid name of city", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfNameIsNotValid() {
        final CityRequest givenRequest = new CityRequest(
                " 111",
                createGeometry(
                        new Coordinate[]{
                                new Coordinate(1, 1),
                                new Coordinate(2, 1),
                                new Coordinate(2, 2),
                                new Coordinate(1, 1)
                        }
                ),
                CAPITAL
        );

        final Set<ConstraintViolation<CityRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("Invalid name of city", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfGeometryIsNull() {
        final CityRequest givenRequest = CityRequest.builder()
                .name("name")
                .type(CAPITAL)
                .build();

        final Set<ConstraintViolation<CityRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfTypeIsNull() {
        final CityRequest givenRequest = CityRequest.builder()
                .name("name")
                .geometry(
                        createGeometry(
                                new Coordinate[]{
                                        new Coordinate(1, 1),
                                        new Coordinate(2, 1),
                                        new Coordinate(2, 2),
                                        new Coordinate(1, 1)
                                }
                        )
                )
                .build();

        final Set<ConstraintViolation<CityRequest>> violations = validator.validate(givenRequest);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    private Geometry createGeometry(final Coordinate[] coordinates) {
        return geoJSONWriter.write(geometryFactory.createPolygon(coordinates));
    }
}
