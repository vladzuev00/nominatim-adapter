package by.aurorasoft.nominatim.crud.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class NominatimProperties {
    NominatimAddress address;

    @JsonCreator
    public NominatimProperties(@JsonProperty("address") NominatimAddress address) {
        this.address = address;
    }
}
