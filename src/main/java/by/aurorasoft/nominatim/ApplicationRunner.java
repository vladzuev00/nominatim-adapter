package by.aurorasoft.nominatim;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.service.SearchCityService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {
        try (final ConfigurableApplicationContext context = run(ApplicationRunner.class, args)) {
            final SearchCityService service = context.getBean(SearchCityService.class);
            service.findInArea(
                    new AreaCoordinate(
                            new Coordinate(52.158160, 23.537081),
                            new Coordinate(52.126338, 23.635885)),
                    0.01);
        }
    }
}
