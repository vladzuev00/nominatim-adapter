package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.factory;

import by.aurorasoft.distanceclassifier.config.property.ClassifyingDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.SkippingTrackPointIterator;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.pointreplacer.TrackPointReplacer;
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
public final class SkippingTrackPointIteratorFactoryTest {
    private static final String FIELD_NAME_POINT_REPLACER = "pointReplacer";
    private static final String FIELD_NAME_POINTS = "points";
    private static final String FIELD_NAME_POINT_MIN_GPS_RELATIVE = "pointMinGpsRelative";

    @Mock
    private TrackPointReplacer mockedPointReplacer;

    @Mock
    private ClassifyingDistanceProperty mockedProperty;

    private SkippingTrackPointIteratorFactory factory;

    @Before
    public void initializeFactory() {
        factory = new SkippingTrackPointIteratorFactory(mockedPointReplacer, mockedProperty);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void iteratorShouldBeCreated() {
        final Track givenTrack = mock(Track.class);

        final List<TrackPoint> givenPoints = mock(List.class);
        when(givenTrack.getPoints()).thenReturn(givenPoints);

        final double givenPointMinGpsRelative = 500.;
        when(mockedProperty.getPointMinGpsRelative()).thenReturn(givenPointMinGpsRelative);

        final SkippingTrackPointIterator actual = factory.create(givenTrack);

        final TrackPointReplacer actualPointReplacer = getPointReplacer(actual);
        assertSame(mockedPointReplacer, actualPointReplacer);

        final List<TrackPoint> actualPoints = getPoints(actual);
        assertSame(givenPoints, actualPoints);

        final double actualPointMinGpsRelative = getPointMinGpsRelative(actual);
        assertEquals(givenPointMinGpsRelative, actualPointMinGpsRelative, 0.);
    }

    private TrackPointReplacer getPointReplacer(final SkippingTrackPointIterator iterator) {
        return getFieldValue(iterator, FIELD_NAME_POINT_REPLACER, TrackPointReplacer.class);
    }

    @SuppressWarnings("unchecked")
    private List<TrackPoint> getPoints(final SkippingTrackPointIterator iterator) {
        return getFieldValue(iterator, FIELD_NAME_POINTS, List.class);
    }

    private double getPointMinGpsRelative(final SkippingTrackPointIterator iterator) {
        return getFieldValue(iterator, FIELD_NAME_POINT_MIN_GPS_RELATIVE, Double.class);
    }
}
