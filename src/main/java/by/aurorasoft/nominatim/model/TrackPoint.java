package by.aurorasoft.nominatim.model;

import by.nhorushko.distancecalculator.LatLngAlt;
import lombok.Value;

import java.time.Instant;

@Value
public class TrackPoint implements LatLngAlt {
    Instant datetime;
    float latitude;
    float longitude;
    int altitude;
    int speed;
    boolean valid;
}
