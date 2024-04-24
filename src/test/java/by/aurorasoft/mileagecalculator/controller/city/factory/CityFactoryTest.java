package by.aurorasoft.mileagecalculator.controller.city.factory;

import by.aurorasoft.mileagecalculator.controller.city.model.CityRequest;
import by.aurorasoft.mileagecalculator.crud.model.dto.City;
import by.aurorasoft.mileagecalculator.model.CityType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.wololo.jts2geojson.GeoJSONReader;

import static by.aurorasoft.mileagecalculator.model.CityType.CAPITAL;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class CityFactoryTest {

    @Mock
    private GeoJSONReader mockedGeoJSONReader;

    private CityFactory factory;

    @Before
    public void initializeFactory() {
        factory = new CityFactory(mockedGeoJSONReader);
    }

    @Test
    public void cityShouldBeCreated() {
        final String givenName = "city";
        final org.wololo.geojson.Geometry givenRequestGeometry = mock(org.wololo.geojson.Geometry.class);
        final CityType givenType = CAPITAL;
        final CityRequest givenRequest = new CityRequest(givenName, givenRequestGeometry, givenType);

        final Geometry givenGeometry = mock(Geometry.class);
        when(mockedGeoJSONReader.read(same(givenRequestGeometry))).thenReturn(givenGeometry);

        final Geometry givenBoundingBox = mock(Geometry.class);
        when(givenGeometry.getEnvelope()).thenReturn(givenBoundingBox);

        final City actual = factory.create(givenRequest);
        final City expected = City.builder()
                .name(givenName)
                .geometry(givenGeometry)
                .type(givenType)
                .boundingBox(givenBoundingBox)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void cityShouldBeCreatedWithId() {
        final Long givenId = 255L;

        final String givenName = "city";
        final org.wololo.geojson.Geometry givenRequestGeometry = mock(org.wololo.geojson.Geometry.class);
        final CityType givenType = CAPITAL;
        final CityRequest givenRequest = new CityRequest(givenName, givenRequestGeometry, givenType);

        final Geometry givenGeometry = mock(Geometry.class);
        when(mockedGeoJSONReader.read(same(givenRequestGeometry))).thenReturn(givenGeometry);

        final Geometry givenBoundingBox = mock(Geometry.class);
        when(givenGeometry.getEnvelope()).thenReturn(givenBoundingBox);

        final City actual = factory.create(givenId, givenRequest);
        final City expected = new City(givenId, givenName, givenGeometry, givenType, givenBoundingBox);
        assertEquals(expected, actual);
    }
}
