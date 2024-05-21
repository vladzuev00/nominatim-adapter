package by.aurorasoft.distanceclassifier.service.cityscan.overpass;

import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.model.CityType;
import by.aurorasoft.distanceclassifier.model.overpass.OverpassSearchCityResponse.Relation;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

import static java.util.Arrays.stream;

@Component
@RequiredArgsConstructor
public final class OverpassCityFactory {
    private final GeometryService geometryService;

    public City create(final Relation relation) {
        return City.builder()
                .name(getName(relation))
                .type(getType(relation))
                .geometry(getGeometry(relation))
                .build();
    }

    private CityType getType(final Relation relation) {
        return stream(CityType.values())
                .filter(type -> type.match(relation.getTags()))
                .findFirst()
                .orElseThrow(
                        () -> new OverpassCityCreatingException(
                                "Impossible to identify city's type in relation: %s".formatted(relation)
                        )
                );
    }

    private String getName(final Relation relation) {
        return relation.getTags().getName();
    }

    private CityGeometry getGeometry(final Relation relation) {
        final MultiPolygon geometry = geometryService.createMultiPolygon(relation);
        final Polygon boundingBox = geometryService.createPolygon(relation.getBounds());
        return new CityGeometry(geometry, boundingBox);
    }

    static final class OverpassCityCreatingException extends RuntimeException {

        @SuppressWarnings("unused")
        public OverpassCityCreatingException() {

        }

        public OverpassCityCreatingException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public OverpassCityCreatingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public OverpassCityCreatingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
