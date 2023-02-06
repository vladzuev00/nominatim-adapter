package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.rest.model.MileageRequest;
import by.aurorasoft.nominatim.rest.model.MileageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mileage")
public class MileageController {

    @PostMapping
    public ResponseEntity<MileageResponse> findMileage(@RequestBody MileageRequest mileageRequest) {
        return null;
    }
}
