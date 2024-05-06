package by.aurorasoft.distanceclassifier.service.cityscan;

import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.service.cityscan.locationappender.ScannedLocationAppender;
import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassCityFactory;
import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassClient;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class CityScanningService {
    private final OverpassClient overpassClient;
    private final OverpassCityFactory cityFactory;
    private final CityService cityService;
    private final ScannedLocationAppender scannedLocationAppender;

    @Transactional(readOnly = true)
    public void scan(final AreaCoordinate areaCoordinate) {
        saveNotExistingCities(areaCoordinate);
        scannedLocationAppender.append(areaCoordinate);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveNotExistingCities(final AreaCoordinate areaCoordinate) {
        final Set<Geometry> existingCityGeometries = findExistingCityGeometries();
        overpassClient.findCities(areaCoordinate)
                .getRelations()
                .stream()
                .map(cityFactory::create)
                .filter(city -> !isContainCityGeometry(existingCityGeometries, city))
                .collect(collectingAndThen(toList(), cityService::saveAll));
    }

    private Set<Geometry> findExistingCityGeometries() {
        try (final Stream<CityGeometry> geometries = cityService.findGeometries()) {
            return geometries.map(CityGeometry::getGeometry).collect(toUnmodifiableSet());
        }
    }

    private boolean isContainCityGeometry(final Set<Geometry> geometries, final City city) {
        return geometries.stream().anyMatch(geometry -> geometry.equalsTopo(city.getGeometry().getGeometry()));
    }
}
