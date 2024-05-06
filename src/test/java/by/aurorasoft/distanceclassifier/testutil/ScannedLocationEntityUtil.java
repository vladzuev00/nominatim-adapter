package by.aurorasoft.distanceclassifier.testutil;

import by.aurorasoft.distanceclassifier.crud.model.entity.ScannedLocationEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@UtilityClass
public final class ScannedLocationEntityUtil {

    public static void checkEquals(final ScannedLocationEntity expected, final ScannedLocationEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertTrue(expected.getGeometry().equalsTopo(actual.getGeometry()));
    }
}
