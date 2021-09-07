package no.unit.pnxservice;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("PMD")
public class Pnxervices {

    private static final String PRIMO_RECORD_PREFIX = "TN_";
    private final transient LambdaLogger logger;

    private final transient HTTPConnectionWrapper connection;
    private final transient String apiKey;
    private final transient String host;


    /**
     *contructor.
     * @param awsContext the handler functions receives this from AWS
     * @param connection used for fetching the PNX data
     * @param apiKey for contacting Primo Rest service
     * @param host example: https://www.example.com
     */
    public Pnxervices(Context awsContext, HTTPConnectionWrapper connection, String apiKey, String host) {
        logger = awsContext.getLogger();
        this.connection = connection;
        this.apiKey = apiKey;
        this.host = host;

    }

    /**
     * generates url.
     * @param docID identifier used for retrieving the pnx
     * @param host   with prefix https://
     * @param apiKey for contacting the api
     * @return url string
     */
    protected String generateUrl(String docID, String host, String apiKey) {
        return host + "/primo/v1/search?vid=NB&tab=default_tab&scope=default_scope&q=any,contains,"
                + docID + "&lang=eng&apikey=" + apiKey;
    }

    /**
     * copied from old version.
     * @param docID used for retrieving the PNX record
     * @return docID without primoRecordPrefix
     */
    private String removePrimoRecordPrefix(String docID) {
        return docID.replaceFirst(PRIMO_RECORD_PREFIX, "");
    }


    /**
     * Preprosess document_id and generates URL before fetching the full PNX record.
     * @param documentId for retrieving the pnx record
     * @return JSONObject fullPNX
     */
    public JSONObject doStuff(String documentId) {
        String docID = removePrimoRecordPrefix(documentId);
        String url = generateUrl(docID, host, apiKey);
        System.out.println("URL:" + url);
        return getFullPNX(url);
    }

    /**
     * fetches the pnx record from the api and converts it to a JSONObject.
     * @param url to the api-endpint
     * @return JSONObject fullPNXrecord
     */
    private JSONObject getFullPNX(String url) {
        String response;
        try {
            response = connection.getResourceAsString(url);
        } catch (IOException e) {
            System.out.println("COULD NOT CONNECT TO EXIBRIS XSERVICE");
            System.out.println(e.getMessage());
            logger.log("COULD NOT CONNECT TO EXIBRIS XSERVICE");
            return null;
        }
        JSONObject extractedData;
        try {
            JSONObject jsonResponse = new JSONObject(response);
            System.out.println(response);
            extractedData = extractUsefulDataFromXservice(jsonResponse);
        } catch (JSONException e) {
            logger.log("Could not parse data from Exibris Xservice at " + url + e.getMessage());
            return null;
        }
        return extractedData;
    }

    /**
     * Function for extracting data needed for the frontend.
     * @param response from REST api
     * @return JSONOBject with only relevant attributes
     * @throws JSONException if the response from REST api is not a PNX json
     */
    protected JSONObject extractUsefulDataFromXservice(JSONObject response)  {
        JSONObject pnx = response.getJSONArray("docs").getJSONObject(0).getJSONObject("pnx");
        JSONObject extractedData = new JSONObject();
        JSONObject control = pnx.getJSONObject("control");
        JSONObject search = pnx.getJSONObject("search");
        if (search.has("sourceid")) {
            extractedData.put("source", search.get("sourceid"));
        } else if (control.has("sourceid")) {
            extractedData.put("source", control.get("sourceid"));
        }
        if (control.has("recordid")) {
            extractedData.put("record_id", control.get("recordid"));
        }
        JSONObject addata = pnx.getJSONObject("addata");
        if (addata.has("isbn")) {
            extractedData.put("isbn", search.get("isbn"));
        }
        if (addata.has("cop")) {
            extractedData.put("publicationPlace", addata.get("cop"));
        }
        if (addata.has("btitle")) {
            extractedData.put("b_title", addata.get("btitle"));
        }
        if (addata.has("volume")) {
            extractedData.put("volume", addata.get("volume"));
        }
        if (addata.has("lad11")) {
            extractedData.put("mms_id", addata.get("lad11"));
        } else {
            extractedData.put("mms_id", new JSONArray());
        }
        if (addata.has("pages")) {
            extractedData.put("pages", addata.get("pages"));
        }
        JSONObject display = pnx.getJSONObject("display");
        if (display.has("creationdate")) {
            extractedData.put("creation_year", display.get("creationdate"));
        }
        if (display.has("creator")) {
            extractedData.put("creator", display.get("creator"));
        }
        if (display.has("title")) {
            extractedData.put("display_title", display.get("title"));
        }
        if (display.has("publisher")) {
            extractedData.put("publisher", display.get("publisher"));
        }
        JSONObject facets = pnx.getJSONObject("facets");
        if (facets.has("library")) {
            extractedData.put("libraries", facets.get("library"));
        } else {
            extractedData.put("libraries", new JSONArray());
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


