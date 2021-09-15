package no.unit.ill.services;

import no.unit.Config;
import org.apache.http.client.utils.URIBuilder;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;

public class BaseBibliotekServiceConnection {

    private static final String HTTPS = "https";
    public static final String BASEBIBLIOTEK_BIBNR_MAPPING_PATH = "/basebibliotek/rest/bibnr/";

    /**
     * Builds an URL and connects to BaseBibliotekService.
     *
     * @param identifier identifier (bibnr)
     * @return the response as InputStreamReader
     * @throws IOException        something wrong with communication
     * @throws URISyntaxException something wrong with uri syntax
     */
    public InputStream connect(String identifier) throws IOException, URISyntaxException {
        final URI uri = getUri(identifier);
        URLConnection connection = uri.toURL().openConnection();
        connection.setConnectTimeout(10_000);
        return convertToByteArrayInputStream(connection.getInputStream());
    }

    protected URI getUri(String identifier) throws URISyntaxException {
        final URI uri = new URIBuilder()
                .setScheme(HTTPS)
                .setHost(Config.getInstance().getBasebibliotekServiceHost())
                .setPath(BASEBIBLIOTEK_BIBNR_MAPPING_PATH + identifier)
                .build();
        return uri;
    }

    @SuppressWarnings("PMD.AssignmentInOperand")
    protected InputStream convertToByteArrayInputStream(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
