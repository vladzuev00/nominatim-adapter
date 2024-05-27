package by.aurorasoft.distanceclassifier.service.cityscan;

import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.service.cityscan.scanner.CityScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Service
@RequiredArgsConstructor
public final class CityScanningService {
    private final CityScanner scanner;
    private final ExecutorService executorService = newSingleThreadExecutor();

    public void scan(final AreaCoordinate areaCoordinate) {
        executorService.execute(() -> scanner.scan(areaCoordinate));
    }
}
