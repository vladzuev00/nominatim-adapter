package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Service
@Slf4j
public class StartingSearchingCitiesProcessService {
    private static final String LOG_SUCCESS_SEARCH_CITIES
            = "Process searching cities has been finished successfully.";
    private static final String LOG_TEMPLATE_FAILURE_SEARCH_CITIES
            = "Process searching cities has been failed. Exception: {}";

    private final SearchCityService searchCityService;
    private final EventHandlingSearchCityProcessService eventHandlingSearchCityProcessService;
    private final ExecutorService executorService;

    public StartingSearchingCitiesProcessService(SearchCityService searchCityService,
                                                 EventHandlingSearchCityProcessService eventHandlingSearchCityProcessService,
                                                 @Qualifier("executorServiceToSearchCities") ExecutorService executorService) {
        this.searchCityService = searchCityService;
        this.eventHandlingSearchCityProcessService = eventHandlingSearchCityProcessService;
        this.executorService = executorService;
    }

    public SearchingCitiesProcess start(AreaCoordinate areaCoordinate, double searchStep) {
        final SearchingCitiesProcess process = this.eventHandlingSearchCityProcessService
                .onStartSearchCities(areaCoordinate, searchStep);
        supplyAsync(() -> this.searchCityService.findInArea(areaCoordinate, searchStep), this.executorService)
                .whenComplete((foundCities, exception) -> {
                    if (exception == null) {
                        log.info(LOG_SUCCESS_SEARCH_CITIES);
                        this.eventHandlingSearchCityProcessService.onSuccessFindCities(process, foundCities);
                    } else {
                        log.error(LOG_TEMPLATE_FAILURE_SEARCH_CITIES, exception.getMessage());
                        this.eventHandlingSearchCityProcessService.onFailedFindCities(process);
                    }
                });
        return process;
    }
}
