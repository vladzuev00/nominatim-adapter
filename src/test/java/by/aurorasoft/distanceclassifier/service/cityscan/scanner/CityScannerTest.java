package by.aurorasoft.distanceclassifier.service.cityscan.scanner;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Relation;
import by.aurorasoft.distanceclassifier.service.cityscan.locationappender.ScannedLocationAppender;
import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassCityFactory;
import by.aurorasoft.distanceclassifier.service.cityscan.overpass.OverpassClient;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public final class CityScannerTest extends AbstractSpringBootTest {

    @MockBean
    private OverpassClient mockedOverpassClient;

    @MockBean
    private OverpassCityFactory mockedCityFactory;

    @MockBean
    private CityService mockedCityService;

    @MockBean
    private ScannedLocationAppender mockedScannedLocationAppender;

    @Autowired
    private CityScanner scanner;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void citiesShouldBeScanned() {
        final AreaCoordinate givenAreaCoordinate = mock(AreaCoordinate.class);

        mockExistingGeometries(
                createPolygon("POLYGON((3 2, 2.5 5, 6 5, 5 3, 3 2))"),
                createPolygon("POLYGON((8 3, 8 6, 11 6, 11 3, 8 3))")
        );

        final Relation firstGivenRelation = mock(Relation.class);
        final Relation secondGivenRelation = mock(Relation.class);
        final Relation thirdGivenRelation = mock(Relation.class);
        final Relation fourthGivenRelation = mock(Relation.class);
        final Relation fifthGivenRelation = mock(Relation.class);
        mockResponse(
                givenAreaCoordinate,
                firstGivenRelation,
                secondGivenRelation,
                thirdGivenRelation,
                fourthGivenRelation,
                fifthGivenRelation
        );

        mockCityForRelation(firstGivenRelation, "POLYGON((3 2, 2.5 5, 6 5, 5 3, 3 2))");
        mockCityForRelation(secondGivenRelation, "POLYGON((8 3, 8 6, 11 6, 11 3, 8 3))");
        final City thirdGivenCity = mockCityForRelation(thirdGivenRelation, "POLYGON((4 7.5, 4 8, 5 10.5, 7.5 11.5, 6.5 8.5, 4 7.5))");
        mockCityForRelation(fourthGivenRelation, "POLYGON((6 5, 5 3, 3 2, 2.5 5, 6 5))");
        final City fifthGivenCity = mockCityForRelation(fifthGivenRelation, "POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))");

        scanner.scan(givenAreaCoordinate);

        final List<City> expectedSavedCities = List.of(thirdGivenCity, fifthGivenCity);
        verify(mockedCityService, times(1)).saveAll(eq(expectedSavedCities));

        verify(mockedScannedLocationAppender, times(1)).append(same(givenAreaCoordinate));
    }

    private void mockExistingGeometries(final Geometry... geometries) {
        when(mockedCityService.findGeometries()).thenReturn(Set.of(geometries));
    }

    private void mockResponse(final AreaCoordinate areaCoordinate, final Relation... relations) {
        final OverpassSearchCityResponse response = new OverpassSearchCityResponse(List.of(relations));
        when(mockedOverpassClient.findCities(same(areaCoordinate))).thenReturn(response);
    }

    private City mockCityForRelation(final Relation relation, final String polygonText) {
        final City city = createCity(polygonText);
        when(mockedCityFactory.create(same(relation))).thenReturn(city);
        return city;
    }

    private City createCity(final String polygonText) {
        final Geometry geometry = createPolygon(polygonText);
        return City.builder()
                .geometry(
                        CityGeometry.builder()
                                .geometry(geometry)
                                .build()
                )
                .build();
    }

    private Polygon createPolygon(final String text) {
        return GeometryUtil.createPolygon(text, geometryFactory);
    }
}
