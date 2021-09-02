package no.unit.ill.services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import no.unit.Config;
import org.apache.http.client.utils.URIBuilder;

public class InstitutionServiceConnection {

    private static final String HTTPS = "https";
    private static final String SLASH = "/";
    private static final String INSTITUTION_MAPPING_PATH = "institution/mapping/";

    /**
     * Builds an URL and connects to InstitutionService.
     * @param context context (e.g. oriaCode)
     * @param identifier identifier (e.g. NTNU_UB)
     * @return the response as InputStreamReader
     * @throws IOException something wrong with communication
     * @throws URISyntaxException something wrong with uri syntax
     */
    public InputStreamReader connect(String context, String identifier) throws IOException, URISyntaxException {
        final URI uri = new URIBuilder()
            .setScheme(HTTPS)
            .setHost(Config.getInstance().getInstitutionServiceHost())
            .setPath(INSTITUTION_MAPPING_PATH + context + SLASH + identifier)
            .build();
        return new InputStreamReader(uri.toURL().openStream());
    }

}
