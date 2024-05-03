package by.aurorasoft.distanceclassifier.it.classifydistance;

import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "distance-classifying.load-city-geometries-on-start-app=false")
public final class ClassifyingDistanceWithNotCachedCityGeometriesIT extends ClassifyingDistanceIT {

}
