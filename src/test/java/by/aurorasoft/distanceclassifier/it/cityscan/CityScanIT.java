package by.aurorasoft.distanceclassifier.it.cityscan;

import by.aurorasoft.distanceclassifier.controller.cityscan.model.AreaCoordinateRequest;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.model.entity.ScannedLocationEntity;
import by.aurorasoft.distanceclassifier.it.base.AbstractIT;
import by.aurorasoft.distanceclassifier.model.CityType;
import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
import lombok.Value;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static by.aurorasoft.distanceclassifier.model.CityType.TOWN;
import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.postExpectingNoContext;
import static by.aurorasoft.distanceclassifier.testutil.ScannedLocationEntityUtil.checkEquals;
import static java.util.Arrays.stream;
import static java.util.Set.copyOf;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Import(CityScanIT.ScannedLocationAppendingBarrier.class)
public final class CityScanIT extends AbstractIT {
    private static final String URL = "/api/v1/cityScan";
    private static final Long SCANNED_LOCATION_ID = 1L;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private ScannedLocationAppendingBarrier scannedLocationAppendingBarrier;

    @Test
    @Sql(statements = "DELETE FROM city")
    @Sql(statements = "UPDATE scanned_location SET geometry = ST_GeomFromText('POLYGON EMPTY', 4326)")
    public void citiesShouldBeScanned() {
        sendWaitingExecution(
                new AreaCoordinateRequest(53.276930, 29.073363, 54.109940, 30.180785),
                new AreaCoordinateRequest(54.138632, 30.234380, 54.144590, 30.246525)
        );

        verifySavingCities(
                new CityView("Быхаў", TOWN),
                new CityView("Бялынічы", TOWN),
                new CityView("Кіраўск", TOWN),
                new CityView("Клічаў", TOWN)
        );
        verifyAppendingScannedLocation(
                "MULTIPOLYGON ("
                        + "((29.073363 53.27693, 29.073363 54.10994, 30.180785 54.10994, 30.180785 53.27693, 29.073363 53.27693)), "
                        + "((30.23438 54.138632, 30.23438 54.14459, 30.246525 54.14459, 30.246525 54.138632, 30.23438 54.138632))"
                        + ")"
        );
    }

    private void sendWaitingExecution(final AreaCoordinateRequest... requests) {
        scannedLocationAppendingBarrier.expect(requests.length);
        stream(requests).forEach(request -> postExpectingNoContext(restTemplate, URL, request));
        scannedLocationAppendingBarrier.await();
    }

    private void verifySavingCities(final CityView... views) {
        final Set<CityEntity> actual = findCities();
        final Set<CityView> actualViews = mapToViews(actual);
        final Set<CityView> expectedViews = Set.of(views);
        assertEquals(expectedViews, actualViews);
        actual.forEach(this::assertIdAndGeometryNotNull);
    }

    private Set<CityEntity> findCities() {
        final List<CityEntity> cities = entityManager.createQuery("SELECT e FROM CityEntity e", CityEntity.class).getResultList();
        return copyOf(cities);
    }

    private Set<CityView> mapToViews(final Set<CityEntity> cities) {
        return cities.stream()
                .map(this::mapToView)
                .collect(toUnmodifiableSet());
    }

    private CityView mapToView(final CityEntity city) {
        return new CityView(city.getName(), city.getType());
    }

    private void assertIdAndGeometryNotNull(final CityEntity city) {
        assertNotNull(city.getId());
        final CityGeometry geometry = city.getGeometry();
        assertNotNull(geometry);
        assertNotNull(geometry.getGeometry());
        assertNotNull(geometry.getBoundingBox());
    }

    @SuppressWarnings("SameParameterValue")
    private void verifyAppendingScannedLocation(final String expectedGeometryText) {
        final ScannedLocationEntity actual = getScannedLocation();
        final Geometry expectedGeometry = createGeometry(expectedGeometryText);
        final ScannedLocationEntity expected = new ScannedLocationEntity(SCANNED_LOCATION_ID, expectedGeometry);
        checkEquals(expected, actual);
    }

    private ScannedLocationEntity getScannedLocation() {
        return entityManager.find(ScannedLocationEntity.class, SCANNED_LOCATION_ID);
    }

    @SuppressWarnings("SameParameterValue")
    private Geometry createGeometry(final String text) {
        return GeometryUtil.createGeometry(text, geometryFactory);
    }

    @Aspect
    @Component
    public static class ScannedLocationAppendingBarrier {
        private static final long TIMEOUT_MS = 20000;
        private static final CountDownLatch DEFAULT_LATCH = new CountDownLatch(0);

        private volatile CountDownLatch latch;

        public ScannedLocationAppendingBarrier() {
            latch = DEFAULT_LATCH;
        }

        public final void expect(final int expectedCalls) {
            latch = new CountDownLatch(expectedCalls);
        }

        @AfterReturning("appendMethod()")
        public void onAfterAppend() {
            latch.countDown();
        }

        public final void await() {
            try {
                awaitInterrupted();
            } catch (final InterruptedException cause) {
                throw new IllegalStateException(cause);
            }
        }

        private void awaitInterrupted()
                throws InterruptedException {
            final boolean timeoutExceeded = !latch.await(TIMEOUT_MS, MILLISECONDS);
            if (timeoutExceeded) {
                throw new IllegalStateException("Latch timeout was exceeded");
            }
        }

        @Pointcut("execution(int by.aurorasoft.distanceclassifier.crud.repository.ScannedLocationRepository.append(org.locationtech.jts.geom.Geometry))")
        private void appendMethod() {

        }
    }

    @Value
    private static class CityView {
        String name;
        CityType type;
    }
}
