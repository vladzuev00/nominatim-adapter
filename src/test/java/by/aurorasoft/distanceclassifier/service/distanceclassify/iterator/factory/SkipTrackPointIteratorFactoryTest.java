package by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.factory;

import by.aurorasoft.distanceclassifier.config.property.ClassifyDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.SkipTrackPointIterator;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.pointreplacer.TrackPointReplacer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static by.aurorasoft.distanceclassifier.testutil.ReflectionUtil.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class SkipTrackPointIteratorFactoryTest {
    private static final String FIELD_NAME_POINT_REPLACER = "pointReplacer";
    private static final String FIELD_NAME_POINTS = "points";
    private static final String FIELD_NAME_POINT_MIN_GPS_RELATIVE = "pointMinGpsRelative";

    @Mock
    private TrackPointReplacer mockedPointReplacer;

    @Mock
    private ClassifyDistanceProperty mockedProperty;

    private SkipTrackPointIteratorFactory factory;

    @Before
    public void initializeFactory() {
        factory = new SkipTrackPointIteratorFactory(mockedPointReplacer, mockedProperty);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void iteratorShouldBeCreated() {
        final Track givenTrack = mock(Track.class);

        final List<TrackPoint> givenPoints = mock(List.class);
        when(givenTrack.getPoints()).thenReturn(givenPoints);

        final double givenPointMinGpsRelative = 500;
        when(mockedProperty.getPointMinGpsRelative()).thenReturn(givenPointMinGpsRelative);

        final SkipTrackPointIterator actual = factory.create(givenTrack);

        final TrackPointReplacer actualPointReplacer = getPointReplacer(actual);
        assertSame(mockedPointReplacer, actualPointReplacer);

        final List<TrackPoint> actualPoints = getPoints(actual);
        assertSame(givenPoints, actualPoints);

        final double actualPointMinGpsRelative = getPointMinGpsRelative(actual);
        final double expectedPointMinGpsRelative = givenPointMinGpsRelative / 1000;
        assertEquals(expectedPointMinGpsRelative, actualPointMinGpsRelative, 0.);
    }

    private TrackPointReplacer getPointReplacer(final SkipTrackPointIterator iterator) {
        return getFieldValue(iterator, FIELD_NAME_POINT_REPLACER, TrackPointReplacer.class);
    }

    @SuppressWarnings("unchecked")
    private List<TrackPoint> getPoints(final SkipTrackPointIterator iterator) {
        return getFieldValue(iterator, FIELD_NAME_POINTS, List.class);
    }

    private double getPointMinGpsRelative(final SkipTrackPointIterator iterator) {
        return getFieldValue(iterator, FIELD_NAME_POINT_MIN_GPS_RELATIVE, Double.class);
    }
}
