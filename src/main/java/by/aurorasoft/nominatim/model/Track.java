package by.aurorasoft.nominatim.model;

import lombok.Value;

import java.util.List;

@Value
public class Track {
    List<TrackPoint> points;

    public TrackPoint getPoint(final int index) {
        return points.get(index);
    }
}
