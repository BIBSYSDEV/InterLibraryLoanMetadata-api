package no.unit.pnxservice;

import com.amazonaws.services.lambda.runtime.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class PNXServicesTest {
    public static final String FULL_PNX_EXAMPLE_1 = "full_pnx_example_1.json";
    public static final String FULL_PNX_EXAMPLE_2 = "full_pnx_example_2.json";
    public static final String CONDENSED_PNX_EXAMPLE_1 = "condensed_pnx_example_1.json";
    public static final String CONDENSED_PNX_EXAMPLE_2 = "condensed_pnx_example_2.json";

    private JSONObject createJSON(String filename){

        StringBuilder contentBuilder = new StringBuilder();
        int i;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(filename))
        {
            while((i = stream.read()) != -1) {
                contentBuilder.append((char) i);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return new JSONObject( contentBuilder.toString());

    }

    @Test
    public void generatesCorrectURL() {
        Context awsContext = mock(Context.class);
        HTTPConnectionWrapper connectionWrapper = mock(HTTPConnectionWrapper.class);
        String host = "https://thesourceoftruth.com";
        String apiKey = "megaSecret";
        String docId = "BIBSYS_ILS71463631120002201";
        String expected = "https://thesourceoftruth.com/primo/v1/search?vid=NB&tab=default_tab&scope=default_scope&q=any,contains,BIBSYS_ILS71463631120002201&lang=eng&apikey=megaSecret";
        Pnxervices pnxServices = new Pnxervices(awsContext, connectionWrapper, apiKey, host);
        assertEquals(expected, pnxServices.generateUrl(docId, host, apiKey));
    }

    @Test
    public void itExtractsCorrectData(){
        Context awsContext = mock(Context.class);
        HTTPConnectionWrapper connectionWrapper = mock(HTTPConnectionWrapper.class);
        JSONObject fullPNXExample1 = createJSON(FULL_PNX_EXAMPLE_1);
        JSONObject fullPNXExample2 = createJSON(FULL_PNX_EXAMPLE_2);
        JSONObject condensedPNXFromFile1 = createJSON(CONDENSED_PNX_EXAMPLE_1);
        JSONObject condensedPNXFromFile2 = createJSON(CONDENSED_PNX_EXAMPLE_2);
        Pnxervices pnxervices = new Pnxervices(awsContext, connectionWrapper, "", "");
        JSONObject pnxServicesCondensedExample1 = pnxervices.extractUsefulDataFromXservice(fullPNXExample1);
        JSONObject pnxServicesCondensedExample2 = pnxervices.extractUsefulDataFromXservice(fullPNXExample2);
        assertTrue(condensedPNXFromFile1.similar(pnxServicesCondensedExample1));
        assertTrue(condensedPNXFromFile2.similar(pnxServicesCondensedExample2));
    }

    @Test
    public void combinesMMsIdsAndLibrariesCorrectly(){
        Context awsContext = mock(Context.class);
        HTTPConnectionWrapper connectionWrapper = mock(HTTPConnectionWrapper.class);
        JSONObject fullPNXExample1 = createJSON(FULL_PNX_EXAMPLE_1);
        JSONObject fullPNXExample2 = createJSON(FULL_PNX_EXAMPLE_2);

        Pnxervices pnxervices = new Pnxervices(awsContext, connectionWrapper, "", "");
        JSONArray combined = pnxervices.combineMMSidAndLibraries(fullPNXExample1);


    }
}
