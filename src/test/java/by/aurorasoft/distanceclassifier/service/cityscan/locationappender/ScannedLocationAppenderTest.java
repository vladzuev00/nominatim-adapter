package by.aurorasoft.distanceclassifier.service.cityscan.locationappender;

import by.aurorasoft.distanceclassifier.crud.service.ScannedLocationService;
import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Polygon;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ScannedLocationAppenderTest {

    @Mock
    private GeometryService mockedGeometryService;

    @Mock
    private ScannedLocationService mockedLocationService;

    private ScannedLocationAppender appender;

    @Before
    public void initializeAppender() {
        appender = new ScannedLocationAppender(mockedGeometryService, mockedLocationService);
    }

    @Test
    public void locationShouldBeAppended() {
        final AreaCoordinate givenAreaCoordinate = mock(AreaCoordinate.class);

        final Polygon givenPolygon = mock(Polygon.class);
        when(mockedGeometryService.createPolygon(same(givenAreaCoordinate))).thenReturn(givenPolygon);

        appender.append(givenAreaCoordinate);

        verify(mockedLocationService, times(1)).append(same(givenPolygon));
    }
}
