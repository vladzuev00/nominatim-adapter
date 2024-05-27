package by.aurorasoft.distanceclassifier.service.cityscan;

import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.service.cityscan.scanner.CityScanner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ExecutorService;

import static by.aurorasoft.distanceclassifier.testutil.ReflectionUtil.setFieldValue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class CityScanningServiceTest {
    private static final String FIELD_NAME_EXECUTOR_SERVICE = "executorService";

    @Mock
    private CityScanner mockedScanner;

    @Mock
    private ExecutorService mockedExecutorService;

    private CityScanningService scanningService;

    @Captor
    private ArgumentCaptor<Runnable> taskArgumentCaptor;

    @Before
    public void initializeScanningService() {
        scanningService = new CityScanningService(mockedScanner);
        injectMockedExecutorService();
    }

    @Test
    public void scanningShouldBeStarted() {
        final AreaCoordinate givenAreaCoordinate = mock(AreaCoordinate.class);

        scanningService.scan(givenAreaCoordinate);

        verify(mockedExecutorService, times(1)).execute(taskArgumentCaptor.capture());
        taskArgumentCaptor.getValue().run();
        verify(mockedScanner, times(1)).scan(same(givenAreaCoordinate));
    }

    private void injectMockedExecutorService() {
        setFieldValue(scanningService, FIELD_NAME_EXECUTOR_SERVICE, mockedExecutorService);
    }
}
