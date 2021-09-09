package no.unit.pnxservice;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class PNXServicesTest {
    public static final String FULL_PNX_EXAMPLE_1 = "full_pnx_example_1.json";
    public static final String FULL_PNX_EXAMPLE_2 = "full_pnx_example_2.json";
    public static final String CONDENSED_PNX_EXAMPLE_1 = "condensed_pnx_example_1.json";
    public static final String CONDENSED_PNX_EXAMPLE_2 = "condensed_pnx_example_2.json";

    private String createJSON(String filename){

        StringBuilder contentBuilder = new StringBuilder();
        int i;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(filename))
        {
            if (stream != null) {
                while((i = stream.read()) != -1) {
                    contentBuilder.append((char) i);
                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return  contentBuilder.toString();

    }

    @Test
    public void itExtractsCorrectData(){
        JsonObject fullPNXExample1 = JsonParser.parseString( createJSON(FULL_PNX_EXAMPLE_1)).getAsJsonObject();
        JsonObject fullPNXExample2 = JsonParser.parseString( createJSON(FULL_PNX_EXAMPLE_2)).getAsJsonObject();
        JsonObject condensedPNXFromFile1 = JsonParser.parseString( createJSON(CONDENSED_PNX_EXAMPLE_1)).getAsJsonObject();
        JsonObject condensedPNXFromFile2 = JsonParser.parseString( createJSON(CONDENSED_PNX_EXAMPLE_2)).getAsJsonObject();
        Pnxervices pnxervices = new Pnxervices();
        JsonObject pnxServicesCondensedExample1 = pnxervices.extractUsefulDataFromXservice(fullPNXExample1);
        JsonObject pnxServicesCondensedExample2 = pnxervices.extractUsefulDataFromXservice(fullPNXExample2);
        assertEquals(condensedPNXFromFile1, pnxServicesCondensedExample1);
        assertEquals(condensedPNXFromFile2, pnxServicesCondensedExample2);
    }

    @Test
    public void combinesMMsIdsAndLibrariesCorrectly(){
        Context awsContext = mock(Context.class);
        JSONObject fullPNXExample1 = new JSONObject( createJSON(FULL_PNX_EXAMPLE_1));
        JSONObject fullPNXExample2 = new JSONObject( createJSON(FULL_PNX_EXAMPLE_2));

        Pnxervices pnxervices = new Pnxervices();
        JSONArray combined = pnxervices.combineMMSidAndLibraries(fullPNXExample1);


    }
}
