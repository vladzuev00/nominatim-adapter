package by.aurorasoft.distanceclassifier.service.cityscan;

import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassCityFactory;
import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class CityScanningService {
    private final OverpassClient overpassClient;
    private final OverpassCityFactory cityFactory;
    private final CityService cityService;

    public void scan(final AreaCoordinate areaCoordinate) {
        final List<City> cities = findCities(areaCoordinate);
        cityService.saveAll(cities);
    }

    private List<City> findCities(final AreaCoordinate areaCoordinate) {
        return overpassClient.findCities(areaCoordinate)
                .getRelations()
                .stream()
                .map(cityFactory::create)
                .toList();
    }
}
