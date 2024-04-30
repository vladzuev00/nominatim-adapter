package by.aurorasoft.distanceclassifier.service.cityscan;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassCityFactory;
import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public final class CityScanningService {
    private final OverpassClient overpassClient;
    private final OverpassCityFactory cityFactory;
    private final CityService cityService;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void scan(final AreaCoordinate areaCoordinate) {
        overpassClient.findCities(areaCoordinate)
                .getRelations()
                .stream()
                .map(cityFactory::create)
                .collect(collectingAndThen(toList(), cityService::saveAll));
    }
}
