package classifyingdistance;

import base.BenchmarkTest;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassify.ClassifyDistanceService;
import by.aurorasoft.distanceclassifier.testutil.TrackCSVFileReadUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public abstract class ClassifyDistanceBenchmarkTest extends BenchmarkTest {
    private static final String GIVEN_TRACK_FILE_NAME = "track-6.csv";
    private static final Track GIVEN_TRACK = TrackCSVFileReadUtil.read(GIVEN_TRACK_FILE_NAME);
    private static final int GIVEN_URBAN_SPEED_THRESHOLD = 75;

    @Benchmark
    public final void classify() {
        getBean(ClassifyDistanceService.class).classify(GIVEN_TRACK, GIVEN_URBAN_SPEED_THRESHOLD);
    }
}
