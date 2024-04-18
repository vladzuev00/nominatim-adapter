package by.aurorasoft.nominatim.service.searchcity;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.model.AreaCoordinate;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse;
import by.aurorasoft.nominatim.service.searchcity.overpassturbo.OverpassTurboClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class SearchingCitiesService {
    private final OverpassTurboClient overpassTurboClient;

    public List<City> find(final AreaCoordinate areaCoordinate) {
//        final OverpassTurboSearchCityResponse response = overpassTurboClient.findCities(areaCoordinate);
        return null;
    }
}
