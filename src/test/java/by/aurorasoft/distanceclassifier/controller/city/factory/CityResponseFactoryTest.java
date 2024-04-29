package by.aurorasoft.distanceclassifier.controller.city.factory;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse;
import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse.CityGeometryResponse;
import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.model.CityType;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import static by.aurorasoft.distanceclassifier.model.CityType.CAPITAL;
import static by.aurorasoft.distanceclassifier.testutil.CityResponseUtil.checkEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class CityResponseFactoryTest extends AbstractSpringBootTest {

    @MockBean
    private GeoJSONWriter mockedGeoJSONWriter;

    @Autowired
    private CityResponseFactory factory;

    @Autowired
    private GeoJSONReader geoJSONReader;

    @Test
    public void responseShouldBeCreated() {
        final Long givenId = 255L;
        final String givenName = "name";
        final CityType givenType = CAPITAL;
        final Geometry givenGeometry = mock(Geometry.class);
        final Geometry givenBoundingBox = mock(Geometry.class);
        final City givenCity = new City(givenId, givenName, givenType, new CityGeometry(givenGeometry, givenBoundingBox));

        final org.wololo.geojson.Geometry givenResponseGeometry = mock(org.wololo.geojson.Geometry.class);
        when(mockedGeoJSONWriter.write(same(givenGeometry))).thenReturn(givenResponseGeometry);

        final org.wololo.geojson.Geometry givenResponseBoundingBox = mock(org.wololo.geojson.Geometry.class);
        when(mockedGeoJSONWriter.write(same(givenBoundingBox))).thenReturn(givenResponseBoundingBox);

        final CityResponse actual = factory.create(givenCity);
        final CityResponse expected = new CityResponse(
                givenId,
                givenName,
                givenType,
                new CityGeometryResponse(givenResponseGeometry, givenResponseBoundingBox)
        );
        checkEquals(expected, actual, geoJSONReader);
    }
}
