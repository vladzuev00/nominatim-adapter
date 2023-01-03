package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.crud.service.SearchingCitiesProcessService;
import by.aurorasoft.nominatim.service.factory.SearchingCitiesProcessFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class EventHandlingSearchCityProcessServiceTest {

    @Mock
    private SearchingCitiesProcessService mockedSearchingCitiesProcessService;

    @Mock
    private CityService mockedCityService;

    @Mock
    private SearchingCitiesProcessFactory mockedSearchingCitiesProcessFactory;

    private EventHandlingSearchCityProcessService eventHandlingSearchCityProcessService;

    @Captor
    private ArgumentCaptor<AreaCoordinate> areaCoordinateArgumentCaptor;

    @Captor
    private ArgumentCaptor<Double> doubleArgumentCaptor;

    @Captor
    private ArgumentCaptor<SearchingCitiesProcess> processArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Before
    public void initializeEventHandlingSearchCityProcessService() {
        this.eventHandlingSearchCityProcessService = new EventHandlingSearchCityProcessService(
                this.mockedSearchingCitiesProcessService,
                this.mockedCityService,
                this.mockedSearchingCitiesProcessFactory);
    }

    @Test
    public void eventOfStartingSearchingCitiesShouldBeHandled() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(52.959981, 25.903515),
                new Coordinate(52.998760, 25.992997));
        final double givenSearchStep = 0.01;

        final SearchingCitiesProcess givenProcessToBeSaved = SearchingCitiesProcess.builder().build();
        when(this.mockedSearchingCitiesProcessFactory.create(any(AreaCoordinate.class), anyDouble()))
                .thenReturn(givenProcessToBeSaved);

        final SearchingCitiesProcess givenSavedProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .build();
        when(this.mockedSearchingCitiesProcessService.save(any(SearchingCitiesProcess.class)))
                .thenReturn(givenSavedProcess);

        final SearchingCitiesProcess actual = this.eventHandlingSearchCityProcessService
                .onStartSearchCities(givenAreaCoordinate, givenSearchStep);
        assertSame(givenSavedProcess, actual);

        verify(this.mockedSearchingCitiesProcessFactory, times(1))
                .create(this.areaCoordinateArgumentCaptor.capture(), this.doubleArgumentCaptor.capture());
        verify(this.mockedSearchingCitiesProcessService, times(1))
                .save(this.processArgumentCaptor.capture());

        assertSame(givenAreaCoordinate, this.areaCoordinateArgumentCaptor.getValue());
        assertEquals(givenSearchStep, this.doubleArgumentCaptor.getValue(), 0.);
        assertSame(givenProcessToBeSaved, this.processArgumentCaptor.getValue());
    }

    @Test
    public void eventOfSuccessFindingCitiesBySubtaskShouldBeHandled() {
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .build();
        final long givenAmountHandledPoints = 100;

        this.eventHandlingSearchCityProcessService.onSuccessFindCitiesBySubtask(givenProcess,
                givenAmountHandledPoints);

        verify(this.mockedSearchingCitiesProcessService, times(1))
                .increaseHandledPoints(this.processArgumentCaptor.capture(), this.longArgumentCaptor.capture());
        assertSame(givenProcess, this.processArgumentCaptor.getValue());
        assertEquals(givenAmountHandledPoints, this.longArgumentCaptor.getValue().longValue());
    }

    @Test
    public void eventOfFailedFindingCitiesBySubtaskShouldBeHandled() {
        throw new RuntimeException();
    }
}
