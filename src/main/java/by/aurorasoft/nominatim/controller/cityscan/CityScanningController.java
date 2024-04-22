package by.aurorasoft.nominatim.controller.cityscan;

import by.aurorasoft.nominatim.service.cityscan.CityScanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/cityScan")
@RequiredArgsConstructor
public class CityScanningController {
    private final CityScanningService service;
}
