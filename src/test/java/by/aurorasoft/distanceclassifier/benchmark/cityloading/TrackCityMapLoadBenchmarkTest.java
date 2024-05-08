package by.aurorasoft.distanceclassifier.benchmark.cityloading;


import by.aurorasoft.distanceclassifier.benchmark.base.BenchmarkTest;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.TrackCityMapLoader;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.List;

public abstract class TrackCityMapLoadBenchmarkTest extends BenchmarkTest {

    @Benchmark
    public final void load() {
        getBean(TrackCityMapLoader.class).load(new Track(List.of()));
    }
}
