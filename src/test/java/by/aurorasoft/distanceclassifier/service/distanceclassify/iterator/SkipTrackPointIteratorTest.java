package by.aurorasoft.distanceclassifier.service.distanceclassify.iterator;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.exception.TrackPointWrongOrderException;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.pointreplacer.TrackPointReplacer;
import by.nhorushko.classifieddistance.Distance;
import nl.altindag.log.LogCaptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static nl.altindag.log.LogCaptor.forClass;
import static org.apache.commons.collections4.IteratorUtils.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class SkipTrackPointIteratorTest {

    @Mock
    private TrackPointReplacer mockedPointReplacer;

    private final LogCaptor logCaptor = forClass(SkipTrackPointIterator.class);

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
        final SkipTrackPointIterator givenIterator = createIterator(
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

        final TrackPoint firstGivenReplacement = createPoint(new Distance(130, 130), new Distance(150, 150));
        final TrackPoint secondGivenReplacement = createPoint(new Distance(90, 220), new Distance(120, 270));
        final TrackPoint thirdGivenReplacement = createPoint(new Distance(260, 480), new Distance(290, 560));
        final TrackPoint fourthGivenReplacement = createPoint(new Distance(50, 730), new Distance(70, 840));

        mockReplacement(secondGivenPoint, thirdGivenPoint, firstGivenReplacement);
        mockReplacement(fourthGivenPoint, sixthGivenPoint, secondGivenReplacement);
        mockReplacement(seventhGivenPoint, ninthGivenPoint, thirdGivenReplacement);
        mockReplacement(eleventhGivenPoint, twelfthGivenPoint, fourthGivenReplacement);

        final List<TrackPoint> actual = toList(givenIterator);
        final List<TrackPoint> expected = List.of(
                firstGivenPoint,
                firstGivenReplacement,
                secondGivenReplacement,
                thirdGivenReplacement,
                tenthGivenPoint,
                fourthGivenReplacement
        );
        assertEquals(expected, actual);
    }

    @Test
    public void pointsShouldBeIteratedWithZeroPointMinGpsRelative() {
        final TrackPoint firstGivenPoint = createPoint(new Distance(0, 0), new Distance(0, 0));
        final TrackPoint secondGivenPoint = createPoint(new Distance(50, 50), new Distance(60, 60));
        final TrackPoint thirdGivenPoint = createPoint(new Distance(80, 130), new Distance(90, 150));
        final TrackPoint fourthGivenPoint = createPoint(new Distance(40, 170), new Distance(50, 200));
        final SkipTrackPointIterator givenIterator = createIterator(
                0,
                firstGivenPoint,
                secondGivenPoint,
                thirdGivenPoint,
                fourthGivenPoint
        );

        final List<TrackPoint> actual = toList(givenIterator);
        final List<TrackPoint> expected = List.of(firstGivenPoint, secondGivenPoint, thirdGivenPoint, fourthGivenPoint);
        assertEquals(expected, actual);

        verifyNoInteractions(mockedPointReplacer);
    }

    @Test
    public void pointsShouldBeIteratedWithMinGpsRelativeMoreThanAllTrack() {
        final TrackPoint firstGivenPoint = createPoint(new Distance(0, 0), new Distance(0, 0));
        final TrackPoint secondGivenPoint = createPoint(new Distance(50, 50), new Distance(60, 60));
        final TrackPoint thirdGivenPoint = createPoint(new Distance(80, 130), new Distance(90, 150));
        final TrackPoint fourthGivenPoint = createPoint(new Distance(40, 170), new Distance(50, 200));
        final SkipTrackPointIterator givenIterator = createIterator(
                180,
                firstGivenPoint,
                secondGivenPoint,
                thirdGivenPoint,
                fourthGivenPoint
        );

        final TrackPoint givenReplacement = createPoint(new Distance(170, 170), new Distance(200, 200));

        mockReplacement(secondGivenPoint, fourthGivenPoint, givenReplacement);

        final List<TrackPoint> actual = toList(givenIterator);
        final List<TrackPoint> expected = List.of(firstGivenPoint, givenReplacement);
        assertEquals(expected, actual);
    }

    @Test
    public void pointsShouldNotBeIteratedBecauseOfWrongOrder() {
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
        final SkipTrackPointIterator givenIterator = createIterator(
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

        final TrackPoint firstGivenReplacement = createPoint(new Distance(130, 130), new Distance(150, 150));

        mockReplacement(secondGivenPoint, thirdGivenPoint, firstGivenReplacement);
        mockWrongOrderReplacement(fourthGivenPoint, sixthGivenPoint);

        toListExpectingWrongOrderException(givenIterator);

        final List<String> actualLogs = logCaptor.getLogs();
        final List<String> expectedLogs = List.of(
                "Points with wrong order was found in sequence: ["
                        + "TrackPoint(coordinate=null, speed=0, gpsDistance=Distance(relative=40.0, absolute=170.0), odometerDistance=Distance(relative=50.0, absolute=200.0)), "
                        + "TrackPoint(coordinate=null, speed=0, gpsDistance=Distance(relative=30.0, absolute=200.0), odometerDistance=Distance(relative=40.0, absolute=240.0)), "
                        + "TrackPoint(coordinate=null, speed=0, gpsDistance=Distance(relative=20.0, absolute=220.0), odometerDistance=Distance(relative=30.0, absolute=270.0))"
                        + "]"
        );
        assertEquals(expectedLogs, actualLogs);
    }

    private TrackPoint createPoint(final Distance gpsDistance, final Distance odometerDistance) {
        return TrackPoint.builder()
                .gpsDistance(gpsDistance)
                .odometerDistance(odometerDistance)
                .build();
    }

    private SkipTrackPointIterator createIterator(final int pointMinGpsRelative, final TrackPoint... points) {
        return new SkipTrackPointIterator(mockedPointReplacer, List.of(points), pointMinGpsRelative);
    }

    private void mockReplacement(final TrackPoint first, final TrackPoint second, final TrackPoint result) {
        when(mockedPointReplacer.replace(same(first), same(second))).thenReturn(result);
    }

    private void mockWrongOrderReplacement(final TrackPoint first, final TrackPoint second) {
        when(mockedPointReplacer.replace(same(first), same(second))).thenThrow(TrackPointWrongOrderException.class);
    }

    private void toListExpectingWrongOrderException(final SkipTrackPointIterator iterator) {
        boolean exceptionArisen;
        try {
            toList(iterator);
            exceptionArisen = false;
        } catch (final TrackPointWrongOrderException exception) {
            exceptionArisen = true;
        }
        assertTrue(exceptionArisen);
    }
}
