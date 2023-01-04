package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity.Type;
import by.aurorasoft.nominatim.crud.service.CityService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;
    private final GeoJSONWriter geoJSONWriter;

    @GetMapping
    public ResponseEntity<List<CityResponse>> findAll() {
        final List<City> foundCities = this.cityService.findAll();
        return ok(this.mapToResponses(foundCities));
    }

    private List<CityResponse> mapToResponses(List<City> mapped) {
        return mapped.stream()
                .map(this::mapToResponse)
                .collect(toList());
    }

    private CityResponse mapToResponse(City mapped) {
        return CityResponse.builder()
                .id(mapped.getId())
                .name(mapped.getName())
                .geometry(this.geoJSONWriter.write(mapped.getGeometry()))
                .type(mapped.getType())
                .build();
    }

    @Value
    @Builder
    @AllArgsConstructor
    private static class CityResponse {
        Long id;
        String name;
        Geometry geometry;
        Type type;
    }
}
