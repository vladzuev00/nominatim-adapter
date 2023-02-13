package by.aurorasoft.nominatim;

import by.aurorasoft.nominatim.service.mileage.MileageService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = run(ApplicationRunner.class, args);
        context.getBean(MileageService.class).findMileage(null);
    }
}
