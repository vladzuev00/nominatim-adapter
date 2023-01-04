package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status;
import by.aurorasoft.nominatim.crud.service.SearchingCitiesProcessService;
import by.aurorasoft.nominatim.rest.mapper.SearchingCitiesProcessControllerMapper;
import by.aurorasoft.nominatim.rest.model.SearchingCitiesProcessResponse;
import by.aurorasoft.nominatim.rest.model.StartSearchingCitiesRequest;
import by.aurorasoft.nominatim.rest.validator.StartSearchingCitiesRequestValidator;
import by.aurorasoft.nominatim.service.StartingSearchingCitiesProcessService;
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
@RequestMapping("/searchCityTask")
@RequiredArgsConstructor
public class SearchCityProcessController {
    private final StartSearchingCitiesRequestValidator validator;
    private final StartingSearchingCitiesProcessService startingProcessService;
    private final SearchingCitiesProcessService processService;
    private final SearchingCitiesProcessControllerMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<SearchingCitiesProcessResponse> findById(@PathVariable Long id) {
        final Optional<SearchingCitiesProcess> optionalFoundProcess = this.processService
                .getByIdOptional(id);
        return of(optionalFoundProcess.map(this.mapper::mapToResponse));
    }

    @GetMapping
    public ResponseEntity<List<SearchingCitiesProcessResponse>> findByStatus(
            @RequestParam(name = "status") Status status) {
        final List<SearchingCitiesProcess> foundProcesses = this.processService
                .findByStatus(status);
        return ok(this.mapper.mapToResponses(foundProcesses));
    }

    @PostMapping
    public ResponseEntity<SearchingCitiesProcessResponse> start(
            @Valid @RequestBody StartSearchingCitiesRequest request,
            @ApiIgnore Errors errors) {
        this.validator.validate(request, errors);
        final SearchingCitiesProcess createdProcess = this.startingProcessService.start(
                request.getBbox(), request.getSearchStep());
        return ok(this.mapper.mapToResponse(createdProcess));
    }
}
