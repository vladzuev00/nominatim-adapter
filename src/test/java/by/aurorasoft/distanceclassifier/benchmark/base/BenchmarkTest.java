package by.aurorasoft.distanceclassifier.benchmark.base;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import org.junit.Test;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;

@State(Scope.Benchmark)
public abstract class BenchmarkTest extends AbstractSpringBootTest {
    private static final int MEASUREMENT_ITERATIONS = 3;
    private static final int WARMUP_ITERATIONS = 3;
    private static final int FORKS = 0;
    private static final boolean SHOULD_FAIL_ON_ERROR = true;

    private static ConfigurableApplicationContext context;

    @Autowired
    public final void setContext(final ConfigurableApplicationContext context) {
        BenchmarkTest.context = context;
    }

    @Test
    public final void run()
            throws RunnerException {
        new Runner(getOptions()).run();
    }

    protected static <T> T getBean(final Class<T> type) {
        return context.getBean(type);
    }

    private Options getOptions() {
        return new OptionsBuilder()
                .include(getClass().getName())
                .warmupIterations(WARMUP_ITERATIONS)
                .measurementIterations(MEASUREMENT_ITERATIONS)
                .forks(FORKS)
                .shouldFailOnError(SHOULD_FAIL_ON_ERROR)
                .mode(AverageTime)
                .timeUnit(MILLISECONDS)
                .build();
    }
}
