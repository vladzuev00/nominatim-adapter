package by.aurorasoft.mileagecalculator.service.cityscan;

import by.aurorasoft.mileagecalculator.crud.model.dto.City;
import by.aurorasoft.mileagecalculator.crud.service.CityService;
import by.aurorasoft.mileagecalculator.model.AreaCoordinate;
import by.aurorasoft.mileagecalculator.model.OverpassSearchCityResponse;
import by.aurorasoft.mileagecalculator.model.OverpassSearchCityResponse.Relation;
import by.aurorasoft.mileagecalculator.service.cityscan.overpass.OverpassCityFactory;
import by.aurorasoft.mileagecalculator.service.cityscan.overpass.OverpassClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class CityScanningServiceTest {

    @Mock
    private OverpassClient mockedOverpassClient;

    @Mock
    private OverpassCityFactory mockedCityFactory;

    @Mock
    private CityService mockedCityService;

    private CityScanningService scanningService;

    @Before
    public void initializeScanningService() {
        scanningService = new CityScanningService(mockedOverpassClient, mockedCityFactory, mockedCityService);
    }

    @Test
    public void citiesShouldBeScanned() {
        final AreaCoordinate givenAreaCoordinate = mock(AreaCoordinate.class);

        final Relation firstGivenRelation = mock(Relation.class);
        final Relation secondGivenRelation = mock(Relation.class);
        final OverpassSearchCityResponse givenResponse = new OverpassSearchCityResponse(
                List.of(firstGivenRelation, secondGivenRelation)
        );
        when(mockedOverpassClient.findCities(same(givenAreaCoordinate))).thenReturn(givenResponse);

        final City firstGivenCity = createCityBoundWithRelation(firstGivenRelation, 255L);
        final City secondGivenCity = createCityBoundWithRelation(secondGivenRelation, 256L);

        scanningService.scan(givenAreaCoordinate);

        final List<City> expectedSavedCities = List.of(firstGivenCity, secondGivenCity);
        verify(mockedCityService, times(1)).saveAll(eq(expectedSavedCities));
    }

    private City createCityBoundWithRelation(final Relation relation, final Long id) {
        final City city = createCity(id);
        when(mockedCityFactory.create(same(relation))).thenReturn(city);
        return city;
    }

    private static City createCity(final Long id) {
        return City.builder()
                .id(id)
                .build();
    }
}
