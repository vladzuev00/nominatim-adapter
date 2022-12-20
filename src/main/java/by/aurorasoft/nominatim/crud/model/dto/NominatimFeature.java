package by.aurorasoft.nominatim.crud.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class NominatimFeature {
    NominatimProperties properties;
    NominatimBbox bbox;

    @JsonCreator
    public NominatimFeature(@JsonProperty("properties") NominatimProperties properties,
                            @JsonProperty("bbox") NominatimBbox bbox) {
        this.properties = properties;
        this.bbox = bbox;
    }
}
