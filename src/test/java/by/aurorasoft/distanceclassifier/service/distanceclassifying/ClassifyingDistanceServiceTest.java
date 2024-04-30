package by.aurorasoft.distanceclassifier.service.distanceclassifying;

import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.ClassifiedDistanceAccumulator;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.ClassifiedDistanceAccumulatorFactory;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ClassifyingDistanceServiceTest {

    @Mock
    private ClassifiedDistanceAccumulatorFactory mockedDistanceAccumulatorFactory;

    private ClassifyingDistanceService service;

    @Captor
    private ArgumentCaptor<TrackPoint> pointArgumentCaptor;

    @Before
    public void initializeService() {
        service = new ClassifyingDistanceService(mockedDistanceAccumulatorFactory);
    }

    @Test
    public void distanceShouldBeClassified() {
        final TrackPoint firstGivenPoint = mock(TrackPoint.class);
        final TrackPoint secondGivenPoint = mock(TrackPoint.class);
        final List<TrackPoint> givenPoints = List.of(firstGivenPoint, secondGivenPoint);
        final Track givenTrack = new Track(givenPoints);

        final int givenUrbanSpeedThreshold = 60;

        final ClassifiedDistanceAccumulator givenAccumulator = mock(ClassifiedDistanceAccumulator.class);
        when(mockedDistanceAccumulatorFactory.create(same(givenTrack), eq(givenUrbanSpeedThreshold)))
                .thenReturn(givenAccumulator);

        final ClassifiedDistanceStorage givenStorage = mock(ClassifiedDistanceStorage.class);
        when(givenAccumulator.get()).thenReturn(givenStorage);

        final ClassifiedDistanceStorage actual = service.classify(givenTrack, givenUrbanSpeedThreshold);
        assertSame(givenStorage, actual);

        verify(givenAccumulator, times(givenPoints.size())).accumulate(pointArgumentCaptor.capture());

        final List<TrackPoint> actualCapturedPoints = pointArgumentCaptor.getAllValues();
        final List<TrackPoint> expectedCapturedPoints = List.of(firstGivenPoint, secondGivenPoint);
        assertEquals(expectedCapturedPoints, actualCapturedPoints);
    }
}
