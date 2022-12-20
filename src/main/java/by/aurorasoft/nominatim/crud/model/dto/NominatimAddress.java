package by.aurorasoft.nominatim.crud.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Value;

import static lombok.AccessLevel.NONE;

@Value
@Getter(NONE)
public class NominatimAddress {
    String town;
    String city;

    @JsonCreator
    public NominatimAddress(@JsonProperty("town") String town, @JsonProperty("city") String city) {
        this.town = town;
        this.city = city;
    }

    public String findName() {
        return this.town != null ? this.town : this.city;
    }
}
