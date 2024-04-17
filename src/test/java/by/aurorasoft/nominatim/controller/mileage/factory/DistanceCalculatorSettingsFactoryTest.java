package by.aurorasoft.nominatim.controller.mileage.factory;

import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import by.nhorushko.distancecalculator.DistanceCalculatorSettingsImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class DistanceCalculatorSettingsFactoryTest {
    private final DistanceCalculatorSettingsFactory factory = new DistanceCalculatorSettingsFactory();

    @Test
    public void settingsShouldBeCreated() {
        final int givenMinDetectionSpeed = 10;
        final int givenMaxMessageTimeout = 16;
        final MileageRequest givenRequest = createRequest(givenMinDetectionSpeed, givenMaxMessageTimeout);

        final DistanceCalculatorSettings actual = factory.create(givenRequest);
        final DistanceCalculatorSettings expected = new DistanceCalculatorSettingsImpl(
                givenMinDetectionSpeed,
                givenMaxMessageTimeout
        );
        assertEquals(expected, actual);
    }

    @SuppressWarnings("SameParameterValue")
    private static MileageRequest createRequest(final Integer minDetectionSpeed, final Integer maxMessageTimeout) {
        return MileageRequest.builder()
                .minDetectionSpeed(minDetectionSpeed)
                .maxMessageTimeout(maxMessageTimeout)
                .build();
    }
}
