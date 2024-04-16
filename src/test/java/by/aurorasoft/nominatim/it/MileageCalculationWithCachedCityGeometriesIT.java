package by.aurorasoft.nominatim.it;

import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "mileage-calc.load-city-geometries-on-start-app=true")
public final class MileageCalculationWithCachedCityGeometriesIT extends MileageCalculationIT {

}
