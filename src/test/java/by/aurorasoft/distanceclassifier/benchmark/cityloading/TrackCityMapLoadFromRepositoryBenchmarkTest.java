package by.aurorasoft.distanceclassifier.benchmark.cityloading;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "distance-classifying.cache-geometries=false",
                "spring.jpa.properties.hibernate.hbm2ddl.import_files=classpath:sql/insert-belarus-cities.sql"
        }
)
public class TrackCityMapLoadFromRepositoryBenchmarkTest extends TrackCityMapLoadBenchmarkTest {

}
