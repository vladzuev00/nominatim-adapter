package by.aurorasoft.nominatim.base;

import by.aurorasoft.nominatim.base.containerinitializer.DataBaseContainerInitializer;
import org.junit.BeforeClass;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.time.ZoneOffset.UTC;
import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;

@Transactional
@SpringBootTest
@ContextConfiguration(initializers = DataBaseContainerInitializer.class)
public abstract class AbstractSpringBootTest {

    @PersistenceContext
    protected EntityManager entityManager;

    @BeforeClass
    public static void setDefaultTimeZone() {
        setDefault(getTimeZone(UTC));
    }
}
