package by.aurorasoft.distanceclassifier.trigger;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;

public final class ScannedLocationTriggerTest extends AbstractSpringBootTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test(expected = UncategorizedSQLException.class)
    public void insertShouldBeDenied() {
        jdbcTemplate.execute("INSERT INTO scanned_location(id, geometry) VALUES(2, ST_GeomFromText('POLYGON EMPTY', 4326))");
    }

    @Test(expected = UncategorizedSQLException.class)
    public void deleteShouldBeDenied() {
        jdbcTemplate.execute("DELETE FROM scanned_location WHERE id = 1");
    }
}
