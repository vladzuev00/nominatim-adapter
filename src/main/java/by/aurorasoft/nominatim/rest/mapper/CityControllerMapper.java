package by.aurorasoft.nominatim.rest.mapper;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.rest.model.CityRequest;
import by.aurorasoft.nominatim.rest.model.CityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public final class CityControllerMapper {
    private final GeoJSONWriter geoJSONWriter;
    private final GeoJSONReader geoJSONReader;

    public CityResponse mapToResponse(City mapped) {
        return CityResponse.builder()
                .id(mapped.getId())
                .name(mapped.getName())
                .geometry(this.geoJSONWriter.write(mapped.getGeometry()))
                .type(mapped.getType())
                .build();
    }

    public List<CityResponse> mapToResponses(List<City> mapped) {
        return mapped.stream()
                .map(this::mapToResponse)
                .collect(toList());
    }

    public City mapToCity(CityRequest mapped) {
        return City.builder()
                .name(mapped.getName())
                .geometry(this.geoJSONReader.read(mapped.getGeometry()))
                .type(mapped.getType())
                .build();
    }

    public City mapToCity(Long id, CityRequest mapped) {
        return City.builder()
                .id(id)
                .name(mapped.getName())
                .geometry(this.geoJSONReader.read(mapped.getGeometry()))
                .type(mapped.getType())
                .build();
    }
}
