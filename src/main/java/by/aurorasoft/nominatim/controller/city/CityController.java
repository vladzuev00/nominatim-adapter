package by.aurorasoft.nominatim.controller.city;

import by.aurorasoft.nominatim.controller.city.factory.CityFactory;
import by.aurorasoft.nominatim.controller.city.factory.CityResponseFactory;
import by.aurorasoft.nominatim.controller.city.model.CityRequest;
import by.aurorasoft.nominatim.controller.city.model.CityResponse;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static by.aurorasoft.nominatim.util.PageRequestUtil.createRequestSortingById;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@RequestMapping("/api/v1/city")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;
    private final CityResponseFactory responseFactory;
    private final CityFactory cityFactory;

    @GetMapping
    public ResponseEntity<Page<CityResponse>> findAll(@RequestParam(name = "pageNumber") @Min(0) final int pageNumber,
                                                      @RequestParam(name = "pageSize") @Min(1) final int pageSize) {
        final PageRequest request = createRequestSortingById(pageNumber, pageSize);
        final Page<CityResponse> page = cityService.findAll(request).map(responseFactory::create);
        return ok(page);
    }

    @PostMapping
    public ResponseEntity<CityResponse> save(@Valid @RequestBody final CityRequest request) {
        return executeMappingToResponse(() -> cityFactory.create(request), CityService::save);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> update(@PathVariable final Long id,
                                               @Valid @RequestBody final CityRequest request) {
        return executeMappingToResponse(() -> cityFactory.create(id, request), CityService::update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CityResponse> remove(@PathVariable final Long id) {
        cityService.delete(id);
        return noContent().build();
    }

    private ResponseEntity<CityResponse> executeMappingToResponse(final Supplier<City> citySupplier,
                                                                  final BiFunction<CityService, City, City> operation) {
        final City city = citySupplier.get();
        final City resultCity = operation.apply(cityService, city);
        final CityResponse response = responseFactory.create(resultCity);
        return ok(response);
    }
}
