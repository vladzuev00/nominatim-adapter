package by.aurorasoft.distanceclassifier.controller.city;

import by.aurorasoft.distanceclassifier.controller.city.factory.CityResponseFactory;
import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static by.aurorasoft.distanceclassifier.util.PageRequestUtil.createRequestSortingById;
import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@RequestMapping("/api/v1/city")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;
    private final CityResponseFactory responseFactory;

    @GetMapping
    public ResponseEntity<Page<CityResponse>> findAll(@RequestParam(name = "pageNumber") @Min(0) final int pageNumber,
                                                      @RequestParam(name = "pageSize") @Min(1) final int pageSize) {
        final PageRequest request = createRequestSortingById(pageNumber, pageSize);
        final Page<CityResponse> page = cityService.findAll(request).map(responseFactory::create);
        return ok(page);
    }
}
