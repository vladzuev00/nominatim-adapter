package by.aurorasoft.nominatim.crud.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class NominatimReverseResponse {
    List<NominatimFeature> nominatimFeatures;

    @JsonCreator
    public NominatimReverseResponse(@JsonProperty("features") List<NominatimFeature> nominatimFeatures) {
        this.nominatimFeatures = nominatimFeatures;
    }
}
