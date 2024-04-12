package by.aurorasoft.nominatim.config;

import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertNotNull;

public final class ModelMapperConfigTest {
    private final ModelMapperConfig config = new ModelMapperConfig();

    @Test
    public void modelMapperShouldBeCreated() {
        final ModelMapper actual = config.modelMapper();
        assertNotNull(actual);
    }
}
