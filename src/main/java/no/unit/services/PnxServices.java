package no.unit.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PnxServices {

    private static final transient Logger log = LoggerFactory.getLogger(PnxServices.class);

    private static final String PRIMO_RECORD_PREFIX = "TN_";
    public static final String WRONG_URL_FOR_PRIMO_API = "Wrong Url for primo api "
            + " {}";
    public static final String ERROR_WHILE_GETTING_AT_PRIMO_API_FOR = "error while getting at "
            + "primo api for {} ";
    public static final String MY_EXTRACTED_PNX = "That's my extracted PNX: ";

    //full PNX json key names:
    private static final String PNX_KEY = "pnx";
    private static final String DOCS_key = "docs";
    private static final String PNX_CONTROL_KEY = "control";
    private static final String PNX_RECORD_ID_KEY = "recordid";
    private static final String PNX_SEARCH_KEY = "search";
    private static final String PNX_ADDATA_KEY = "addata";
    private static final String PNX_SOURCE_ID_KEY = "sourceid";
    private static final String PNX_CITY_OF_PUBLICATION_KEY = "cop";
    private static final String PNX_B_TITLE_KEY = "btitle";
    private static final String PNX_LAD11_KEY = "lad11";
    private static final String PNX_DISPLAY_KEY = "display";
    private static final String PNX_CREATION_DATE_KEY = "creationdate";
    private static final String PNX_FACETS_KEY = "facets";
    private static final String PNX_LIBRARY_KEY = "library";

    //output json key names:
    public static final String EXTRACTED_RECORD_ID_KEY = "record_id";
    public static final String EXTRACTED_SOURCE_KEY = "source";
    public static final String EXTRACTION_PUBLICATION_PLACE_KEY = "publication_place";
    public static final String EXTRACTED_MMS_ID_KEY = "mms_id";
    public static final String EXTRACTED_B_TITLE_KEY = "b_title";
    public static final String EXTRACTED_CREATION_YEAR_KEY = "creation_year";
    public static final String EXTRACTED_DISPLAY_TITLE_KEY = "display_title";
    public static final String EXTRACTED_LIBRARIES_KEY = "libraries";

    //json key names that both output json and pnx json share:
    public static final String ISBN = "isbn";
    public static final String VOLUME = "volume";
    public static final String PAGES = "pages";
    public static final String CREATOR = "creator";
    public static final String TITLE = "title";
    public static final String PUBLISHER = "publisher";

    private final transient PnxServiceConnection connection;

    /**
     *contructor.
     */
    public PnxServices() {
        this.connection = new PnxServiceConnection();
    }

    public PnxServices(PnxServiceConnection connection) {
        this.connection = connection;
    }


    /**
     * copied from old version.
     * @param docID used for retrieving the PNX record
     * @return docID without primoRecordPrefix
     */
    protected String removePrimoRecordPrefix(String docID) {
        return docID.replaceFirst(PRIMO_RECORD_PREFIX, "");
    }

    public JsonObject getPnxData(String documentId) {
        JsonObject fullPNX = getFullPNX(documentId);
        final JsonObject usefullPnx = extractUsefulDataFromPnxService(fullPNX);
        log.info(MY_EXTRACTED_PNX + usefullPnx);
        return usefullPnx;
    }

    protected JsonObject getFullPNX(String documentId) {
        String docID = removePrimoRecordPrefix(documentId);
        try (InputStreamReader streamReader = connection.connect(docID)) {
            String json = new BufferedReader(streamReader)
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            return JsonParser.parseString(json).getAsJsonObject();
        } catch (URISyntaxException e) {
            log.error(WRONG_URL_FOR_PRIMO_API, documentId, e);
        } catch (IOException e) {
            log.error(ERROR_WHILE_GETTING_AT_PRIMO_API_FOR,  documentId, e);
        }
        return new JsonObject();
    }

    protected JsonObject extractUsefulDataFromPnxService(JsonObject response) {
        JsonObject pnx = response.getAsJsonArray(DOCS_key).get(0).getAsJsonObject().getAsJsonObject(PNX_KEY);
        JsonObject extractedData = new JsonObject();

        JsonObject control = pnx.getAsJsonObject(PNX_CONTROL_KEY);
        extractedData.add(EXTRACTED_RECORD_ID_KEY, control.get(PNX_RECORD_ID_KEY));

        JsonObject search = pnx.getAsJsonObject(PNX_SEARCH_KEY);
        extractedData.add(EXTRACTED_SOURCE_KEY, search.get(PNX_SOURCE_ID_KEY));


        JsonObject addata = pnx.getAsJsonObject(PNX_ADDATA_KEY);
        extractedData.add(ISBN, addata.get(ISBN));
        extractedData.add(EXTRACTION_PUBLICATION_PLACE_KEY, addata.get(PNX_CITY_OF_PUBLICATION_KEY));
        extractedData.add(EXTRACTED_B_TITLE_KEY, addata.get(PNX_B_TITLE_KEY));
        extractedData.add(VOLUME, addata.get(VOLUME));
        extractedData.add(PAGES, addata.get(PAGES));
        if (addata.has(PNX_LAD11_KEY)) {
            extractedData.add(EXTRACTED_MMS_ID_KEY, addata.get(PNX_LAD11_KEY));
        } else {
            extractedData.add(EXTRACTED_MMS_ID_KEY, new JsonArray());
        }

        JsonObject display = pnx.getAsJsonObject(PNX_DISPLAY_KEY);
        extractedData.add(EXTRACTED_CREATION_YEAR_KEY, display.get(PNX_CREATION_DATE_KEY));
        extractedData.add(CREATOR, display.get(CREATOR));
        extractedData.add(EXTRACTED_DISPLAY_TITLE_KEY, display.get(TITLE));
        extractedData.add(PUBLISHER, display.get(PUBLISHER));

        JsonObject facets = pnx.getAsJsonObject(PNX_FACETS_KEY);
        if (facets.has(PNX_LIBRARY_KEY)) {
            extractedData.add(EXTRACTED_LIBRARIES_KEY, facets.get(PNX_LIBRARY_KEY));
        } else {
            extractedData.add(EXTRACTED_LIBRARIES_KEY, new JsonArray());
        }

        return extractedData;
    }

}


