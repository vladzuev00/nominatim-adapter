package by.aurorasoft.distanceclassifier.service.cityfeature;

import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.service.cityfeature.factory.CityFeatureFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;

import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class CityFeatureServiceTest {

    @Mock
    private CityFeatureFactory mockedFeatureFactory;

    @Mock
    private CityService mockedCityService;

    private CityFeatureService featureService;

    private boolean streamWasClosed;

    @Before
    public void initializeFeatureService() {
        featureService = new CityFeatureService(mockedFeatureFactory, mockedCityService);
    }

    @Before
    public void resetStreamWasClosed() {
        streamWasClosed = false;
    }

    @Test
    public void allCityFeaturesShouldBeGot() {
        final City firstGivenCity = mock(City.class);
        final City secondGivenCity = mock(City.class);
        final City thirdGivenCity = mock(City.class);
        final Stream<City> givenCities = createStreamCapturingClose(firstGivenCity, secondGivenCity, thirdGivenCity);
        when(mockedCityService.streamAll()).thenReturn(givenCities);

        final Feature firstGivenFeature = mockFeatureFor(firstGivenCity);
        final Feature secondGivenFeature = mockFeatureFor(secondGivenCity);
        final Feature thirdGivenFeature = mockFeatureFor(thirdGivenCity);

        final FeatureCollection actual = featureService.getAll();
        final FeatureCollection expected = new FeatureCollection(
                new Feature[]{
                        firstGivenFeature,
                        secondGivenFeature,
                        thirdGivenFeature
                }
        );
        checkEquals(expected, actual);

        assertTrue(streamWasClosed);
    }

    private Stream<City> createStreamCapturingClose(final City... cities) {
        return Stream.of(cities).onClose(() -> streamWasClosed = true);
    }

    private Feature mockFeatureFor(final City city) {
        final Feature feature = mock(Feature.class);
        when(mockedFeatureFactory.create(same(city))).thenReturn(feature);
        return feature;
    }

    private void checkEquals(final FeatureCollection expected, final FeatureCollection actual) {
        assertArrayEquals(expected.getFeatures(), actual.getFeatures());
    }
}
