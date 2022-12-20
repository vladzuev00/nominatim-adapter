package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.rest.client.NominatimService;
import by.aurorasoft.nominatim.rest.mapper.NominatimReverseResponseToCityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Double.compare;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.iterate;

@Service
@RequiredArgsConstructor
public final class SearchCityService {
    private final NominatimService nominatimService;
    private final NominatimReverseResponseToCityMapper mapper;
    private final CityService cityService;

    public void findInArea(AreaCoordinate areaCoordinate, double searchStep) {
        final Set<String> namesAlreadyFoundCities = new HashSet<>();
        final Set<City> foundCities = iterate(areaCoordinate.getLeftBottom(),
                currentCoordinate -> hasNextCoordinate(currentCoordinate, areaCoordinate, searchStep),
                currentCoordinate -> findNextCoordinate(currentCoordinate, areaCoordinate, searchStep))
                .map(this.nominatimService::reverse)
                .map(this.mapper::map)
                .peek(System.out::println)
                .filter(city -> city.getName() != null)
                .filter(city -> namesAlreadyFoundCities.add(city.getName()))
                .collect(toSet());
        this.cityService.saveAll(foundCities);
    }

    private static boolean hasNextCoordinate(Coordinate currentCoordinate, AreaCoordinate areaCoordinate,
                                             double searchStep) {
        return compare(currentCoordinate.getLatitude() + searchStep, areaCoordinate.getRightUpper().getLatitude()) <= 0
                || compare(currentCoordinate.getLongitude() + searchStep, areaCoordinate.getRightUpper().getLongitude()) <= 0;
    }

    private static Coordinate findNextCoordinate(Coordinate currentCoordinate, AreaCoordinate areaCoordinate,
                                                 double searchStep) {
        if (compare(currentCoordinate.getLatitude() + searchStep, areaCoordinate.getRightUpper().getLatitude()) > 0) {
            return new Coordinate(areaCoordinate.getLeftBottom().getLatitude(),
                    currentCoordinate.getLongitude() + searchStep);
        } else {
            return new Coordinate(currentCoordinate.getLatitude() + searchStep,
                    currentCoordinate.getLongitude());
        }
    }
}
