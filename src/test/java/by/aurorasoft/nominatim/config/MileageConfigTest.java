package by.aurorasoft.nominatim.config;

import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.trackfilter.TrackFilter;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public final class MileageConfigTest {
    private final MileageConfig config = new MileageConfig();

    @Test
    public void trackFilterShouldBeCreated() {
        final TrackFilter actual = config.trackFilter();
        assertNotNull(actual);
    }

    @Test
    public void distanceCalculatorShouldBeCreated() {
        final DistanceCalculator actual = config.distanceCalculator();
        assertNotNull(actual);
    }
}
