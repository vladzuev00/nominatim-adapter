package by.aurorasoft.distanceclassifier.benchmark.citymaploading;

import by.aurorasoft.distanceclassifier.benchmark.base.BenchmarkTest;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.TrackCityMapLoader;
import by.aurorasoft.distanceclassifier.testutil.TrackCSVFileReadUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class TrackCityMapLoadBenchmarkTest extends BenchmarkTest {
    private static final String GIVEN_TRACK_FILE_NAME = "track-6.csv";
    private static final Track GIVEN_TRACK = TrackCSVFileReadUtil.read(GIVEN_TRACK_FILE_NAME);

    @Benchmark
    public final void load() {
        getBean(TrackCityMapLoader.class).load(GIVEN_TRACK);
    }
}
