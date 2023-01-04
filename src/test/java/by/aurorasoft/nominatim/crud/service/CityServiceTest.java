package by.aurorasoft.nominatim.crud.service;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.crud.model.dto.City;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class CityServiceTest extends AbstractContextTest {

    @Autowired
    private CityService service;

    @Test
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 'CAPITAL')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(256, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 'CAPITAL')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(257, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 'CAPITAL')")
    public void allCitiesShouldBeFound() {
        final List<City> foundCities = this.service.findAll();
        final List<Long> actualIds = foundCities.stream()
                .map(City::getId)
                .collect(toList());
        final List<Long> expectedIds = List.of(255L, 256L, 257L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void allCitiesShouldNotBeFound() {
        final List<City> foundCities = this.service.findAll();
        assertTrue(foundCities.isEmpty());
    }
}
