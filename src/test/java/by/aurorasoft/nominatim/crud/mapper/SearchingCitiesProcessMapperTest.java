package by.aurorasoft.nominatim.crud.mapper;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;

public final class SearchingCitiesProcessMapperTest extends AbstractContextTest {

    @Autowired
    private SearchingCitiesProcessMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Coordinate[] givenGeometryCoordinates = createGeometryCoordinates();
        final SearchingCitiesProcess givenDto = SearchingCitiesProcess.builder()
                .id(255L)
                .geometry(this.geometryFactory.createPolygon(givenGeometryCoordinates))
                .searchStep(0.01)
                .totalPoints(100)
                .handledPoints(50)
                .status()
    }

    @Test
    public void entityShouldBeMappedToDto() {
        throw new RuntimeException();
    }

    private static Coordinate[] createGeometryCoordinates() {
        return new Coordinate[]{
                new Coordinate(1, 2),
                new Coordinate(2, 3),
                new Coordinate(3, 4),
                new Coordinate(4, 5),
                new Coordinate(1, 2)
        };
    }
}
