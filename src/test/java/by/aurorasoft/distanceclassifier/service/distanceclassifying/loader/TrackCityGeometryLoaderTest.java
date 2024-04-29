//package by.aurorasoft.distanceclassifier.service.distanceclassifying.loader;
//
//import by.aurorasoft.distanceclassifier.model.PreparedBoundedGeometry;
//import by.aurorasoft.distanceclassifier.model.Track;
//import by.aurorasoft.distanceclassifier.service.distanceclassifying.simplifier.TrackSimplifier;
//import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.locationtech.jts.geom.LineString;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Set;
//
//import static java.util.Collections.emptySet;
//import static org.junit.Assert.assertSame;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class TrackCityGeometryLoaderTest {
//
//    @Mock
//    private TrackSimplifier mockedTrackSimplifier;
//
//    @Mock
//    private GeometryService mockedGeometryService;
//
//    private TestTrackCityGeometryLoader loader;
//
//    @Before
//    public void initializeLoader() {
//        loader = new TestTrackCityGeometryLoader(mockedTrackSimplifier, mockedGeometryService);
//    }
//
//    @Test
//    public void geometriesShouldBeLoaded() {
//        final Track givenTrack = mock(Track.class);
//
//        final Track givenSimplifiedTrack = mock(Track.class);
//        when(mockedTrackSimplifier.simplify(same(givenTrack))).thenReturn(givenSimplifiedTrack);
//
//        final LineString givenSimplifiedLine = mock(LineString.class);
//        when(mockedGeometryService.createLine(same(givenSimplifiedTrack))).thenReturn(givenSimplifiedLine);
//
//        final Set<PreparedBoundedGeometry> actual = loader.load(givenTrack);
//        assertTrue(actual.isEmpty());
//
//        assertSame(loader.capturedLine, givenSimplifiedLine);
//    }
//
//    private static final class TestTrackCityGeometryLoader extends TrackCityGeometryLoader {
//        private LineString capturedLine;
//
//        public TestTrackCityGeometryLoader(final TrackSimplifier trackSimplifier, final GeometryService geometryService) {
//            super(trackSimplifier, geometryService);
//        }
//
//        @Override
//        protected Set<PreparedBoundedGeometry> loadInternal(final LineString line) {
//            capturedLine = line;
//            return emptySet();
//        }
//    }
//}
