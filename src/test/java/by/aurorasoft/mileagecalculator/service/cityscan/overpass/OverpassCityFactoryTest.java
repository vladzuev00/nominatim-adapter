package by.aurorasoft.mileagecalculator.service.cityscan.overpass;

import by.aurorasoft.mileagecalculator.crud.model.dto.City;
import by.aurorasoft.mileagecalculator.model.OverpassSearchCityResponse.Bounds;
import by.aurorasoft.mileagecalculator.model.OverpassSearchCityResponse.Relation;
import by.aurorasoft.mileagecalculator.model.OverpassSearchCityResponse.Tags;
import by.aurorasoft.mileagecalculator.service.geometry.GeometryService;
import by.aurorasoft.mileagecalculator.service.cityscan.overpass.OverpassCityFactory.OverpassCityCreatingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static by.aurorasoft.mileagecalculator.model.CityType.TOWN;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class OverpassCityFactoryTest {

    @Mock
    private GeometryService mockedGeometryService;

    private OverpassCityFactory factory;

    @Before
    public void initializeFactory() {
        factory = new OverpassCityFactory(mockedGeometryService);
    }

    @Test
    public void cityShouldBeCreated() {
        final Bounds givenBounds = Bounds.builder().build();
        final String givenName = "city";
        final Relation givenRelation = Relation.builder()
                .bounds(givenBounds)
                .tags(
                        Tags.builder()
                                .name(givenName)
                                .place("town")
                                .build()
                )
                .build();

        final MultiPolygon givenGeometry = mock(MultiPolygon.class);
        when(mockedGeometryService.createMultiPolygon(same(givenRelation))).thenReturn(givenGeometry);

        final Polygon givenBoundingBox = mock(Polygon.class);
        when(mockedGeometryService.createPolygon(same(givenBounds))).thenReturn(givenBoundingBox);

        final City actual = factory.create(givenRelation);
        final City expected = City.builder()
                .name(givenName)
                .geometry(givenGeometry)
                .type(TOWN)
                .boundingBox(givenBoundingBox)
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = OverpassCityCreatingException.class)
    public void cityShouldNotBeCreatedBecauseOfImpossibleToIdentifyCityType() {
        final Relation givenRelation = Relation.builder()
                .tags(
                        Tags.builder()
                                .place("some-value")
                                .build()
                )
                .build();

        factory.create(givenRelation);
    }
}
