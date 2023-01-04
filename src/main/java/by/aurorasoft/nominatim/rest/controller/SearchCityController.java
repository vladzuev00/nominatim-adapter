package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status;
import by.aurorasoft.nominatim.crud.service.SearchingCitiesProcessService;
import by.aurorasoft.nominatim.rest.controller.exception.ConstraintException;
import by.aurorasoft.nominatim.rest.model.StartSearchingCitiesRequest;
import by.aurorasoft.nominatim.service.StartingSearchingCitiesProcessService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import java.util.List;

import static java.lang.Double.compare;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.of;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/searchCityTask")
@RequiredArgsConstructor
public class SearchCityController {
    private static final String EXCEPTION_MESSAGE_NOT_VALID_AREA_COORDINATE
            = "Left bottom point's coordinates should be less than right upper point's coordinates.";

    private final StartingSearchingCitiesProcessService startingSearchingCitiesProcessService;
    private final SearchingCitiesProcessService searchingCitiesProcessService;
    private final GeoJSONWriter geoJSONWriter;

    @GetMapping("/{id}")
    public ResponseEntity<SearchingCitiesProcess> findById(@PathVariable Long id) {
        return of(this.searchingCitiesProcessService.getByIdOptional(id));
    }

    @GetMapping
    public ResponseEntity<List<SearchingCitiesProcessResponse>> findByStatus(
            @RequestParam(name = "status") Status status) {
        final List<SearchingCitiesProcess> foundProcesses = this.searchingCitiesProcessService
                .findByStatus(status);
        return ok(this.mapToResponses(foundProcesses));
    }

    @PostMapping
    public ResponseEntity<SearchingCitiesProcessResponse> start(
            @Valid @RequestBody StartSearchingCitiesRequest request,
            @ApiIgnore Errors errors) {
        validate(request, errors);
        final SearchingCitiesProcess createdProcess = this.startingSearchingCitiesProcessService.start(
                request.getBbox(), request.getSearchStep());
        return ok(this.mapToResponse(createdProcess));
    }

    private static void validate(StartSearchingCitiesRequest requestBody, Errors errors) {
        if (errors.hasErrors()) {
            throw new ConstraintException(errors);
        }
        if (!isValidAreaCoordinate(requestBody.getBbox())) {
            throw new ConstraintException(EXCEPTION_MESSAGE_NOT_VALID_AREA_COORDINATE);
        }
    }

    private static boolean isValidAreaCoordinate(AreaCoordinate research) {
        final Coordinate leftBottom = research.getLeftBottom();
        final Coordinate rightUpper = research.getRightUpper();
        return compare(leftBottom.getLatitude(), rightUpper.getLatitude()) <= 0
                && compare(leftBottom.getLongitude(), rightUpper.getLongitude()) <= 0;
    }

    private List<SearchingCitiesProcessResponse> mapToResponses(List<SearchingCitiesProcess> mapped) {
        return mapped.stream()
                .map(this::mapToResponse)
                .collect(toList());
    }

    private SearchingCitiesProcessResponse mapToResponse(SearchingCitiesProcess mapped) {
        return SearchingCitiesProcessResponse.builder()
                .id(mapped.getId())
                .geometry(this.geoJSONWriter.write(mapped.getGeometry()))
                .searchStep(mapped.getSearchStep())
                .totalPoints(mapped.getTotalPoints())
                .handledPoints(mapped.getHandledPoints())
                .status(mapped.getStatus())
                .build();
    }

    @Value
    @Builder
    @AllArgsConstructor
    private static class SearchingCitiesProcessResponse {
        Long id;
        Geometry geometry;
        double searchStep;
        long totalPoints;
        long handledPoints;
        Status status;
    }
}
