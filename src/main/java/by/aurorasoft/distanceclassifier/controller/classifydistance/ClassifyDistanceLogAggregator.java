package by.aurorasoft.distanceclassifier.controller.classifydistance;

import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class ClassifyDistanceLogAggregator {

    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalDuration = new AtomicLong(0);
    private final AtomicLong totalDistance = new AtomicLong(0);

    public void addRequest(ClassifiedDistanceStorage storage, long duration) {
        totalRequests.incrementAndGet();
        totalDuration.addAndGet(duration);
        totalDistance.addAndGet((long) storage.getGpsDistance().getTotal());

        if (duration > 5) {
            log.info("Classify distance: {} km, urban: {} km, country: {} km, time {} ms",
                    storage.getGpsDistance().getTotal(),
                    storage.getGpsDistance().getUrban(),
                    storage.getGpsDistance().getCountry(),
                    duration
            );
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void report() {
        long requestCount = totalRequests.getAndSet(0);
        long durationSum = totalDuration.getAndSet(0);
        long distanceSum = totalDistance.getAndSet(0);

        if (requestCount > 0) {
            log.info("Classify distance - RPM {}, handled: {} km, average processing time of {} ms per request",
                    requestCount, distanceSum, durationSum / requestCount);
        } else {
            log.info("Classify distance - No request");
        }
    }
}
