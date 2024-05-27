package by.aurorasoft.distanceclassifier.benchmark.classifyingdistance;

import by.aurorasoft.distanceclassifier.benchmark.base.BenchmarkTest;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassify.ClassifyDistanceService;
import org.openjdk.jmh.annotations.Benchmark;
import org.springframework.test.annotation.DirtiesContext;

import static by.aurorasoft.distanceclassifier.testutil.TrackCSVFileReadUtil.read;

@DirtiesContext
public abstract class ClassifyDistanceBenchmarkTest extends BenchmarkTest {
    private static final String GIVEN_TRACK_FILE_NAME = "track-6.csv";
    private static final Track GIVEN_TRACK = read(GIVEN_TRACK_FILE_NAME);
    private static final int GIVEN_URBAN_SPEED_THRESHOLD = 75;

    @Benchmark
    public final void classify() {
        getBean(ClassifyDistanceService.class).classify(GIVEN_TRACK, GIVEN_URBAN_SPEED_THRESHOLD);
    }
}
