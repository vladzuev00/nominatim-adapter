package by.aurorasoft.mileagecalculator.controller.mileage;

import by.aurorasoft.mileagecalculator.base.AbstractJunitSpringBootTest;
import by.aurorasoft.mileagecalculator.controller.mileage.factory.TrackFactory;
import by.aurorasoft.mileagecalculator.controller.mileage.model.MileageRequest;
import by.aurorasoft.mileagecalculator.controller.mileage.model.MileageRequest.DistanceRequest;
import by.aurorasoft.mileagecalculator.controller.mileage.model.MileageRequest.PointRequest;
import by.aurorasoft.mileagecalculator.model.MileagePercentage;
import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.service.mileage.MileagePercentageCalculatingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static by.aurorasoft.mileagecalculator.testutil.HttpUtil.*;
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
    private MileagePercentageCalculatingService mockedService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void percentageShouldBeCalculated() {
        final int givenUrbanSpeedThreshold = 60;
        final MileageRequest givenRequest = new MileageRequest(
                List.of(
                        new PointRequest(
                                5.5,
                                6.6,
                                50,
                                new DistanceRequest(10., 11.),
                                new DistanceRequest(12., 13.)
                        ),
                        new PointRequest(
                                8.8,
                                9.9,
                                85,
                                new DistanceRequest(20., 21.),
                                new DistanceRequest(22., 23.)
                        )
                ),
                givenUrbanSpeedThreshold
        );

        final Track givenTrack = mock(Track.class);
        when(mockedTrackFactory.create(eq(givenRequest))).thenReturn(givenTrack);

        final MileagePercentage givenPercentage = new MileagePercentage(0.3, 0.7);
        when(mockedService.calculate(same(givenTrack), eq(givenUrbanSpeedThreshold))).thenReturn(givenPercentage);

        final MileagePercentage actual = postExpectingOk(restTemplate, URL, givenRequest, MileagePercentage.class);
        assertEquals(givenPercentage, actual);
    }

    @Test
    public void percentageShouldNotBeCalculatedBecauseOfNotValidRequest()
            throws Exception {
        final MileageRequest givenRequest = MileageRequest.builder()
                .urbanSpeedThreshold(60)
                .build();

        final String actual = postExpectingNotAcceptable(restTemplate, URL, givenRequest, String.class);
        final String expected = """
                {
                  "status": "NOT_ACCEPTABLE",
                  "message": "trackPoints : не должно равняться null",
                  "dateTime": "2024-04-17 09-53-51"
                }""";
        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);

        verifyNoInteractions(mockedTrackFactory, mockedService);
    }
}
