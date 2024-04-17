package by.aurorasoft.nominatim.controller.mileage.factory;

import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import by.nhorushko.distancecalculator.DistanceCalculatorSettingsImpl;
import org.springframework.stereotype.Component;

@Component
public final class DistanceCalculatorSettingsFactory {

    public DistanceCalculatorSettings create(final MileageRequest request) {
        return new DistanceCalculatorSettingsImpl(request.getMinDetectionSpeed(), request.getMaxMessageTimeout());
    }
}
