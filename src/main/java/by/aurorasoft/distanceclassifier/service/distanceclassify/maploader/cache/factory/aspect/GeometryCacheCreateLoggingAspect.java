package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.factory.aspect;

import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.GeometryCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class GeometryCacheCreateLoggingAspect {

    @AfterReturning(pointcut = "create()", returning = "cache")
    public void log(final GeometryCache cache) {
        log.info(
                """
                        Cache with city geometries was created:
                            City geometries count: {}
                            Scanned location's geometry: {}""",
                cache.getCityGeometries().size(),
                cache.getScannedGeometry()
        );
    }

    @Pointcut(
            "execution("
                    + "public by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.GeometryCache "
                    + "by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.factory.GeometryCacheFactory.create()"
                    + ")"
    )
    private void create() {

    }
}
