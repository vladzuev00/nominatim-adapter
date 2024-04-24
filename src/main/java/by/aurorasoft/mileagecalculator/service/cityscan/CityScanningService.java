package by.aurorasoft.mileagecalculator.service.cityscan;

import by.aurorasoft.mileagecalculator.crud.model.dto.City;
import by.aurorasoft.mileagecalculator.crud.service.CityService;
import by.aurorasoft.mileagecalculator.model.AreaCoordinate;
import by.aurorasoft.mileagecalculator.service.cityscan.overpass.OverpassCityFactory;
import by.aurorasoft.mileagecalculator.service.cityscan.overpass.OverpassClient;
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
