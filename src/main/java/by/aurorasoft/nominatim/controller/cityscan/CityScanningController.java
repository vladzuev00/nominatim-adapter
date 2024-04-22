package by.aurorasoft.nominatim.controller.cityscan;

import by.aurorasoft.nominatim.controller.cityscan.factory.AreaCoordinateFactory;
import by.aurorasoft.nominatim.controller.cityscan.model.AreaCoordinateRequest;
import by.aurorasoft.nominatim.model.AreaCoordinate;
import by.aurorasoft.nominatim.service.cityscan.CityScanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.noContent;

@Validated
@RestController
@RequestMapping("/api/v1/cityScan")
@RequiredArgsConstructor
public class CityScanningController {
    private final AreaCoordinateFactory areaCoordinateFactory;
    private final CityScanningService cityScanningService;

    @PostMapping
    public ResponseEntity<?> scan(@Valid @RequestBody final AreaCoordinateRequest request) {
        final AreaCoordinate areaCoordinate = areaCoordinateFactory.create(request);
        cityScanningService.scan(areaCoordinate);
        return noContent().build();
    }
}
