package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
import by.aurorasoft.nominatim.rest.controller.exception.ConstraintException;
import by.aurorasoft.nominatim.service.StartingSearchingCitiesProcessService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import static java.lang.Double.compare;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/searchCity")
@RequiredArgsConstructor
public class SearchCityController {
    private static final String EXCEPTION_MESSAGE_NOT_VALID_AREA_COORDINATE
            = "Left bottom point's coordinates should be less than right upper point's coordinates.";

    private final StartingSearchingCitiesProcessService startingSearchingCitiesProcessService;

    @PostMapping
    public ResponseEntity<SearchingCitiesProcess> start(
            @Valid @RequestBody SearchCityController.StartSearchingCitiesRequest request,
            @ApiIgnore Errors errors) {
        validate(request, errors);
        final SearchingCitiesProcess createdProcess = this.startingSearchingCitiesProcessService.start(
                request.getBbox(), request.getSearchStep());
        return ok(createdProcess);
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

    @Value
    private static class StartSearchingCitiesRequest {

        @NotNull
        @Valid
        AreaCoordinate bbox;

        @NotNull
        @DecimalMin(value = "0.01")
        @DecimalMax(value = "5")
        Double searchStep;

        @JsonCreator
        public StartSearchingCitiesRequest(@JsonProperty("bbox") AreaCoordinate bbox,
                                           @JsonProperty("searchStep") Double searchStep) {
            this.bbox = bbox;
            this.searchStep = searchStep;
        }
    }
}
