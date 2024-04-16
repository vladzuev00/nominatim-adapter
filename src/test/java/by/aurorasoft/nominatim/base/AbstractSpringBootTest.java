package by.aurorasoft.nominatim.base;

import by.aurorasoft.nominatim.base.containerinitializer.DataBaseContainerInitializer;
import com.yannbriancon.interceptor.HibernateQueryInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.time.ZoneOffset.UTC;
import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Transactional
@SpringBootTest
@ContextConfiguration(initializers = DataBaseContainerInitializer.class)
public abstract class AbstractSpringBootTest {

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private HibernateQueryInterceptor queryInterceptor;

    @BeforeClass
    public static void setDefaultTimeZone() {
        setDefault(getTimeZone(UTC));
    }

    protected final void startQueryCount() {
        log.info("======================= START QUERY COUNTER ====================================");
        queryInterceptor.startQueryCount();
    }

    protected final void checkQueryCount(final int expected) {
        entityManager.flush();
        log.info("======================= FINISH QUERY COUNTER ====================================");
        assertEquals(Long.valueOf(expected), queryInterceptor.getQueryCount(), "wrong count of queries");
    }
}
