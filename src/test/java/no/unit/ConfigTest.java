package no.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ConfigTest {

    @Test
    public void testCorsHeaderNotSet() {
        final Config config = Config.getInstance();
        config.setCorsHeader(null);
        final String corsHeader = config.getCorsHeader();
        assertNull(corsHeader);
    }

    @Test
    public void testCheckPropertiesSet() {
        final Config instance = Config.getInstance();
        instance.setInstitutionServiceHost(Config.INSTITUTIONSERVICE_ENDPOINT_KEY);
        instance.setBasebibliotekServiceHost(Config.BASEBIBLIOTEKSERVICE_ENDPOINT_KEY);
        assertTrue(instance.checkProperties());
        assertEquals(Config.INSTITUTIONSERVICE_ENDPOINT_KEY, instance.getInstitutionServiceHost());
        assertEquals(Config.BASEBIBLIOTEKSERVICE_ENDPOINT_KEY, instance.getBasebibliotekServiceHost());
    }

    @Test
    public void testCheckPropertiesNotSet() {
        final Config instance = Config.getInstance();
        assertThrows(RuntimeException.class, () -> {
            instance.checkProperties();
        });
    }
}
