package no.unit.ill.services;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseBibliotekServiceConnectionTest {

    public static final String IDENTIFIER = "identifier";
    public static final String INSTITUTION_MAPPING_CONTEXT_IDENTIFIER = "/institution/mapping/context/identifier";

    @Test
    public void testBuildUri() throws URISyntaxException {
        final BaseBibliotekServiceConnection connection = new BaseBibliotekServiceConnection();
        final URI uri = connection.getUri(IDENTIFIER);
        String expectedURI = INSTITUTION_MAPPING_CONTEXT_IDENTIFIER;
        assertEquals(expectedURI, uri.getPath());
    }
}