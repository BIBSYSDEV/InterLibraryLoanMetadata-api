package no.unit.services;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static no.unit.services.BaseBibliotekServiceConnection.BASEBIBLIOTEK_BIBNR_MAPPING_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseBibliotekServiceConnectionTest {

    public static final String IDENTIFIER = "identifier";
    public static final String BASEBIBLIOTEK_BIBNR_PATH = BASEBIBLIOTEK_BIBNR_MAPPING_PATH + IDENTIFIER;

    @Test
    public void testBuildUri() throws URISyntaxException {
        final BaseBibliotekServiceConnection connection = new BaseBibliotekServiceConnection();
        final URI uri = connection.getUri(IDENTIFIER);
        String expectedURI = BASEBIBLIOTEK_BIBNR_PATH;
        assertEquals(expectedURI, uri.getPath());
    }

    @Test
    public void testConvertToByteArrayInputStream() throws IOException {
        final BaseBibliotekServiceConnection connection = new BaseBibliotekServiceConnection();
        try (InputStream inputstream = getClass().getResourceAsStream("/sampleLibraryFromBaseBibliotek.xml")) {
            connection.convertToByteArrayInputStream(inputstream, Charset.defaultCharset());
        }

    }
}