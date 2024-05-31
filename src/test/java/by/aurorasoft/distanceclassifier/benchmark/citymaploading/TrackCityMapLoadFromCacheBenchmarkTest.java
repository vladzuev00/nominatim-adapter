package by.aurorasoft.distanceclassifier.benchmark.citymaploading;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "distance-classifying.cache-geometries=true",
                "spring.sql.init.data-locations=classpath:sql/insert-belarus-cities.sql"
        }
)
public class TrackCityMapLoadFromCacheBenchmarkTest extends TrackCityMapLoadBenchmarkTest {

}
