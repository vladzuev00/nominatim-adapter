package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.factory;

import by.aurorasoft.distanceclassifier.config.property.ClassifyingDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.ConnectingTrackPointIterator;
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
public final class UnionTrackPointIteratorFactoryTest {
    private static final String FIELD_NAME_POINTS = "points";
    private static final String FIELD_NAME_GPS_RELATIVE_THRESHOLD = "gpsRelativeThreshold";

    @Mock
    private ClassifyingDistanceProperty mockedProperty;

    private ConnectingTrackPointIteratorFactory factory;

    @Before
    public void initializeFactory() {
        factory = new ConnectingTrackPointIteratorFactory(mockedProperty);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void iteratorShouldBeCreated() {
        final Track givenTrack = mock(Track.class);

        final List<TrackPoint> givenPoints = mock(List.class);
        when(givenTrack.getPoints()).thenReturn(givenPoints);

        final double givenPointUnionGpsRelativeThreshold = 500.;
        when(mockedProperty.getPointMinGpsRelative()).thenReturn(givenPointUnionGpsRelativeThreshold);

        final ConnectingTrackPointIterator actual = factory.create(givenTrack);

        final List<TrackPoint> actualPoints = getPoints(actual);
        assertSame(givenPoints, actualPoints);

        final double actualGpsRelativeThreshold = getGpsRelativeThreshold(actual);
        assertEquals(givenPointUnionGpsRelativeThreshold, actualGpsRelativeThreshold, 0.);
    }

    @SuppressWarnings("unchecked")
    private List<TrackPoint> getPoints(final ConnectingTrackPointIterator iterator) {
        return getFieldValue(iterator, FIELD_NAME_POINTS, List.class);
    }

    private double getGpsRelativeThreshold(final ConnectingTrackPointIterator iterator) {
        return getFieldValue(iterator, FIELD_NAME_GPS_RELATIVE_THRESHOLD, Double.class);
    }
}
