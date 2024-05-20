package by.aurorasoft.distanceclassifier.testutil;

import lombok.experimental.UtilityClass;
import org.wololo.geojson.Feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@UtilityClass
public final class FeatureUtil {

    public static void checkEquals(final Feature expected, final Feature actual) {
        assertEquals(expected.getId(), actual.getId());
        assertSame(expected.getGeometry(), actual.getGeometry());
        assertEquals(expected.getProperties(), actual.getProperties());
    }
}
