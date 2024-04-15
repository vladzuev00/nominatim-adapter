package by.aurorasoft.nominatim.rest.mapper;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.model.CityType;
import by.aurorasoft.nominatim.model.NominatimReverseResponse;
import by.aurorasoft.nominatim.model.NominatimReverseResponse.ExtraTags;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Component;
import org.wololo.jts2geojson.GeoJSONReader;

@Component
@RequiredArgsConstructor
public final class NominatimReverseResponseToCityMapper {
    private final GeoJSONReader geoJSONReader;
    private final GeometryFactory geometryFactory;

    public City map(NominatimReverseResponse source) {
        final Geometry geometry = this.mapGeometry(source);
        return City.builder()
                .name(source.getName())
                .geometry(geometry)
                .type(identifyCityType(source))
                .boundingBox(geometry.getEnvelope())
                .build();
    }

    private Geometry mapGeometry(NominatimReverseResponse source) {
        return this.geoJSONReader.read(source.getGeojson(), this.geometryFactory);
    }

    private static CityType identifyCityType(NominatimReverseResponse source) {
        final ExtraTags extraTags = source.getExtratags();
        return extraTags != null ? CityType.findByJsonValue(extraTags.getCapital()) : null;
    }
}
