package by.aurorasoft.nominatim.crud.repository;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status.ERROR;
import static by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SearchingCitiesProcessRepositoryTest extends AbstractContextTest {

    @Autowired
    private SearchingCitiesProcessRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO searching_cities_process "
            + "(id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 0.01, 10000, 1000, 'HANDLING')")
    public void processShouldBeFoundById() {
        super.startQueryCount();
        final SearchingCitiesProcessEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final Coordinate[] expectedCoordinates = new Coordinate[]{
                new CoordinateXY(1, 2),
                new CoordinateXY(3, 4),
                new CoordinateXY(5, 6),
                new CoordinateXY(6, 7),
                new CoordinateXY(1, 2)
        };
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .geometry(this.geometryFactory.createPolygon(expectedCoordinates))
                .searchStep(0.01)
                .totalPoints(10000)
                .handledPoints(1000)
                .status(HANDLING)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void processShouldBeSaved() {
        final Coordinate[] givenCoordinates = new Coordinate[]{
                new CoordinateXY(1, 2),
                new CoordinateXY(3, 4),
                new CoordinateXY(5, 6),
                new CoordinateXY(6, 7),
                new CoordinateXY(1, 2)
        };
        final SearchingCitiesProcessEntity givenProcess = SearchingCitiesProcessEntity.builder()
                .geometry(this.geometryFactory.createPolygon(givenCoordinates))
                .searchStep(0.01)
                .totalPoints(10000)
                .handledPoints(1000)
                .status(HANDLING)
                .build();

        super.startQueryCount();
        this.repository.save(givenProcess);
        super.checkQueryCount(1);
    }

    @Test
    @Sql(statements = "INSERT INTO searching_cities_process "
            + "(id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 0.01, 10000, 1000, 'HANDLING')")
    public void processShouldBeUpdatedByStatus() {
        super.startQueryCount();
        this.repository.updateStatus(255L, ERROR);
        super.checkQueryCount(1);

        final SearchingCitiesProcessEntity actual = this.repository.findById(255L).orElseThrow();

        final Coordinate[] expectedCoordinates = new Coordinate[]{
                new CoordinateXY(1, 2),
                new CoordinateXY(3, 4),
                new CoordinateXY(5, 6),
                new CoordinateXY(6, 7),
                new CoordinateXY(1, 2)
        };
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .geometry(this.geometryFactory.createPolygon(expectedCoordinates))
                .searchStep(0.01)
                .totalPoints(10000)
                .handledPoints(1000)
                .status(ERROR)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO searching_cities_process "
            + "(id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 0.01, 10000, 1000, 'HANDLING')")
    public void processShouldBeUpdatedByIncreasingHandledPoints() {
        super.startQueryCount();
        this.repository.increaseHandledPoints(255L, 100);
        super.checkQueryCount(1);

        final SearchingCitiesProcessEntity actual = this.repository.findById(255L).orElseThrow();

        final Coordinate[] expectedCoordinates = new Coordinate[]{
                new CoordinateXY(1, 2),
                new CoordinateXY(3, 4),
                new CoordinateXY(5, 6),
                new CoordinateXY(6, 7),
                new CoordinateXY(1, 2)
        };
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .geometry(this.geometryFactory.createPolygon(expectedCoordinates))
                .searchStep(0.01)
                .totalPoints(10000)
                .handledPoints(1100)
                .status(HANDLING)
                .build();
        checkEquals(expected, actual);
    }

    private static void checkEquals(final SearchingCitiesProcessEntity expected,
                                    final SearchingCitiesProcessEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getGeometry(), actual.getGeometry());
        assertEquals(expected.getSearchStep(), actual.getSearchStep(), 0.);
        assertEquals(expected.getTotalPoints(), actual.getTotalPoints());
        assertEquals(expected.getHandledPoints(), actual.getHandledPoints());
        assertSame(expected.getStatus(), actual.getStatus());
    }
}
