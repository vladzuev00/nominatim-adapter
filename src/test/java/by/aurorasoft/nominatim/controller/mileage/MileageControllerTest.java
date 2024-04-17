package by.aurorasoft.nominatim.controller.mileage;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.controller.mileage.factory.DistanceCalculatorSettingsFactory;
import by.aurorasoft.nominatim.controller.mileage.factory.TrackFactory;
import by.aurorasoft.nominatim.service.mileage.MileageCalculatingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class MileageControllerTest extends AbstractJunitSpringBootTest {
    private static final String URL = "/api/v1/mileage";

    @MockBean
    private TrackFactory mockedTrackFactory;

    @MockBean
    private DistanceCalculatorSettingsFactory mockedDistanceCalculatorSettingsFactory;

    @MockBean
    private MileageCalculatingService mockedMileageCalculatingService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void mileageShouldBeFound() {

    }
}
