package by.aurorasoft.distanceclassifier.service.cityfeature.factory;

import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.model.CityType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.wololo.geojson.Feature;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.Map;

import static by.aurorasoft.distanceclassifier.model.CityType.CAPITAL;
import static by.aurorasoft.distanceclassifier.service.cityfeature.factory.CityFeatureFactory.*;
import static by.aurorasoft.distanceclassifier.testutil.FeatureUtil.checkEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class CityFeatureFactoryTest {

    @Mock
    private GeoJSONWriter mockedGeoJSONWriter;

    private CityFeatureFactory factory;

    @Before
    public void initializeFactory() {
        factory = new CityFeatureFactory(mockedGeoJSONWriter);
    }

    @Test
    public void featureShouldBeCreated() {
        final Long givenId = 255L;
        final String givenName = "city-name";
        final CityType givenType = CAPITAL;
        final Geometry givenGeometry = mock(Geometry.class);
        final City givenCity = createCity(givenId, givenName, givenType, givenGeometry);

        final var givenFeatureGeometry = mock(org.wololo.geojson.Geometry.class);
        when(mockedGeoJSONWriter.write(same(givenGeometry))).thenReturn(givenFeatureGeometry);

        final Feature actual = factory.create(givenCity);
        final Map<String, Object> expectedProperties = createProperties(givenId, givenName, givenType);
        final Feature expected = new Feature(givenFeatureGeometry, expectedProperties);
        checkEquals(expected, actual);
    }

    @SuppressWarnings("SameParameterValue")
    private City createCity(final Long id, final String name, final CityType type, final Geometry geometry) {
        return City.builder()
                .id(id)
                .name(name)
                .type(type)
                .geometry(createCityGeometry(geometry))
                .build();
    }

    private CityGeometry createCityGeometry(final Geometry geometry) {
        return CityGeometry.builder()
                .geometry(geometry)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private Map<String, Object> createProperties(final Long id, final String name, final CityType type) {
        return Map.of(
                PROPERTY_KEY_ID, id,
                PROPERTY_KEY_NAME, name,
                PROPERTY_KEY_TYPE, type
        );
    }
}
