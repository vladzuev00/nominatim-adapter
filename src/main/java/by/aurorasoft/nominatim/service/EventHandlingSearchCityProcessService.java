package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.crud.service.SearchingCitiesProcessService;
import by.aurorasoft.nominatim.service.factory.SearchingCitiesProcessFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status.*;

@Service
@RequiredArgsConstructor
public class EventHandlingSearchCityProcessService {
    private final SearchingCitiesProcessService searchingCitiesProcessService;
    private final CityService cityService;
    private final SearchingCitiesProcessFactory searchingCitiesProcessFactory;

    public SearchingCitiesProcess onStartSearchCities(AreaCoordinate areaCoordinate, double searchStep) {
        final SearchingCitiesProcess savedProcess = this.searchingCitiesProcessFactory
                .create(areaCoordinate, searchStep);
        return this.searchingCitiesProcessService.save(savedProcess);
    }

    @Transactional
    public void onSuccessFindCities(SearchingCitiesProcess process, Collection<City> foundCities) {
        this.searchingCitiesProcessService.updateHandledPointsAndStatus(process, process.getTotalPoints(), SUCCESS);
        this.cityService.saveAll(foundCities);
    }

    public void onFailedFindCities(SearchingCitiesProcess process) {
        this.searchingCitiesProcessService.updateStatus(process, ERROR);
    }
}
