package by.aurorasoft.nominatim.model;

import by.nhorushko.distancecalculator.LatLngAlt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
@Builder
public class TrackPoint implements LatLngAlt {
    Instant datetime;
    float latitude;
    float longitude;
    int altitude;
    int speed;
    boolean valid;
}
