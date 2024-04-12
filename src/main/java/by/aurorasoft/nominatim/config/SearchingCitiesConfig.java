package by.aurorasoft.nominatim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Configuration
public class SearchingCitiesConfig {

    @Bean
    public ExecutorService executorServiceSearchingCities() {
        return newSingleThreadExecutor();
    }
}
