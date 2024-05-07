package by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator;

import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.pointlocator.TrackPointLocator;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.pointlocator.TrackPointLocatorFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static by.aurorasoft.distanceclassifier.testutil.ReflectionUtil.getFieldValue;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class ClassifiedDistanceAccumulatorFactoryTest {
    private static final String FIELD_NAME_POINT_LOCATOR = "pointLocator";

    @Mock
    private TrackPointLocatorFactory mockedPointLocatorFactory;

    private ClassifiedDistanceAccumulatorFactory accumulatorFactory;

    @Before
    public void initializeAccumulatorFactory() {
        accumulatorFactory = new ClassifiedDistanceAccumulatorFactory(mockedPointLocatorFactory);
    }

    @Test
    public void accumulatorShouldBeCreated() {
        final Track givenTrack = mock(Track.class);
        final int givenUrbanSpeedThreshold = 50;

        final TrackPointLocator givenPointLocator = mock(TrackPointLocator.class);
        when(mockedPointLocatorFactory.create(same(givenTrack), eq(givenUrbanSpeedThreshold)))
                .thenReturn(givenPointLocator);

        final ClassifiedDistanceAccumulator actual = accumulatorFactory.create(givenTrack, givenUrbanSpeedThreshold);

        final TrackPointLocator actualPointLocator = getPointLocator(actual);
        assertSame(givenPointLocator, actualPointLocator);
    }

    private TrackPointLocator getPointLocator(final ClassifiedDistanceAccumulator accumulator) {
        return getFieldValue(accumulator, FIELD_NAME_POINT_LOCATOR, TrackPointLocator.class);
    }
}
