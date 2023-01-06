package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status;
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

import java.util.Collection;
import java.util.List;

import static by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status.ERROR;
import static by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class EventHandlingSearchCityProcessServiceTest extends AbstractContextTest {

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

    @Captor
    private ArgumentCaptor<Status> statusArgumentCaptor;

    @Captor
    private ArgumentCaptor<Collection<City>> citiesArgumentCaptor;

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

        final SearchingCitiesProcess givenProcessToBeSaved = createProcess(null);
        when(this.mockedSearchingCitiesProcessFactory.create(any(AreaCoordinate.class), anyDouble()))
                .thenReturn(givenProcessToBeSaved);

        final SearchingCitiesProcess givenSavedProcess = createProcess(255L);
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
        final SearchingCitiesProcess givenProcess = createProcess(255L);
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
        final Exception givenException = mock(Exception.class);

        final String givenMessage = "message";
        when(givenException.getMessage()).thenReturn(givenMessage);

        this.eventHandlingSearchCityProcessService.onFailedFindCitiesBySubtask(givenException);

        verify(givenException, times(1)).getMessage();
        verify(givenException, times(1)).printStackTrace();
    }

    @Test
    public void eventOnSuccessFindingAllCitiesShouldBeHandledSuccessfully() {
        final SearchingCitiesProcess givenProcess = createProcess(255L);
        final Collection<City> givenFoundCities = List.of(createCity(1L), createCity(2L), createCity(3L));

        this.eventHandlingSearchCityProcessService.onSuccessFindAllCities(givenProcess, givenFoundCities);

        verify(this.mockedSearchingCitiesProcessService, times(1))
                .updateStatus(this.processArgumentCaptor.capture(), this.statusArgumentCaptor.capture());
        verify(this.mockedCityService, times(1))
                .saveAll(this.citiesArgumentCaptor.capture());

        assertSame(givenProcess, this.processArgumentCaptor.getValue());
        assertSame(SUCCESS, this.statusArgumentCaptor.getValue());
        assertEquals(givenFoundCities, this.citiesArgumentCaptor.getValue());
    }

    @Test
    public void eventOnFailedFindingAllCitiesShouldBeHandled() {
        final SearchingCitiesProcess givenProcess = createProcess(255L);

        final String givenExceptionMessage = "message";
        final Exception givenException = mock(Exception.class);
        when(givenException.getMessage()).thenReturn(givenExceptionMessage);

        this.eventHandlingSearchCityProcessService.onFailedFindAllCities(givenProcess, givenException);

        verify(this.mockedSearchingCitiesProcessService, times(1))
                .updateStatus(this.processArgumentCaptor.capture(), this.statusArgumentCaptor.capture());
        verify(givenException, times(1)).getMessage();
        verify(givenException, times(1)).printStackTrace();

        assertSame(givenProcess, this.processArgumentCaptor.getValue());
        assertSame(ERROR, this.statusArgumentCaptor.getValue());
    }

    private static SearchingCitiesProcess createProcess(Long id) {
        return SearchingCitiesProcess.builder()
                .id(id)
                .build();
    }

    private City createCity(Long id) {
        return City.builder()
                .id(id)
                .build();
    }
}
