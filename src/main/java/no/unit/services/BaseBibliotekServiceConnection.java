package no.unit.services;


import no.unit.Config;
import org.apache.http.client.utils.URIBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BaseBibliotekServiceConnection {

    private static final String HTTPS = "https";
    public static final String BASEBIBLIOTEK_BIBNR_MAPPING_PATH = "/basebibliotek/rest/bibnr/";
    public static final String EMPTY_STRING = "";

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
        Charset charsetFromResponse = this.getCharsetFromResponse(connection);
        return convertToByteArrayInputStream(connection.getInputStream(), charsetFromResponse);
    }

    private Charset getCharsetFromResponse(URLConnection connection) {
        String contentType = connection.getContentType();
        String[] values = contentType.split(";"); // values.length should be 2
        String charset = EMPTY_STRING;
        for (String value : values) {
             String v = value.trim();
            if (v.toLowerCase(Locale.getDefault()).startsWith("charset=")) {
                charset = v.substring("charset=".length());
            }
        }
        if (EMPTY_STRING.equals(charset)) {
            charset = UTF_8.name(); //Assumption
        }
        return Charset.forName(charset);
    }

    protected URI getUri(String identifier) throws URISyntaxException {
        return new URIBuilder()
                .setScheme(HTTPS)
                .setHost(Config.getInstance().getBasebibliotekServiceHost())
                .setPath(BASEBIBLIOTEK_BIBNR_MAPPING_PATH + identifier)
                .build();
    }

    @SuppressWarnings("PMD.AssignmentInOperand")
    protected InputStream convertToByteArrayInputStream(InputStream input, Charset charset) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        byte[] bytes = baos.toString(UTF_8).getBytes(charset);
        return new ByteArrayInputStream(bytes);
    }
}
