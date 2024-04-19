package by.aurorasoft.nominatim.service.searchcity.factory;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.model.CityType;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Relation;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CityFactory {
    private final GeometryFactory geometryFactory;

    public City create(final Relation relation) {
        return City.builder()
                .name(getName(relation))
                .geometry(getGeometry(relation))
                .type(getType(relation))
                .boundingBox(getBoundingBox(relation))
                .build();
    }

    private static String getName(final Relation relation) {
        return relation.getTags().getName();
    }

    private Geometry getGeometry(final Relation relation) {
        geometryFactory.createMultiLineString().
    }

    private CityType getType(final Relation relation) {

    }

    private Geometry getBoundingBox(final Relation relation) {

    }
}
