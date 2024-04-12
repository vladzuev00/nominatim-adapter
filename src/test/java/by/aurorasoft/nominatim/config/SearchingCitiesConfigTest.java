package by.aurorasoft.nominatim.config;

import org.junit.Test;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertNotNull;

public final class SearchingCitiesConfigTest {
    private final SearchingCitiesConfig config = new SearchingCitiesConfig();

    @Test
    public void executorServiceSearchingCitiesShouldBeCreated() {
        final ExecutorService actual = config.executorServiceSearchingCities();
        assertNotNull(actual);
    }
}
