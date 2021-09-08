package no.unit.ill.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class InstitutionServiceConnectionTest {

    public static final String CONTEXT = "context";
    public static final String IDENTIFIER = "identifier";
    public static final String INSTITUTION_MAPPING_CONTEXT_IDENTIFIER = "/institution/mapping/context/identifier";

    @Test
    public void testBuildUri() throws URISyntaxException {
        final InstitutionServiceConnection connection = new InstitutionServiceConnection();
        final URI uri = connection.getUri(CONTEXT, IDENTIFIER);
        String expectedURI = INSTITUTION_MAPPING_CONTEXT_IDENTIFIER;
        assertEquals(expectedURI, uri.getPath());
    }
}