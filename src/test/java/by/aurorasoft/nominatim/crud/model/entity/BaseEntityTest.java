package by.aurorasoft.nominatim.crud.model.entity;

import by.aurorasoft.nominatim.base.AbstractSpringBootTest;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import static org.junit.Assert.*;

public final class BaseEntityTest extends AbstractSpringBootTest {

    @Test
    public void entitiesShouldBeEqual() {
        final Long givenId = 255L;
        final BaseEntity<Long> firstGivenEntity = createEntity(givenId);
        final BaseEntity<Long> secondGivenEntity = createEntity(givenId);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertTrue(actual);
    }

    @Test
    @SuppressWarnings("EqualsWithItself")
    public void sameEntitiesShouldBeEqual() {
        final BaseEntity<Long> firstGivenEntity = createEntity(255L);

        final boolean actual = firstGivenEntity.equals(firstGivenEntity);
        assertTrue(actual);
    }

    @Test
    public void notProxyEntityShouldBeEqualProxyEntity() {
        final Long givenId = 255L;
        final BaseEntity<Long> firstGivenEntity = createCity(givenId);
        final BaseEntity<Long> secondGivenEntity = getProxyCity(givenId);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertTrue(actual);
    }

    @Test
    public void entitiesShouldNotBeEqualBecauseOfOtherEntityIsNull() {
        final BaseEntity<Long> firstGivenEntity = createEntity(255L);
        final BaseEntity<Long> secondGivenEntity = null;

        @SuppressWarnings("all") final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertFalse(actual);
    }

    @Test
    public void entitiesShouldNotBeEqualBecauseOfDifferentNotProxyTypes() {
        final Long givenId = 255L;
        final BaseEntity<Long> firstGivenEntity = createEntity(givenId);
        final BaseEntity<Long> secondGivenEntity = getProxyCity(givenId);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertFalse(actual);
    }

    @Test
    public void entitiesShouldNotBeEqual() {
        final BaseEntity<Long> firstGivenEntity = createEntity(255L);
        final BaseEntity<Long> secondGivenEntity = createEntity(256L);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertFalse(actual);
    }

    @Test
    public void hashCodeShouldBeFound() {
        final BaseEntity<Long> givenEntity = createEntity(255L);

        final int actual = givenEntity.hashCode();
        final int expected = 286;
        assertEquals(expected, actual);
    }

    private static BaseEntity<Long> createEntity(final Long id) {
        final BaseEntity<Long> entity = new TestEntity();
        entity.setId(id);
        return entity;
    }

    @SuppressWarnings("SameParameterValue")
    private static CityEntity createCity(final Long id) {
        return CityEntity.builder()
                .id(id)
                .build();
    }

    private CityEntity getProxyCity(final Long id) {
        return entityManager.getReference(CityEntity.class, id);
    }

    @Setter
    @Getter
    private static final class TestEntity extends BaseEntity<Long> {
        private Long id;
    }
}
