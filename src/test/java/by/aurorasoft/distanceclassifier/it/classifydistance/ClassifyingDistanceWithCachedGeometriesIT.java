package by.aurorasoft.distanceclassifier.it.classifydistance;

import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        properties = {
                "distance-classifying.cache-geometries=true",
                "spring.sql.init.data-locations=classpath:data.sql,classpath:sql/insert-belarus-cities.sql"
        }
)
public final class ClassifyingDistanceWithCachedGeometriesIT extends ClassifyingDistanceIT {

}
