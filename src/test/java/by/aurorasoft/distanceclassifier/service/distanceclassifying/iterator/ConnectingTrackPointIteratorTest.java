package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.replacer.TrackPointConnector;
import by.nhorushko.classifieddistance.Distance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.apache.commons.collections4.IteratorUtils.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class ConnectingTrackPointIteratorTest {

    @Mock
    private TrackPointConnector mockedPointConnected;

    @Test
    public void pointsShouldBeIterated() {
        final TrackPoint firstGivenPoint = createPoint(new Distance(0, 0), new Distance(0, 0));
        final TrackPoint secondGivenPoint = createPoint(new Distance(50, 50), new Distance(60, 60));
        final TrackPoint thirdGivenPoint = createPoint(new Distance(80, 130), new Distance(90, 150));
        final TrackPoint fourthGivenPoint = createPoint(new Distance(40, 170), new Distance(50, 200));
        final TrackPoint fifthGivenPoint = createPoint(new Distance(30, 200), new Distance(40, 240));
        final TrackPoint sixthGivenPoint = createPoint(new Distance(20, 220), new Distance(30, 270));
        final TrackPoint seventhGivenPoint = createPoint(new Distance(25, 245), new Distance(35, 305));
        final TrackPoint eighthGivenPoint = createPoint(new Distance(35, 280), new Distance(45, 350));
        final TrackPoint ninthGivenPoint = createPoint(new Distance(200, 480), new Distance(210, 560));
        final TrackPoint tenthGivenPoint = createPoint(new Distance(200, 680), new Distance(210, 770));
        final TrackPoint eleventhGivenPoint = createPoint(new Distance(20, 700), new Distance(30, 800));
        final TrackPoint twelfthGivenPoint = createPoint(new Distance(30, 730), new Distance(40, 840));
        final ConnectingTrackPointIterator givenIterator = createIterator(
                75,
                firstGivenPoint,
                secondGivenPoint,
                thirdGivenPoint,
                fourthGivenPoint,
                fifthGivenPoint,
                sixthGivenPoint,
                seventhGivenPoint,
                eighthGivenPoint,
                ninthGivenPoint,
                tenthGivenPoint,
                eleventhGivenPoint,
                twelfthGivenPoint
        );

        final TrackPoint secondGivenConnectionResult = createPoint(new Distance(130, 130), new Distance(150, 150));
        final TrackPoint thirdGivenConnectionResult = createPoint(new Distance(90, 220), new Distance(120, 270));
        final TrackPoint fourthGivenConnectionResult = createPoint(new Distance(260, 480), new Distance(290, 560));
        final TrackPoint fifthGivenConnectionResult = createPoint(new Distance(200, 680), new Distance(210, 770));
        final TrackPoint sixthGivenConnectionResult = createPoint(new Distance(50, 730), new Distance(70, 840));

        mockConnectionResult(firstGivenPoint, firstGivenPoint, firstGivenPoint);
        mockConnectionResult(firstGivenPoint, thirdGivenPoint, secondGivenConnectionResult);
        mockConnectionResult(thirdGivenPoint, sixthGivenPoint, thirdGivenConnectionResult);
        mockConnectionResult(sixthGivenPoint, ninthGivenPoint, fourthGivenConnectionResult);
        mockConnectionResult(ninthGivenPoint, tenthGivenPoint, fifthGivenConnectionResult);
        mockConnectionResult(tenthGivenPoint, twelfthGivenPoint, sixthGivenConnectionResult);

        final List<TrackPoint> actual = toList(givenIterator);
        final List<TrackPoint> expected = List.of(
                firstGivenPoint,
                secondGivenConnectionResult,
                thirdGivenConnectionResult,
                fourthGivenConnectionResult,
                fifthGivenConnectionResult,
                sixthGivenConnectionResult
        );
        assertEquals(expected, actual);
    }

    @Test
    public void pointsShouldBeIteratedWithZeroPointMinGpsRelative() {
        final TrackPoint firstGivenPoint = createPoint(new Distance(0, 0), new Distance(0, 0));
        final TrackPoint secondGivenPoint = createPoint(new Distance(50, 50), new Distance(60, 60));
        final TrackPoint thirdGivenPoint = createPoint(new Distance(80, 130), new Distance(90, 150));
        final TrackPoint fourthGivenPoint = createPoint(new Distance(40, 170), new Distance(50, 200));
        final ConnectingTrackPointIterator givenIterator = createIterator(
                0,
                firstGivenPoint,
                secondGivenPoint,
                thirdGivenPoint,
                fourthGivenPoint
        );

        mockConnectionResult(firstGivenPoint, firstGivenPoint, firstGivenPoint);
        mockConnectionResult(secondGivenPoint, secondGivenPoint, secondGivenPoint);
        mockConnectionResult(thirdGivenPoint, thirdGivenPoint, thirdGivenPoint);
        mockConnectionResult(fourthGivenPoint, fourthGivenPoint, fourthGivenPoint);

        final List<TrackPoint> actual = toList(givenIterator);
        final List<TrackPoint> expected = List.of(
                firstGivenPoint,
                secondGivenPoint,
                thirdGivenPoint,
                fourthGivenPoint
        );
        assertEquals(expected, actual);
    }

    private TrackPoint createPoint(final Distance gpsDistance, final Distance odometerDistance) {
        return TrackPoint.builder()
                .gpsDistance(gpsDistance)
                .odometerDistance(odometerDistance)
                .build();
    }

    private ConnectingTrackPointIterator createIterator(final int pointMinGpsRelative, final TrackPoint... points) {
        return new ConnectingTrackPointIterator(mockedPointConnected, List.of(points), pointMinGpsRelative);
    }

    private void mockConnectionResult(final TrackPoint first, final TrackPoint second, final TrackPoint result) {
        when(mockedPointConnected.connect(same(first), same(second))).thenReturn(result);
    }
}
