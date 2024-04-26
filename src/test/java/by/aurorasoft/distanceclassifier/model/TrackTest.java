package by.aurorasoft.distanceclassifier.model;

import org.junit.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public final class TrackTest {

    @Test
    public void pointShouldBeGotByIndex() {
        final TrackPoint givenPoint = mock(TrackPoint.class);
        final Track givenTrack = new Track(List.of(givenPoint));

        final TrackPoint actual = givenTrack.getPoint(0);
        assertSame(givenPoint, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void pointShouldNotBeGotByIndex() {
        final Track givenTrack = new Track(emptyList());

        givenTrack.getPoint(0);
    }
}
