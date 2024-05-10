package by.aurorasoft.distanceclassifier.benchmark.cityloading;


import by.aurorasoft.distanceclassifier.benchmark.base.BenchmarkTest;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.TrackCityMapLoader;
import org.openjdk.jmh.annotations.Benchmark;
import org.springframework.test.annotation.DirtiesContext;

import static by.aurorasoft.distanceclassifier.testutil.TrackCSVFileReadUtil.read;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public abstract class TrackCityMapLoadBenchmarkTest extends BenchmarkTest {
    private static final String TRACK_FILE_NAME = "track-6.csv";
    private static final Track TRACK = read(TRACK_FILE_NAME);

    @Benchmark
    public final void load() {
        getBean(TrackCityMapLoader.class).load(TRACK);
    }
}
