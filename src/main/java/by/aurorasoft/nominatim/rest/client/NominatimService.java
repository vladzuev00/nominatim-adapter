package by.aurorasoft.nominatim.rest.client;

import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.crud.model.dto.NominatimReverseResponse;
import by.aurorasoft.nominatim.rest.client.exception.NominatimClientException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Service
public final class NominatimService implements AutoCloseable {
    private static final String URI_WITHOUT_PARAMETERS = "https://nominatim.openstreetmap.org/reverse";
    private static final String PARAM_NAME_FORMAT = "format";
    private static final String PARAM_NAME_LATITUDE = "lat";
    private static final String PARAM_NAME_LONGITUDE = "lon";
    private static final String PARAM_NAME_ZOOM = "zoom";

    private static final String FORMAT = "geojson";
    private static final int ZOOM = 10;

    private static final ParameterizedTypeReference<NominatimReverseResponse> PARAMETERIZED_TYPE_REFERENCE
            = new ParameterizedTypeReference<>() {
    };

    private static final int DURATION_IN_SECONDS_BETWEEN_REQUESTS = 1;

    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final Lock lock;
    private final Condition condition;
    private boolean durationBetweenRequestsPassed;

    public NominatimService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.executorService = newSingleThreadExecutor();
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.durationBetweenRequestsPassed = true;
    }

    @PostConstruct
    public void runTaskWaitingNecessaryDurationBetweenRequests() {
        this.executorService.submit(this.createTaskWaitingNecessaryDurationBetweenRequests());
    }

    public NominatimReverseResponse reverse(Coordinate coordinate) {
        this.lock.lock();
        try {
            while (!this.durationBetweenRequestsPassed) {
                this.condition.await();
            }
            final String uri = findUriToFindByCoordinate(coordinate);
            System.out.println(uri);
            final ResponseEntity<NominatimReverseResponse> responseEntity = this.restTemplate
                    .exchange(uri, GET, EMPTY, PARAMETERIZED_TYPE_REFERENCE);
            this.durationBetweenRequestsPassed = false;
            this.condition.signalAll();
            System.out.println(responseEntity.getBody());
            return responseEntity.getBody();
        } catch (final InterruptedException cause) {
            throw new NominatimClientException(cause);
        } finally {
            this.lock.unlock();
        }
    }

    private Runnable createTaskWaitingNecessaryDurationBetweenRequests() {
        return () -> {
            this.lock.lock();
            try {
                while (!currentThread().isInterrupted()) {
                    while (this.durationBetweenRequestsPassed) {
                        this.condition.await();
                    }
                    SECONDS.sleep(DURATION_IN_SECONDS_BETWEEN_REQUESTS);
                    this.durationBetweenRequestsPassed = true;
                    this.condition.signalAll();
                }
            } catch (final InterruptedException cause) {
                throw new NominatimClientException(cause);
            } finally {
                this.lock.unlock();
            }
        };
    }

    @Override
    @PreDestroy
    public void close() {
        this.executorService.shutdownNow();
    }

    private static String findUriToFindByCoordinate(Coordinate coordinate) {
        return fromUriString(URI_WITHOUT_PARAMETERS)
                .queryParam(PARAM_NAME_FORMAT, FORMAT)
                .queryParam(PARAM_NAME_LATITUDE, coordinate.getLatitude())
                .queryParam(PARAM_NAME_LONGITUDE, coordinate.getLongitude())
                .queryParam(PARAM_NAME_ZOOM, ZOOM)
                .build()
                .toUriString();
    }
}
