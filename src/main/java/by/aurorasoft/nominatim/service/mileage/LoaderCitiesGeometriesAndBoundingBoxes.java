package by.aurorasoft.nominatim.service.mileage;

import by.aurorasoft.nominatim.crud.service.CityService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoaderCitiesGeometriesAndBoundingBoxes {
    private final MileageService mileageService;
    private final CityService cityService;

    @EventListener(classes = ApplicationReadyEvent.class)
    public void onApplicationReady() {
        final Map<PreparedGeometry, PreparedGeometry> citiesGeometriesByBoundingBoxes = this.cityService
                .findPreparedGeometriesByPreparedBoundingBoxes();
        this.mileageService.setCitiesGeometriesByBoundingBoxes(citiesGeometriesByBoundingBoxes);
    }
}
