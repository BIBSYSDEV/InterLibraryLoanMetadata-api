package no.unit.ill.services;

import no.unit.Config;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;


public class PnxServiceConnection {

    private static final String HTTPS = "https";
    private static final String PRIMO_PATH = "/primo/v1/search";
    private static final String VID = "vid";
    private static final String BIBSYS = "BIBSYS";
    private static final String TAB = "tab";
    private static final String DEFAULT_TAB = "default_tab";
    private static final String SCOPE = "scope";
    private static final String DEFAULT_SCOPE = "default_scope";
    private static final String QUERY = "q";
    private static final String QUERY_START = "any,contains,";
    private static final String LANGUAGE = "lang";
    private static final String ENGLISH = "eng";
    private static final String API_KEY = "apikey";





    public InputStreamReader connect(String docId) throws IOException, URISyntaxException {
        URI uri = generatePrimoUri(docId);
        return new InputStreamReader(uri.toURL().openStream());
    }

    protected URI generatePrimoUri(String docId) throws URISyntaxException {
        return new URIBuilder().setScheme(HTTPS)
                .setHost(Config.getInstance().getPrimoRestApiHost())
                .setPath(PRIMO_PATH)
                .addParameter(VID,BIBSYS)
                .addParameter(TAB, DEFAULT_TAB)
                .addParameter(SCOPE, DEFAULT_SCOPE)
                .addParameter(QUERY, QUERY_START + docId)
                .addParameter(LANGUAGE, ENGLISH)
                .addParameter(API_KEY, Config.getInstance().getPrimoRestApiKey())
                .build();
    }
}
