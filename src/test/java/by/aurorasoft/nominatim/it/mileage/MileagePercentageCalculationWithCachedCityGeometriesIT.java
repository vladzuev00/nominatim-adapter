package by.aurorasoft.nominatim.it.mileage;

import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "mileage-calc.load-city-geometries-on-start-app=true")
public final class MileagePercentageCalculationWithCachedCityGeometriesIT extends MileagePercentageCalculationIT {

}
