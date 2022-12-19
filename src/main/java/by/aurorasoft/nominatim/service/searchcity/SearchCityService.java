package by.aurorasoft.nominatim.service.searchcity;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.service.searchcity.searcherinside.CitiesSearcherInSide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class SearchCityService {

    private final List<CitiesSearcherInSide> citiesSearcherInSide;

    public Collection<City> findCitiesInArea(AreaCoordinate areaCoordinate, double searchStep) {
        return this.citiesSearcherInSide.stream()
                .map(searcher -> supplyAsync(() -> searcher.findCities(areaCoordinate, searchStep)))
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(toSet());
    }
}
