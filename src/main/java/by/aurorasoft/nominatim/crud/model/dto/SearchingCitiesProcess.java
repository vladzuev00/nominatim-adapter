package by.aurorasoft.nominatim.crud.model.dto;

import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class SearchingCitiesProcess {
    Long id;
    double searchStep;
    int totalPoints;
    int handledPoints;
    Status status;
}
