package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.rest.mapper.CityControllerMapper;
import by.aurorasoft.nominatim.rest.model.CityPageResponse;
import by.aurorasoft.nominatim.rest.model.CityRequest;
import by.aurorasoft.nominatim.rest.model.CityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.of;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
@Validated
public class CityController {
    private final CityService service;
    private final CityControllerMapper mapper;

    @GetMapping
    public ResponseEntity<CityPageResponse> findAll(
            @RequestParam(name = "pageNumber") @Min(0) @Max(3000000) int pageNumber,
            @RequestParam(name = "pageSize") @Min(1) @Max(3000000) int pageSize) {
        final List<City> foundCities = this.service.findAll(pageNumber, pageSize);
        return ok(this.mapper.mapToResponse(pageNumber, pageSize, foundCities));
    }

    @PostMapping
    public ResponseEntity<CityResponse> save(@Valid @RequestBody CityRequest request) {
        final City cityToBeSaved = this.mapper.mapToCity(request);
        final City savedCity = this.service.save(cityToBeSaved);
        return ok(this.mapper.mapToResponse(savedCity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CityRequest request) {
        final City cityToBeUpdated = this.mapper.mapToCity(id, request);
        final City updatedCity = this.service.update(cityToBeUpdated);
        return ok(this.mapper.mapToResponse(updatedCity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CityResponse> remove(@PathVariable Long id) {
        final Optional<City> optionalRemovedCity = this.service.getByIdOptional(id);
        optionalRemovedCity.ifPresent(removedCity -> this.service.delete(removedCity.getId()));
        return of(optionalRemovedCity.map(this.mapper::mapToResponse));
    }
}
