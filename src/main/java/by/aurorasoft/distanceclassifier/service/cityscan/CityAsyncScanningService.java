package by.aurorasoft.distanceclassifier.service.cityscan;

import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Service
@RequiredArgsConstructor
public final class CityAsyncScanningService {
    private final CityScanningService scanningService;
    private final ExecutorService executorService = newSingleThreadExecutor();

    public void scanAsync(final AreaCoordinate areaCoordinate) {
        executorService.execute(() -> scanningService.scan(areaCoordinate));
    }
}
