package no.unit.pnxservice;


import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PnxServiceConnectionTest {
    public static final String PRIMO_REST_PATH = "/primo/v1/search";

    @Test
    public void testBuildUri() throws URISyntaxException {
        PnxServiceConnection connection = new PnxServiceConnection();
        URI uri = connection.generatePrimoUri("test");
        assertEquals(PRIMO_REST_PATH, uri.getPath());
    }
}
