package by.aurorasoft.nominatim.service.searchcity.overpass;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.model.CityType;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Relation;
import by.aurorasoft.nominatim.service.geometry.GeometryService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class OverpassCityFactory {
    private final GeometryService geometryService;

    public City create(final Relation relation) {
        return City.builder()
                .name(relation.getName())
                .geometry(getGeometry(relation))
                .type(getType(relation))
                .boundingBox(getBoundingBox(relation))
                .build();
    }

    private Geometry getGeometry(final Relation relation) {
        return geometryService.createMultiPolygon(relation);
    }

    private CityType getType(final Relation relation) {

    }

    private Geometry getBoundingBox(final Relation relation) {
        return null;
    }
}