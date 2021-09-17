package no.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ConfigTest {

    @Test
    public void testCheckPropertiesSet() {
        final Config instance = Config.getInstance();
        instance.setBasebibliotekServiceHost(Config.BASEBIBLIOTEKSERVICE_ENDPOINT_KEY);
        instance.setStage(Config.STAGE_KEY);
        instance.setPrimoRestApiKey(Config.PRIMO_SERVICE_API_KEY);
        instance.setPrimoRestApiHost(Config.PRIMO_SERVICE_ENDPOINT_KEY);
        assertTrue(instance.checkProperties());
        assertEquals(Config.BASEBIBLIOTEKSERVICE_ENDPOINT_KEY, instance.getBasebibliotekServiceHost());
        assertEquals(Config.STAGE_KEY, instance.getStage());
        assertEquals(Config.PRIMO_SERVICE_API_KEY, instance.getPrimoRestApiKey());
        assertEquals(Config.PRIMO_SERVICE_ENDPOINT_KEY, instance.getPrimoRestApiHost());
    }

    @Test
    public void testCheckPropertiesNotSet() {
        final Config instance = Config.getInstance();
        assertThrows(IllegalStateException.class, () -> {
            instance.checkProperties();
        });
    }
}
