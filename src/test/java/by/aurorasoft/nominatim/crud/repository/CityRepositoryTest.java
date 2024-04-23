package by.aurorasoft.nominatim.crud.repository;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static by.aurorasoft.nominatim.model.CityType.TOWN;
import static by.aurorasoft.nominatim.model.CityType.CITY;
import static by.aurorasoft.nominatim.testutil.CityEntityUtil.checkEquals;
import static by.aurorasoft.nominatim.testutil.GeometryUtil.createLineByText;
import static by.aurorasoft.nominatim.testutil.GeometryUtil.createPolygonByText;
import static by.aurorasoft.nominatim.testutil.IdUtil.mapToSortedIds;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class CityRepositoryTest extends AbstractJunitSpringBootTest {
    private static final String TUPLE_ALIAS_BOUNDING_BOX = "boundingBox";
    private static final String TUPLE_ALIAS_GEOMETRY = "geometry";

    @Autowired
    private CityRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void cityShouldBeFoundById() {
        final Long givenId = 63L;

        startQueryCount();
        final Optional<CityEntity> optionalActual = repository.findById(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final CityEntity actual = optionalActual.get();

        final CityEntity expected = CityEntity.builder()
                .id(givenId)
                .name("Сарни")
                .geometry(createPolygon("POLYGON((26.5713051 51.3218903,26.5786303 51.3192656,26.5831032 51.3074501,26.601612 51.2990736,26.6273034 51.2947553,26.6366959 51.3036507,26.6459503 51.3315349,26.637939 51.3463765,26.6047889 51.3591434,26.5759207 51.355952,26.5713051 51.3218903))"))
                .type(TOWN)
                .boundingBox(createPolygon("POLYGON((26.5713051 51.3218903,26.5786303 51.3192656,26.5831032 51.3074501,26.601612 51.2990736,26.6273034 51.2947553,26.6366959 51.3036507,26.6459503 51.3315349,26.637939 51.3463765,26.6047889 51.3591434,26.5759207 51.355952,26.5713051 51.3218903))"))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void cityShouldBeSaved() {
        final CityEntity givenCity = CityEntity.builder()
                .name("TestCity")
                .geometry(createPolygon("POLYGON((1 1, 2 1, 2 2, 1 1))"))
                .type(CITY)
                .boundingBox(createPolygon("POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))"))
                .build();

        startQueryCount();
        repository.save(givenCity);
        checkQueryCount(2);
    }

    @Test
    @Sql(statements = "DELETE FROM city WHERE id NOT IN (63, 58)")
    public void boundingBoxesWithGeometriesShouldBeFound() {
        startQueryCount();
        final Map<Geometry, Geometry> actual = findGeometriesByBoundingBox();
        checkQueryCount(1);

        final Map<Geometry, Geometry> expected = Map.of(
                createPolygon("POLYGON((26.5713051 51.3218903,26.5786303 51.3192656,26.5831032 51.3074501,26.601612 51.2990736,26.6273034 51.2947553,26.6366959 51.3036507,26.6459503 51.3315349,26.637939 51.3463765,26.6047889 51.3591434,26.5759207 51.355952,26.5713051 51.3218903))"),
                createPolygon("POLYGON((26.5713051 51.3218903,26.5786303 51.3192656,26.5831032 51.3074501,26.601612 51.2990736,26.6273034 51.2947553,26.6366959 51.3036507,26.6459503 51.3315349,26.637939 51.3463765,26.6047889 51.3591434,26.5759207 51.355952,26.5713051 51.3218903))"),
                createPolygon("POLYGON((26.1181731 53.5669171,26.1220879 53.5627187,26.1237694 53.5617895,26.1287667 53.5588824,26.1305154 53.5584735,26.1395147 53.556369,26.1534094 53.5505,26.1626521 53.5546695,26.1555441 53.5579651,26.1575208 53.5650461,26.1570622 53.567929,26.1569957 53.5684761,26.1600534 53.5728229,26.1527336 53.5759405,26.1480035 53.5781995,26.1423211 53.5797467,26.137291 53.5819,26.1336929 53.5823034,26.129894 53.5816433,26.1235157 53.5776559,26.1222441 53.5696428,26.1193288 53.5684393,26.1193089 53.5682134,26.1181731 53.5669171))"),
                createPolygon("POLYGON((26.1181731 53.5669171,26.1220879 53.5627187,26.1237694 53.5617895,26.1287667 53.5588824,26.1305154 53.5584735,26.1395147 53.556369,26.1534094 53.5505,26.1626521 53.5546695,26.1555441 53.5579651,26.1575208 53.5650461,26.1570622 53.567929,26.1569957 53.5684761,26.1600534 53.5728229,26.1527336 53.5759405,26.1480035 53.5781995,26.1423211 53.5797467,26.137291 53.5819,26.1336929 53.5823034,26.129894 53.5816433,26.1235157 53.5776559,26.1222441 53.5696428,26.1193288 53.5684393,26.1193089 53.5682134,26.1181731 53.5669171))")
        );
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "DELETE FROM city")
    public void boundingBoxesWithGeometriesShouldNotBeFound() {
        startQueryCount();
        final List<Tuple> actual = repository.findBoundingBoxesWithGeometries();
        checkQueryCount(1);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void citiesIntersectedByLineShouldBeFound() {
        final LineString givenLine = createLine("LINESTRING (24.356189727783203 52.20729446411133, 24.357080459594727 52.207244873046875, 24.35715675354004 52.206912994384766, 24.357515335083008 52.19639205932617, 24.367416381835938 52.18671798706055, 24.369365692138672 52.185455322265625, 24.369239807128906 52.18511199951172, 24.369524002075195 52.18476104736328, 24.37179183959961 52.18284225463867, 24.344202041625977 52.178504943847656, 24.341712951660156 52.17830276489258, 24.336538314819336 52.178810119628906, 24.334169387817383 52.179500579833984, 24.32586669921875 52.1827278137207, 24.32234764099121 52.18429946899414, 24.300270080566406 52.196861267089844, 24.297218322753906 52.1981086730957, 24.293201446533203 52.19924545288086, 24.28963279724121 52.19975280761719, 24.285850524902344 52.19984817504883, 24.281848907470703 52.19951248168945)");

        startQueryCount();
        try (final Stream<CityEntity> actual = repository.findIntersectedCities(givenLine)) {
            checkQueryCount(1);
            final List<Long> actualIds = mapToSortedIds(actual);
            final List<Long> expected = List.of(31L);
            assertEquals(expected, actualIds);
        }
    }

    @Test
    @Sql(statements = "DELETE FROM city WHERE id = 31")
    public void citiesIntersectedByLineShouldNotBeFound() {
        final LineString givenLine = createLine("LINESTRING (24.356189727783203 52.20729446411133, 24.357080459594727 52.207244873046875, 24.35715675354004 52.206912994384766, 24.357515335083008 52.19639205932617, 24.367416381835938 52.18671798706055, 24.369365692138672 52.185455322265625, 24.369239807128906 52.18511199951172, 24.369524002075195 52.18476104736328, 24.37179183959961 52.18284225463867, 24.344202041625977 52.178504943847656, 24.341712951660156 52.17830276489258, 24.336538314819336 52.178810119628906, 24.334169387817383 52.179500579833984, 24.32586669921875 52.1827278137207, 24.32234764099121 52.18429946899414, 24.300270080566406 52.196861267089844, 24.297218322753906 52.1981086730957, 24.293201446533203 52.19924545288086, 24.28963279724121 52.19975280761719, 24.285850524902344 52.19984817504883, 24.281848907470703 52.19951248168946)");

        startQueryCount();
        try (final Stream<CityEntity> actual = repository.findIntersectedCities(givenLine)) {
            checkQueryCount(1);
            assertTrue(actual.findAny().isEmpty());
        }
    }

    private Polygon createPolygon(final String text) {
        return createPolygonByText(text, geometryFactory);
    }

    private LineString createLine(final String text) {
        return createLineByText(text, geometryFactory);
    }

    private Map<Geometry, Geometry> findGeometriesByBoundingBox() {
        return repository.findBoundingBoxesWithGeometries()
                .stream()
                .collect(toMap(CityRepositoryTest::getBoundingBox, CityRepositoryTest::getGeometry));
    }

    private static Geometry getBoundingBox(final Tuple tuple) {
        return getGeometry(tuple, TUPLE_ALIAS_BOUNDING_BOX);
    }

    private static Geometry getGeometry(final Tuple tuple) {
        return getGeometry(tuple, TUPLE_ALIAS_GEOMETRY);
    }

    private static Geometry getGeometry(final Tuple tuple, final String alias) {
        return (Geometry) tuple.get(alias);
    }
}
