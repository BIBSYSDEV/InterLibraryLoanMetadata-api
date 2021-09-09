package no.unit.pnxservice;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class Pnxervices {

    private static final transient Logger log = LoggerFactory.getLogger(Pnxervices.class);

    private static final String PRIMO_RECORD_PREFIX = "TN_";
    public static final String WRONG_URL_FOR_PRIMO_API = "Wrong Url for primo api "
            + " {}";
    public static final String ERROR_WHILE_GETTING_AT_PRIMO_API_FOR = "error while getting at "
            + "primo api for {}/{}";

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
    private static final String EXTRACTED_RECORD_ID_KEY = "record_id";
    private static final String EXTRACTED_SOURCE_KEY = "source";
    private static final String EXTRACTION_PUBLICATION_PLACE_KEY = "publication_place";
    private static final String EXTRACTED_MMS_ID_KEY = "mms_id";
    private static final String EXTRACTED_B_TITLE_KEY = "b_title";
    private static final String EXTRACTED_CREATION_YEAR_KEY = "creation_year";
    private static final String EXTRACTED_DISPLAY_TITLE_KEY = "display_title";
    private static final String EXTRACTED_LIBRARIES_KEY = "libraries";

    //json key names that both output json and pnx json share:
    private static final String ISBN = "isbn";
    private static final String VOLUME = "volume";
    private static final String PAGES = "pages";
    private static final String CREATOR = "creator";
    private static final String TITLE = "title";
    private static final String PUBLISHER = "publisher";




    private final transient PnxServiceConnection connection;


    /**
     *contructor.
     */
    public Pnxervices() {
        this.connection = new PnxServiceConnection();
    }

    public Pnxervices(PnxServiceConnection connection){
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

    public JsonObject getPnxData(String documentId){
        JsonObject fullPNX = getFullPNX(documentId);
        return extractUsefulDataFromXservice(fullPNX);
    }




    protected JsonObject getFullPNX(String documentId) {
        String docID = removePrimoRecordPrefix(documentId);
        try(InputStreamReader streamReader = connection.connect(docID)) {
            String json = new BufferedReader(streamReader)
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            return JsonParser.parseString(json).getAsJsonObject();
        }catch ( URISyntaxException e) {
            System.out.println("ERROR: URI ERROR" + e.getMessage());
           // log.error(WRONG_URL_FOR_PRIMO_API, documentId, e);
        }catch (IOException e) {
            System.out.println("ERROR: IO ERROR" + e);
           // log.error(ERROR_WHILE_GETTING_AT_PRIMO_API_FOR,  documentId, e);
        }
        return new JsonObject();
    }

    protected JsonObject extractUsefulDataFromXservice(JsonObject response){
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
        if(addata.has(PNX_LAD11_KEY)){
            extractedData.add(EXTRACTED_MMS_ID_KEY, addata.get(PNX_LAD11_KEY));
        }else {
            extractedData.add(EXTRACTED_MMS_ID_KEY, new JsonArray());
        }

        JsonObject display = pnx.getAsJsonObject(PNX_DISPLAY_KEY);
        extractedData.add(EXTRACTED_CREATION_YEAR_KEY, display.get(PNX_CREATION_DATE_KEY));
        extractedData.add(CREATOR, display.get(CREATOR));
        extractedData.add(EXTRACTED_DISPLAY_TITLE_KEY, display.get(TITLE));
        extractedData.add(PUBLISHER, display.get(PUBLISHER));

        JsonObject facets = pnx.getAsJsonObject(PNX_FACETS_KEY);
        if(facets.has(PNX_LIBRARY_KEY)){
            extractedData.add(EXTRACTED_LIBRARIES_KEY, facets.get(PNX_LIBRARY_KEY));
        }else {
            extractedData.add(EXTRACTED_LIBRARIES_KEY, new JsonArray());
        }

        return extractedData;
    }







    /**
     * not finished.
     * @param fullPnxResponse from the api
     * @return JSONArray of combinedMMSidsAndLibraries
     */
    protected JSONArray combineMMSidAndLibraries(JSONObject fullPnxResponse) {
        JSONObject pnx = fullPnxResponse.getJSONArray("docs").getJSONObject(0).getJSONObject("pnx");
        JSONObject addata = pnx.getJSONObject("addata");
        JSONObject facets = pnx.getJSONObject("facets");

        JSONArray mmsIdsJson = new JSONArray();
        JSONArray librariesJson = new JSONArray();

        List<LibrariesWithMMsIds> librariesWithMMsIds = new ArrayList<>();
        if (addata.has("lad11")) {
            mmsIdsJson = addata.getJSONArray("lad11");
        }
        if (facets.has("library")) {
            librariesJson = facets.getJSONArray("library");
        }

        librariesJson.forEach(library -> {
            String libraryString = library.toString();
            String libraryCode = libraryString.replaceAll("\\d+", "");
            String libraryNumber = libraryString.replaceAll("\\D*", "");
            LibrariesWithMMsIds newLibrary = new LibrariesWithMMsIds(libraryCode);

            int index = librariesWithMMsIds.indexOf(newLibrary);
            if (index != -1) {
                librariesWithMMsIds.get(index).addLibraryNumber(libraryNumber);
            } else {
                newLibrary.addLibraryNumber(libraryNumber);
                librariesWithMMsIds.add(newLibrary);
            }
        });
        mmsIdsJson.forEach(mmsIdWithLibrary -> {
            String mmsIdWithLibraryString = mmsIdWithLibrary.toString();
            String[] mmsIdsWithLIbraryStringSplitted = mmsIdWithLibraryString.split("_");
            int standarNumberOfLibrarySplitSections = 3;
            if (mmsIdsWithLIbraryStringSplitted.length == standarNumberOfLibrarySplitSections) {
                String libraryCode = mmsIdsWithLIbraryStringSplitted[1];
                String mmsId = mmsIdsWithLIbraryStringSplitted[2];
                LibrariesWithMMsIds newLibrary = new LibrariesWithMMsIds(libraryCode);
                int index = librariesWithMMsIds.indexOf(newLibrary);
                if (index != -1) {
                    librariesWithMMsIds.get(index).addMMId(mmsId);
                } else {
                    newLibrary.addLibraryNumber(mmsId);
                    librariesWithMMsIds.add(newLibrary);
                }
            }
        });

        return new JSONArray(librariesWithMMsIds);
    }

}

