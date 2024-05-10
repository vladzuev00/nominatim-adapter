package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public final class SignificantTrackPointIteratorFactory {
    private final double gpsThreshold;

    public SignificantTrackPointIteratorFactory(final double gpsThreshold) {
        this.gpsThreshold = gpsThreshold;
    }
}
