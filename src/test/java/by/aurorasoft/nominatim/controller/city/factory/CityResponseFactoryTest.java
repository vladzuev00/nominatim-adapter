package by.aurorasoft.nominatim.controller.city.factory;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.controller.city.model.CityResponse;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.model.CityType;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import static by.aurorasoft.nominatim.model.CityType.CAPITAL;
import static by.aurorasoft.nominatim.util.CityResponseUtil.checkEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class CityResponseFactoryTest extends AbstractJunitSpringBootTest {

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
        final Geometry givenGeometry = mock(Geometry.class);
        final CityType givenType = CAPITAL;
        final City givenCity = City.builder()
                .id(givenId)
                .name(givenName)
                .geometry(givenGeometry)
                .type(givenType)
                .build();

        final org.wololo.geojson.Geometry givenResponseGeometry = mock(org.wololo.geojson.Geometry.class);
        when(mockedGeoJSONWriter.write(same(givenGeometry))).thenReturn(givenResponseGeometry);

        final CityResponse actual = factory.create(givenCity);
        final CityResponse expected = new CityResponse(givenId, givenName, givenResponseGeometry, givenType);
        checkEquals(expected, actual, geoJSONReader);
    }
}
