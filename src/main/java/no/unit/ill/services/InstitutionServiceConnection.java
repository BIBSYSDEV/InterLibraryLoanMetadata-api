package no.unit.ill.services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import no.unit.Config;
import org.apache.http.client.utils.URIBuilder;

public class InstitutionServiceConnection {

    public static final String HTTPS = "https";
    public static final String SLASH = "/";
    final String institutionResourcePath = "institution/mapping/";

    public InputStreamReader connect(String context, String identifier) throws IOException, URISyntaxException {
        final URI uri = new URIBuilder()
            .setScheme(HTTPS)
            .setHost(Config.getInstance().getInstitutionServiceHost())
            .setPath(institutionResourcePath + context + SLASH + identifier)
            .build();
        return new InputStreamReader(uri.toURL().openStream());
    }

}
