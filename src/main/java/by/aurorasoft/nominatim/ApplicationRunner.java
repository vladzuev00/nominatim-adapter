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
            service.findInAreaAndSave(
                    new AreaCoordinate(
                            new Coordinate(53.211134, 26.375680),
                            new Coordinate(53.220913, 26.418141)),
                    0.01);
        }
    }
}
