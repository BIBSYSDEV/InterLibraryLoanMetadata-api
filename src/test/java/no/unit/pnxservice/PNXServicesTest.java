package no.unit.pnxservice;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Objects;


import static no.unit.pnxservice.Pnxervices.ERROR_WHILE_GETTING_AT_PRIMO_API_FOR;
import static no.unit.pnxservice.Pnxervices.WRONG_URL_FOR_PRIMO_API;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void getPnxData() throws IOException, URISyntaxException {
        JsonObject condensedPNXFromFile1 = JsonParser.parseString( createJSON(CONDENSED_PNX_EXAMPLE_1)).getAsJsonObject();
        PnxServiceConnection pnxServiceConnection = mock(PnxServiceConnection.class);
        InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                FULL_PNX_EXAMPLE_1)));
        when(pnxServiceConnection.connect(anyString())).thenReturn(inputStreamReader);
        Pnxervices pnxervices = new Pnxervices(pnxServiceConnection);
        JsonObject result = pnxervices.getPnxData("test");
        assertEquals(condensedPNXFromFile1, result);

    }

    @Test
    public void getFullPNXResponse() throws IOException, URISyntaxException {
        PnxServiceConnection pnxServiceConnection = mock(PnxServiceConnection.class);
        InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                FULL_PNX_EXAMPLE_1)));
        when(pnxServiceConnection.connect(anyString())).thenReturn(inputStreamReader);
        Pnxervices pnxervices = new Pnxervices(pnxServiceConnection);
        JsonObject fullPnxResult = pnxervices.getFullPNX("test_test");
        assertTrue(fullPnxResult.getAsJsonArray("docs").get(0).getAsJsonObject().has("pnx"));
    }

    @Test
    public void handlesUriException() throws IOException, URISyntaxException {
        PnxServiceConnection pnxServiceConnection = mock(PnxServiceConnection.class);
        when(pnxServiceConnection.connect(anyString())).thenThrow(new URISyntaxException("docId", WRONG_URL_FOR_PRIMO_API));
        Pnxervices pnxervices = new Pnxervices(pnxServiceConnection);
        JsonObject expected = new JsonObject();
        JsonObject result = pnxervices.getFullPNX("testtest");
        assertEquals(expected, result);
    }
    @Test
    public void handlesIOException() throws IOException, URISyntaxException {
        PnxServiceConnection pnxServiceConnection = mock(PnxServiceConnection.class);
        when(pnxServiceConnection.connect(anyString())).thenThrow(new IOException( ERROR_WHILE_GETTING_AT_PRIMO_API_FOR));
        Pnxervices pnxervices = new Pnxervices(pnxServiceConnection);
        JsonObject expected = new JsonObject();
        JsonObject result = pnxervices.getFullPNX("testtest");
        assertEquals(expected, result);
    }

    @Test
    public void removesPrimoPrefixCorrectly(){
        String withPrefix = "TN_BIBSYS_ILS71560264980002201";
        String withoutPrefix = "BIBSYS_ILS71560264980002201";
        Pnxervices pnxervices = new Pnxervices();
        String prepared = pnxervices.removePrimoRecordPrefix(withPrefix);
        assertEquals(withoutPrefix, prepared);
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
        JSONObject fullPNXExample1 = new JSONObject( createJSON(FULL_PNX_EXAMPLE_1));
        JSONObject fullPNXExample2 = new JSONObject( createJSON(FULL_PNX_EXAMPLE_2));

        Pnxervices pnxervices = new Pnxervices();
        JSONArray combined = pnxervices.combineMMSidAndLibraries(fullPNXExample1);


    }
}
