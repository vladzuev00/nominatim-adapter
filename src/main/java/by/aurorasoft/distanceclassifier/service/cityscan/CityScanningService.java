package by.aurorasoft.distanceclassifier.service.cityscan;

import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.service.cityscan.locationappender.ScannedLocationAppender;
import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassCityFactory;
import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassClient;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public final class CityScanningService {
    private final OverpassClient overpassClient;
    private final OverpassCityFactory cityFactory;
    private final CityService cityService;
    private final ScannedLocationAppender scannedLocationAppender;
    private final ExecutorService executorService = newSingleThreadExecutor();

    public void scanAsync(final AreaCoordinate areaCoordinate) {
        executorService.execute(new CityScanningTask(areaCoordinate));
    }

    @RequiredArgsConstructor
    final class CityScanningTask implements Runnable {
        private final AreaCoordinate areaCoordinate;

        @Override
        public void run() {
            saveNotExistingCities(areaCoordinate);
            scannedLocationAppender.append(areaCoordinate);
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        private void saveNotExistingCities(final AreaCoordinate areaCoordinate) {
            final Set<Geometry> existingGeometries = cityService.findGeometries();
            overpassClient.findCities(areaCoordinate)
                    .getRelations()
                    .stream()
                    .map(cityFactory::create)
                    .filter(city -> !isContainGeometry(existingGeometries, city))
                    .collect(collectingAndThen(toList(), cityService::saveAll));
        }

        private boolean isContainGeometry(final Set<Geometry> geometries, final City city) {
            return geometries.stream().anyMatch(geometry -> geometry.equalsTopo(city.getGeometry().getGeometry()));
        }
    }
}
