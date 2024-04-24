package by.aurorasoft.mileagecalculator.service.mileage.loader;

import by.aurorasoft.mileagecalculator.crud.service.CityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class TrackCityGeometryLoaderFromRepositoryTest {

    @Mock
    private CityService mockedCityService;

    private TrackCityGeometryLoaderFromRepository loader;

    @Before
    public void initializeLoader() {
        loader = new TrackCityGeometryLoaderFromRepository(null, null, mockedCityService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void geometriesShouldBeLoaded() {
        final LineString givenLine = mock(LineString.class);

        final List<PreparedGeometry> givenGeometries = mock(List.class);
        when(mockedCityService.findIntersectedPreparedGeometries(same(givenLine))).thenReturn(givenGeometries);

        final List<PreparedGeometry> actual = loader.load(givenLine);
        assertSame(givenGeometries, actual);
    }
}
