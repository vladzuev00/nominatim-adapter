//package by.aurorasoft.nominatim.crud.mapper;
//
//import by.aurorasoft.nominatim.base.AbstractSpringBootTest;
//import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
//import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity;
//import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status;
//import by.aurorasoft.nominatim.model.SearchingCitiesProcessStatus;
//import org.junit.Test;
//import org.locationtech.jts.geom.Coordinate;
//import org.locationtech.jts.geom.Geometry;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status.SUCCESS;
//import static by.aurorasoft.nominatim.model.SearchingCitiesProcessStatus.SUCCESS;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//
//public final class SearchingCitiesProcessMapperTest extends AbstractSpringBootTest {
//    private static final Coordinate[] GIVEN_GEOMETRY_COORDINATES = new Coordinate[]{
//            new Coordinate(1, 2),
//            new Coordinate(2, 3),
//            new Coordinate(3, 4),
//            new Coordinate(4, 5),
//            new Coordinate(1, 2)
//    };
//
//    @Autowired
//    private SearchingCitiesProcessMapper mapper;
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Test
//    public void dtoShouldBeMappedToEntity() {
//        final Long givenId = 255L;
//        final Geometry givenGeometry = createGivenGeometry();
//        final double givenSearchStep = 0.01;
//        final long givenTotalPoints = 100;
//        final long givenHandledPoints = 50;
//        final SearchingCitiesProcessStatus givenStatus = SUCCESS;
//        final SearchingCitiesProcess givenDto = SearchingCitiesProcess.builder()
//                .id(givenId)
//                .geometry(givenGeometry)
//                .searchStep(givenSearchStep)
//                .totalPoints(givenTotalPoints)
//                .handledPoints(givenHandledPoints)
//                .status(givenStatus)
//                .build();
//
//        final SearchingCitiesProcessEntity actual = mapper.toEntity(givenDto);
//        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
//                .id(givenId)
//                .geometry(givenGeometry)
//                .searchStep(givenSearchStep)
//                .totalPoints(givenTotalPoints)
//                .handledPoints(givenHandledPoints)
//                .status(givenStatus)
//                .build();
//        checkEquals(expected, actual);
//    }
//
//    @Test
//    public void entityShouldBeMappedToDto() {
//        final Long givenId = 255L;
//        final Geometry givenGeometry = createGivenGeometry();
//        final double givenSearchStep = 0.01;
//        final long givenTotalPoints = 100;
//        final long givenHandledPoints = 50;
//        final SearchingCitiesProcessStatus givenStatus = SUCCESS;
//        final SearchingCitiesProcessEntity givenEntity = SearchingCitiesProcessEntity.builder()
//                .id(givenId)
//                .geometry(givenGeometry)
//                .searchStep(givenSearchStep)
//                .totalPoints(givenTotalPoints)
//                .handledPoints(givenHandledPoints)
//                .status(givenStatus)
//                .build();
//
//        final SearchingCitiesProcess actual = mapper.toDto(givenEntity);
//        final SearchingCitiesProcess expected = SearchingCitiesProcess.builder()
//                .id(givenId)
//                .geometry(givenGeometry)
//                .searchStep(givenSearchStep)
//                .totalPoints(givenTotalPoints)
//                .handledPoints(givenHandledPoints)
//                .status(givenStatus)
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    private Geometry createGivenGeometry() {
//        return geometryFactory.createPolygon(GIVEN_GEOMETRY_COORDINATES);
//    }
//
//    private static void checkEquals(final SearchingCitiesProcessEntity expected,
//                                    final SearchingCitiesProcessEntity actual) {
//        assertEquals(expected.getId(), actual.getId());
//        assertEquals(expected.getGeometry(), actual.getGeometry());
//        assertEquals(expected.getSearchStep(), actual.getSearchStep(), 0.);
//        assertEquals(expected.getTotalPoints(), actual.getTotalPoints());
//        assertEquals(expected.getHandledPoints(), actual.getHandledPoints());
//        assertSame(expected.getStatus(), actual.getStatus());
//    }
//}
