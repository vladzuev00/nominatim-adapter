package by.aurorasoft.mileagecalculator.controller.mileage;

import by.aurorasoft.mileagecalculator.base.AbstractJunitSpringBootTest;
import by.aurorasoft.mileagecalculator.controller.mileage.factory.TEMPDistanceCalculatorSettingsFactory;
import by.aurorasoft.mileagecalculator.controller.mileage.factory.TrackFactory;
import by.aurorasoft.mileagecalculator.controller.mileage.model.TEMPMileageRequest;
import by.aurorasoft.mileagecalculator.controller.mileage.model.TEMPMileageRequest.TEMPTrackPointRequest;
import by.aurorasoft.mileagecalculator.model.MileagePercentage;
import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.service.mileage.MileagePercentageCalculatingService;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static by.aurorasoft.mileagecalculator.testutil.HttpUtil.*;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class MileagePercentageCalculatingControllerTest extends AbstractJunitSpringBootTest {
    private static final String URL = "/api/v1/mileagePercentage";

    @MockBean
    private TrackFactory mockedTrackFactory;

    @MockBean
    private TEMPDistanceCalculatorSettingsFactory mockedDistanceCalculatorSettingsFactory;

    @MockBean
    private MileagePercentageCalculatingService mockedService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void mileagePercentageShouldBeFound() {
        final TEMPMileageRequest givenRequest = TEMPMileageRequest.builder()
                .trackPoints(
                        List.of(
                                new TEMPTrackPointRequest(parse("2023-02-14T12:28:04Z"), 45., 46., 15, 500, true),
                                new TEMPTrackPointRequest(parse("2023-02-14T12:28:05Z"), 45.001, 46., 15, 500, true)
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

        final MileagePercentage givenMileagePercentage = new MileagePercentage(0.3, 0.7);
        when(mockedService.calculate(same(givenTrack), same(givenDistanceCalculatorSettings)))
                .thenReturn(givenMileagePercentage);

        final MileagePercentage actual = postExpectingOk(restTemplate, URL, givenRequest, MileagePercentage.class);
        assertEquals(givenMileagePercentage, actual);
    }

    @Test
    public void mileagePercentageShouldNotBeFoundBecauseOfNotValidRequest()
            throws Exception {
        final TEMPMileageRequest givenRequest = TEMPMileageRequest.builder()
                .minDetectionSpeed(10)
                .maxMessageTimeout(11)
                .build();

        final String actual = postExpectingNotAcceptable(restTemplate, URL, givenRequest, String.class);
        final String expected = """
                {
                  "status": "NOT_ACCEPTABLE",
                  "message": "trackPoints : не должно равняться null",
                  "dateTime": "2024-04-17 09-53-51"
                }""";
        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);

        verifyNoInteractions(mockedTrackFactory, mockedDistanceCalculatorSettingsFactory, mockedService);
    }
}
