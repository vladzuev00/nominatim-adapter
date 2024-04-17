package by.aurorasoft.nominatim.controller.mileage;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.controller.mileage.factory.DistanceCalculatorSettingsFactory;
import by.aurorasoft.nominatim.controller.mileage.factory.TrackFactory;
import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest;
import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest.TrackPointRequest;
import by.aurorasoft.nominatim.model.Mileage;
import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.service.mileage.MileageCalculatingService;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static by.aurorasoft.nominatim.util.HttpUtil.postExpectingNotAcceptable;
import static by.aurorasoft.nominatim.util.HttpUtil.postExpectingOk;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
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
        final MileageRequest givenRequest = MileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TrackPointRequest(parse("2023-02-14T12:28:04Z"), 45F, 46F, 15, 500, true),
                                new TrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.001F, 46F, 15, 500, true)
                        )
                )
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final Track givenTrack = mock(Track.class);
        when(mockedTrackFactory.create(eq(givenRequest))).thenReturn(givenTrack);

        final DistanceCalculatorSettings givenDistanceCalculatorSettings = mock(DistanceCalculatorSettings.class);
        when(mockedDistanceCalculatorSettingsFactory.create(eq(givenRequest)))
                .thenReturn(givenDistanceCalculatorSettings);

        final Mileage givenMileage = new Mileage(5.5, 6.6);
        when(mockedMileageCalculatingService.calculate(same(givenTrack), same(givenDistanceCalculatorSettings)))
                .thenReturn(givenMileage);

        final Mileage actual = findMileageExpectingOk(givenRequest);
        assertEquals(givenMileage, actual);
    }

    @Test
    public void mileageShouldNotBeFoundBecauseOfNotValidRequest()
            throws Exception {
        final MileageRequest givenRequest = MileageRequest.builder()
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final String actual = findMileageExpectingNotAcceptable(givenRequest);
        final String expected = """
                {
                  "httpStatus": "NOT_ACCEPTABLE",
                  "message": "trackPoints : не должно равняться null",
                  "dateTime": "2024-04-17 09-53-51"
                }""";
        assertEquals(expected, actual, true);
    }

    private Mileage findMileageExpectingOk(final MileageRequest request) {
        return postExpectingOk(restTemplate, URL, request, Mileage.class);
    }

    private String findMileageExpectingNotAcceptable(final MileageRequest request) {
        return postExpectingNotAcceptable(restTemplate, URL, request, String.class);
    }
}
