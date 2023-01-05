package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.rest.controller.exception.ConstraintException;
import by.aurorasoft.nominatim.rest.mapper.CityControllerMapper;
import by.aurorasoft.nominatim.rest.model.CityRequest;
import by.aurorasoft.nominatim.rest.model.CityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.of;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityController {
    private final CityService service;
    private final CityControllerMapper mapper;

    @GetMapping
    public ResponseEntity<List<CityResponse>> findAll() {
        final List<City> foundCities = this.service.findAll();
        return ok(this.mapper.mapToResponses(foundCities));
    }

    @PostMapping
    public ResponseEntity<CityResponse> save(
            @Valid @RequestBody CityRequest request,
            @ApiIgnore Errors errors) {
        validate(errors);
        final City cityToBeSaved = this.mapper.mapToCity(request);
        final City savedCity = this.service.save(cityToBeSaved);
        return ok(this.mapper.mapToResponse(savedCity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CityRequest request,
            @ApiIgnore Errors errors) {
        validate(errors);
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

    private static void validate(Errors errors) {
        if (errors.hasErrors()) {
            throw new ConstraintException(errors);
        }
    }
}
