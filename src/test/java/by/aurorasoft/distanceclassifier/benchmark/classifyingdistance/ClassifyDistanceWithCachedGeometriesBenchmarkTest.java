package by.aurorasoft.distanceclassifier.benchmark.classifyingdistance;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "distance-classifying.cache-geometries=true",
                "spring.jpa.properties.hibernate.hbm2ddl.import_files=classpath:sql/insert-belarus-cities.sql"
        }
)
public class ClassifyDistanceWithCachedGeometriesBenchmarkTest extends ClassifyDistanceBenchmarkTest {

}
