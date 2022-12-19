package by.aurorasoft.nominatim;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.service.searchcity.SearchCityService;
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
                        new Coordinate(53.650630, 27.066252),
                        new Coordinate(53.716469, 27.129561)),
                0.01));
    }
}
