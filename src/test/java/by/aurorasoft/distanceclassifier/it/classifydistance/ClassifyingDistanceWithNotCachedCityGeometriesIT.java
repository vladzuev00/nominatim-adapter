package by.aurorasoft.distanceclassifier.it.classifydistance;

import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        properties = {
                "distance-classifying.load-city-geometries-on-start-app=false",
                "spring.jpa.properties.hibernate.hbm2ddl.import_files=data.sql,classpath:sql/insert-belarus-cities.sql"
        }
)
public final class ClassifyingDistanceWithNotCachedCityGeometriesIT extends ClassifyingDistanceIT {

}