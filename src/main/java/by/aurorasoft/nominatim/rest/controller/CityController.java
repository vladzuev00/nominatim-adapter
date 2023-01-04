package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity.Type;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.rest.controller.exception.ConstraintException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.of;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;
    private final GeoJSONWriter geoJSONWriter;
    private final GeoJSONReader geoJSONReader;

    @GetMapping
    public ResponseEntity<List<CityResponse>> findAll() {
        final List<City> foundCities = this.cityService.findAll();
        return ok(this.mapToResponses(foundCities));
    }

    @PostMapping
    public ResponseEntity<CityResponse> save(
            @Valid @RequestBody CityRequest request,
            @ApiIgnore Errors errors) {
        validate(errors);
        final City cityToBeSaved = this.mapToCity(request);
        final City savedCity = this.cityService.save(cityToBeSaved);
        return ok(this.mapToResponse(savedCity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CityRequest request,
            @ApiIgnore Errors errors) {
        validate(errors);
        final City cityToBeUpdated = this.mapToCity(id, request);
        final City updatedCity = this.cityService.update(cityToBeUpdated);
        return ok(this.mapToResponse(updatedCity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CityResponse> remove(@PathVariable Long id) {
        final Optional<City> optionalRemovedCity = this.cityService.getByIdOptional(id);
        optionalRemovedCity.ifPresent(removedCity -> this.cityService.delete(removedCity.getId()));
        return of(optionalRemovedCity.map(this::mapToResponse));
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

    private static void validate(Errors errors) {
        if (errors.hasErrors()) {
            throw new ConstraintException(errors);
        }
    }

    private City mapToCity(CityRequest mapped) {
        return City.builder()
                .name(mapped.getName())
                .geometry(this.geoJSONReader.read(mapped.getGeometry()))
                .type(mapped.getType())
                .build();
    }

    private City mapToCity(Long id, CityRequest mapped) {
        return City.builder()
                .id(id)
                .name(mapped.getName())
                .geometry(this.geoJSONReader.read(mapped.getGeometry()))
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

    @Value
    private static class CityRequest {
        String name;
        Geometry geometry;
        Type type;

        @JsonCreator
        public CityRequest(@JsonProperty("name") String name,
                           @JsonProperty("geometry") Geometry geometry,
                           @JsonProperty("type") Type type) {
            this.name = name;
            this.geometry = geometry;
            this.type = type;
        }
    }
}
