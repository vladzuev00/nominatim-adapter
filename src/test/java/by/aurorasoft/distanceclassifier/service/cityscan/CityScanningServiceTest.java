//package by.aurorasoft.distanceclassifier.service.cityscan;
//
//import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
//import by.aurorasoft.distanceclassifier.crud.model.dto.City;
//import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
//import by.aurorasoft.distanceclassifier.crud.service.CityService;
//import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
//import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse;
//import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Relation;
//import by.aurorasoft.distanceclassifier.service.cityscan.CityAsyncScanningService.CityScanningTask;
//import by.aurorasoft.distanceclassifier.service.cityscan.locationappender.ScannedLocationAppender;
//import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassCityFactory;
//import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassClient;
//import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
//import org.junit.Test;
//import org.locationtech.jts.geom.Geometry;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.Polygon;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.ExecutorService;
//
//import static by.aurorasoft.distanceclassifier.testutil.ReflectionUtil.getFieldValue;
//import static by.aurorasoft.distanceclassifier.testutil.ReflectionUtil.setFieldValue;
//import static org.junit.Assert.assertSame;
//import static org.mockito.Mockito.*;
//
//public final class CityScanningServiceTest extends AbstractSpringBootTest {
//    private static final String FIELD_NAME_EXECUTOR_SERVICE = "executorService";
//    private static final String FIELD_NAME_AREA_COORDINATE = "areaCoordinate";
//
//    @MockBean
//    private OverpassClient mockedOverpassClient;
//
//    @MockBean
//    private OverpassCityFactory mockedCityFactory;
//
//    @MockBean
//    private CityService mockedCityService;
//
//    @MockBean
//    private ScannedLocationAppender mockedScannedLocationAppender;
//
//    @Autowired
//    private CityAsyncScanningService scanningService;
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Captor
//    private ArgumentCaptor<CityScanningTask> taskArgumentCaptor;
//
//    @Test
//    public void citiesShouldBeScannedByTask() {
//        final AreaCoordinate givenAreaCoordinate = mock(AreaCoordinate.class);
//        final CityScanningTask givenTask = createTask(givenAreaCoordinate);
//
//        mockExistingGeometries(
//                createPolygon("POLYGON((3 2, 2.5 5, 6 5, 5 3, 3 2))"),
//                createPolygon("POLYGON((8 3, 8 6, 11 6, 11 3, 8 3))")
//        );
//
//        final Relation firstGivenRelation = mock(Relation.class);
//        final Relation secondGivenRelation = mock(Relation.class);
//        final Relation thirdGivenRelation = mock(Relation.class);
//        final Relation fourthGivenRelation = mock(Relation.class);
//        final Relation fifthGivenRelation = mock(Relation.class);
//        mockResponse(
//                givenAreaCoordinate,
//                firstGivenRelation,
//                secondGivenRelation,
//                thirdGivenRelation,
//                fourthGivenRelation,
//                fifthGivenRelation
//        );
//
//        mockCityForRelation(firstGivenRelation, 255L, "POLYGON((3 2, 2.5 5, 6 5, 5 3, 3 2))");
//        mockCityForRelation(secondGivenRelation, 256L, "POLYGON((8 3, 8 6, 11 6, 11 3, 8 3))");
//        final City thirdGivenCity = mockCityForRelation(thirdGivenRelation, 257L, "POLYGON((4 7.5, 4 8, 5 10.5, 7.5 11.5, 6.5 8.5, 4 7.5))");
//        mockCityForRelation(fourthGivenRelation, 258L, "POLYGON((6 5, 5 3, 3 2, 2.5 5, 6 5))");
//        final City fifthGivenCity = mockCityForRelation(fifthGivenRelation, 259L, "POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))");
//
//        givenTask.run();
//
//        final List<City> expectedSavedCities = List.of(thirdGivenCity, fifthGivenCity);
//        verify(mockedCityService, times(1)).saveAll(eq(expectedSavedCities));
//
//        verify(mockedScannedLocationAppender, times(1)).append(same(givenAreaCoordinate));
//    }
//
//    @Test
//    public void citiesShouldBeScannedAsync() {
//        final AreaCoordinate givenAreaCoordinate = mock(AreaCoordinate.class);
//        final ExecutorService givenExecutorService = mockExecutorService();
//
//        scanningService.scanAsync(givenAreaCoordinate);
//
//        verify(givenExecutorService, times(1)).execute(taskArgumentCaptor.capture());
//
//        final CityScanningTask capturedTask = taskArgumentCaptor.getValue();
//        final AreaCoordinate actualAreaCoordinate = getAreaCoordinate(capturedTask);
//        assertSame(givenAreaCoordinate, actualAreaCoordinate);
//    }
//
//    private CityScanningTask createTask(final AreaCoordinate areaCoordinate) {
//        return scanningService.new CityScanningTask(areaCoordinate);
//    }
//
//    private void mockExistingGeometries(final Geometry... geometries) {
//        when(mockedCityService.findGeometries()).thenReturn(Set.of(geometries));
//    }
//
//    private void mockResponse(final AreaCoordinate areaCoordinate, final Relation... relations) {
//        final OverpassSearchCityResponse response = new OverpassSearchCityResponse(List.of(relations));
//        when(mockedOverpassClient.findCities(same(areaCoordinate))).thenReturn(response);
//    }
//
//    private City mockCityForRelation(final Relation relation, final Long id, final String polygonText) {
//        final City city = createCity(id, polygonText);
//        when(mockedCityFactory.create(same(relation))).thenReturn(city);
//        return city;
//    }
//
//    private City createCity(final Long id, final String polygonText) {
//        final Geometry geometry = createPolygon(polygonText);
//        return City.builder()
//                .id(id)
//                .geometry(
//                        CityGeometry.builder()
//                                .geometry(geometry)
//                                .build()
//                )
//                .build();
//    }
//
//    private Polygon createPolygon(final String text) {
//        return GeometryUtil.createPolygon(text, geometryFactory);
//    }
//
//    private ExecutorService mockExecutorService() {
//        final ExecutorService executorService = mock(ExecutorService.class);
//        setFieldValue(scanningService, FIELD_NAME_EXECUTOR_SERVICE, executorService);
//        return executorService;
//    }
//
//    private AreaCoordinate getAreaCoordinate(final CityScanningTask task) {
//        return getFieldValue(task, FIELD_NAME_AREA_COORDINATE, AreaCoordinate.class);
//    }
//}
