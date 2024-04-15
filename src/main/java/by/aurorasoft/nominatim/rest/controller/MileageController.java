package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.rest.model.MileageRequest;
import by.aurorasoft.nominatim.rest.model.Mileage;
import by.aurorasoft.nominatim.service.mileage.MileageCalculatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/mileage")
@Validated
@RequiredArgsConstructor
public class MileageController {
    private final MileageCalculatingService mileageService;

    @PostMapping
    public ResponseEntity<Mileage> findMileage(@Valid @RequestBody MileageRequest mileageRequest) {
        return ok(this.mileageService.calculate(null, null));
    }
}
