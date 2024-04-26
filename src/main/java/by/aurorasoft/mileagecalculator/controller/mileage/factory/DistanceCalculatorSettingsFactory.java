package by.aurorasoft.mileagecalculator.controller.mileage.factory;

import by.aurorasoft.mileagecalculator.controller.mileage.model.TempMileageRequest;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import by.nhorushko.distancecalculator.DistanceCalculatorSettingsImpl;
import org.springframework.stereotype.Component;

@Component
public final class DistanceCalculatorSettingsFactory {

    public DistanceCalculatorSettings create(final TempMileageRequest request) {
        return new DistanceCalculatorSettingsImpl(request.getMinDetectionSpeed(), request.getMaxMessageTimeout());
    }
}
