package by.aurorasoft.distanceclassifier.base;

import com.yannbriancon.interceptor.HibernateQueryInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
public abstract class AbstractJunitSpringBootTest extends AbstractSpringBootTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private HibernateQueryInterceptor queryInterceptor;

    protected final void startQueryCount() {
        log.info("======================= START QUERY COUNTER ====================================");
        queryInterceptor.startQueryCount();
    }

    protected final void checkQueryCount(final int expected) {
        entityManager.flush();
        log.info("======================= FINISH QUERY COUNTER ====================================");
        assertEquals("wrong count of queries", Long.valueOf(expected), queryInterceptor.getQueryCount());
    }
}
