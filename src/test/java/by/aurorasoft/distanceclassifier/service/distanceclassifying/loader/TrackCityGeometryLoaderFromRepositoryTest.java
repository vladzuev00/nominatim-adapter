package by.aurorasoft.distanceclassifier.service.distanceclassifying.loader;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.PreparedBoundedGeometry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.LineString;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

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
    public void geometriesShouldBeLoadedInternally() {
        final LineString givenLine = mock(LineString.class);

        final Set<PreparedBoundedGeometry> givenGeometries = mock(Set.class);
        when(mockedCityService.findBoundedPreparedGeometries(same(givenLine))).thenReturn(givenGeometries);

        final Set<PreparedBoundedGeometry> actual = loader.loadInternal(givenLine);
        assertSame(givenGeometries, actual);
    }
}
