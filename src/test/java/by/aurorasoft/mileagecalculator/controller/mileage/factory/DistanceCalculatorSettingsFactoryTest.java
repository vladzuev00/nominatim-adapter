package by.aurorasoft.mileagecalculator.controller.mileage.factory;

import by.aurorasoft.mileagecalculator.controller.mileage.model.TempMileageRequest;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import by.nhorushko.distancecalculator.DistanceCalculatorSettingsImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class DistanceCalculatorSettingsFactoryTest {
    private final TEMPDistanceCalculatorSettingsFactory factory = new TEMPDistanceCalculatorSettingsFactory();

    @Test
    public void settingsShouldBeCreated() {
        final int givenMinDetectionSpeed = 10;
        final int givenMaxMessageTimeout = 16;
        final TempMileageRequest givenRequest = createRequest(givenMinDetectionSpeed, givenMaxMessageTimeout);

        final DistanceCalculatorSettings actual = factory.create(givenRequest);
        final DistanceCalculatorSettings expected = new DistanceCalculatorSettingsImpl(
                givenMinDetectionSpeed,
                givenMaxMessageTimeout
        );
        assertEquals(expected, actual);
    }

    @SuppressWarnings("SameParameterValue")
    private static TempMileageRequest createRequest(final Integer minDetectionSpeed, final Integer maxMessageTimeout) {
        return TempMileageRequest.builder()
                .minDetectionSpeed(minDetectionSpeed)
                .maxMessageTimeout(maxMessageTimeout)
                .build();
    }
}
