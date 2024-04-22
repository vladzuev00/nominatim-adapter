package by.aurorasoft.nominatim.service.mileage.loader;

import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.service.geometry.GeometryService;
import by.aurorasoft.nominatim.service.mileage.simplifier.TrackSimplifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class TrackCityGeometryLoaderTest {

    @Mock
    private TrackSimplifier mockedTrackSimplifier;

    @Mock
    private GeometryService mockedGeometryService;

    private TestTrackCityGeometryLoader loader;

    @Before
    public void initializeLoader() {
        loader = new TestTrackCityGeometryLoader(mockedTrackSimplifier, mockedGeometryService);
    }

    @Test
    public void geometriesShouldBeLoaded() {
        final Track givenTrack = createEmptyTrack();

        final Track givenSimplifiedTrack = createEmptyTrack();
        when(mockedTrackSimplifier.simplify(same(givenTrack))).thenReturn(givenSimplifiedTrack);

        final LineString givenSimplifiedLine = mock(LineString.class);
        when(mockedGeometryService.createLine(same(givenSimplifiedTrack))).thenReturn(givenSimplifiedLine);

        final List<PreparedGeometry> actual = loader.load(givenTrack);
        assertTrue(actual.isEmpty());

        assertSame(loader.capturedLine, givenSimplifiedLine);
    }

    private static Track createEmptyTrack() {
        return new Track(emptyList());
    }

    private static final class TestTrackCityGeometryLoader extends TrackCityGeometryLoader {
        private LineString capturedLine;

        public TestTrackCityGeometryLoader(final TrackSimplifier trackSimplifier, final GeometryService geometryService) {
            super(trackSimplifier, geometryService);
        }

        @Override
        protected List<PreparedGeometry> load(final LineString line) {
            capturedLine = line;
            return emptyList();
        }
    }
}
