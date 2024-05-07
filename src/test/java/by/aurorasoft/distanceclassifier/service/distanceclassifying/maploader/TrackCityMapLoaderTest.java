package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader;

import by.aurorasoft.distanceclassifier.model.CityMap;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.simplifier.TrackSimplifier;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class TrackCityMapLoaderTest {

    @Mock
    private TrackSimplifier mockedTrackSimplifier;

    @Mock
    private GeometryService mockedGeometryService;

    @Test
    public void mapShouldBeLoaded() {
        final PreparedGeometry givenScannedGeometry = mock(PreparedGeometry.class);
        final TestTrackCityMapLoader givenLoader = createLoader(givenScannedGeometry);
        final Track givenTrack = mock(Track.class);

        final Track givenSimplifiedTrack = mock(Track.class);
        when(mockedTrackSimplifier.simplify(same(givenTrack))).thenReturn(givenSimplifiedTrack);

        final LineString givenSimplifiedLine = mock(LineString.class);
        when(mockedGeometryService.createLine(same(givenSimplifiedTrack))).thenReturn(givenSimplifiedLine);

        final CityMap actual = givenLoader.load(givenTrack);
        final CityMap expected = new CityMap(emptySet(), givenScannedGeometry);
        assertEquals(expected, actual);

        assertSame(givenLoader.capturedLine, givenSimplifiedLine);
        assertTrue(givenLoader.streamWasClosed);
    }

    private TestTrackCityMapLoader createLoader(final PreparedGeometry scannedGeometry) {
        return new TestTrackCityMapLoader(mockedTrackSimplifier, mockedGeometryService, scannedGeometry);
    }

    private static final class TestTrackCityMapLoader extends TrackCityMapLoader {
        private final PreparedGeometry scannedGeometry;
        private LineString capturedLine;
        private boolean streamWasClosed;

        public TestTrackCityMapLoader(final TrackSimplifier trackSimplifier,
                                      final GeometryService geometryService,
                                      final PreparedGeometry scannedGeometry) {
            super(trackSimplifier, geometryService);
            this.scannedGeometry = scannedGeometry;
        }

        @Override
        protected Stream<PreparedGeometry> loadCityGeometries(final LineString line) {
            capturedLine = line;
            return Stream.<PreparedGeometry>empty().onClose(() -> streamWasClosed = true);
        }

        @Override
        protected PreparedGeometry loadScannedGeometry() {
            return scannedGeometry;
        }
    }
}
