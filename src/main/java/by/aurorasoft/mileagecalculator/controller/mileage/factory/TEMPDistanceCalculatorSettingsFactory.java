package by.aurorasoft.mileagecalculator.controller.mileage.factory;

import by.aurorasoft.mileagecalculator.controller.mileage.model.TEMPMileageRequest;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import by.nhorushko.distancecalculator.DistanceCalculatorSettingsImpl;
import org.springframework.stereotype.Component;

@Component
public final class TEMPDistanceCalculatorSettingsFactory {

    public DistanceCalculatorSettings create(final TEMPMileageRequest request) {
        return new DistanceCalculatorSettingsImpl(request.getMinDetectionSpeed(), request.getMaxMessageTimeout());
    }
}
