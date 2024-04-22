package by.aurorasoft.nominatim.service.searchcity;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.model.AreaCoordinate;
import by.aurorasoft.nominatim.service.searchcity.overpass.OverpassCityFactory;
import by.aurorasoft.nominatim.service.searchcity.overpass.OverpassClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class SearchingCitiesService {
    private final OverpassClient overpassTurboClient;
    private final OverpassCityFactory cityFactory;

    public List<City> find(final AreaCoordinate areaCoordinate) {
        return overpassTurboClient.findCities(areaCoordinate)
                .getRelations()
                .stream()
                .map(cityFactory::create)
                .toList();
    }
}
