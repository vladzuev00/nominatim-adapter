package by.aurorasoft.nominatim;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.service.SearchCityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(ApplicationRunner.class, args);

        final SearchCityService service = context.getBean(SearchCityService.class);
        System.out.println(service.findCitiesInArea(
                new AreaCoordinate(
                        new Coordinate(53.665497, 27.037624),
                        new Coordinate(53.729191, 27.098866)),
                0.01));
    }
}
