package by.aurorasoft.mileagecalculator.controller.classifydistance;

import by.aurorasoft.mileagecalculator.base.AbstractJunitSpringBootTest;
import by.aurorasoft.mileagecalculator.controller.classifydistance.factory.TrackFactory;
import by.aurorasoft.mileagecalculator.controller.classifydistance.model.ClassifyDistanceRequest;
import by.aurorasoft.mileagecalculator.controller.classifydistance.model.ClassifyDistanceRequest.DistanceRequest;
import by.aurorasoft.mileagecalculator.controller.classifydistance.model.ClassifyDistanceRequest.PointRequest;
import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.service.mileage.ClassifyingDistanceService;
import by.nhorushko.classifieddistance.ClassifiedDistance;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static by.aurorasoft.mileagecalculator.testutil.HttpUtil.*;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class ClassifyingDistanceControllerTest extends AbstractJunitSpringBootTest {
    private static final String URL = "/api/v1/classifyDistance";

    @MockBean
    private TrackFactory mockedTrackFactory;

    @MockBean
    private ClassifyingDistanceService mockedService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void distanceShouldBeClassified()
            throws Exception {
        final int givenUrbanSpeedThreshold = 60;
        final ClassifyDistanceRequest givenRequest = new ClassifyDistanceRequest(
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

        final ClassifiedDistanceStorage givenStorage = new ClassifiedDistanceStorage(
                new ClassifiedDistance(1.1, 2.2),
                new ClassifiedDistance(3.3, 4.4)
        );
        when(mockedService.classify(same(givenTrack), eq(givenUrbanSpeedThreshold))).thenReturn(givenStorage);

        final String actual = postExpectingOk(restTemplate, URL, givenRequest, String.class);
        final String expected = """
                {
                  "gpsDistance": {
                    "urban": 1.1,
                    "country": 2.2,
                    "total": 3.3000000000000003
                  },
                  "odoDistance": {
                    "urban": 3.3,
                    "country": 4.4,
                    "total": 7.7
                  }
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void distanceShouldNotBeClassifiedBecauseOfNotValidRequest()
            throws Exception {
        final ClassifyDistanceRequest givenRequest = ClassifyDistanceRequest.builder()
                .urbanSpeedThreshold(60)
                .build();

        final String actual = postExpectingNotAcceptable(restTemplate, URL, givenRequest, String.class);
        final String expected = """
                {
                   "status": "NOT_ACCEPTABLE",
                   "message": "trackPoints : не должно равняться null",
                   "dateTime": "2024-04-26 09-48-28"
                }""";
        assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);

        verifyNoInteractions(mockedTrackFactory, mockedService);
    }
}
