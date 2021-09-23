package no.unit.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PnxServicesTest {
    public static final String FULL_PNX_EXAMPLE_1 = "full_pnx_example_1.json";
    public static final String FULL_PNX_EXAMPLE_2 = "full_pnx_example_2.json";
    public static final String FULL_PNX_EXAMPLE_3 = "full_pnx_example_3.json";
    public static final String CONDENSED_PNX_EXAMPLE_1 = "condensed_pnx_example_1.json";
    public static final String CONDENSED_PNX_EXAMPLE_2 = "condensed_pnx_example_2.json";
    public static final String CONDENSED_PNX_EXAMPLE_3 = "condensed_pnx_example_3.json";

    private String createJson(String filename) {

        StringBuilder contentBuilder = new StringBuilder();
        int i;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (stream != null) {
                while ((i = stream.read()) != -1) {
                    contentBuilder.append((char) i);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  contentBuilder.toString();

    }

    @Test
    public void getPnxData() throws IOException, URISyntaxException {
        JsonObject condensedPnxFromFile1 = JsonParser.parseString(
                createJson(CONDENSED_PNX_EXAMPLE_1)).getAsJsonObject();
        PnxServiceConnection pnxServiceConnection = mock(PnxServiceConnection.class);
        InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(getClass()
                        .getClassLoader()
                        .getResourceAsStream(FULL_PNX_EXAMPLE_1)));
        when(pnxServiceConnection.connect(anyString())).thenReturn(inputStreamReader);
        PnxServices pnxServices = new PnxServices(pnxServiceConnection);
        JsonObject result = pnxServices.getPnxData("test")
                .getAsJsonObject();
        assertEquals(condensedPnxFromFile1, result);

    }

    @Test
    public void getFullPnxResponse() throws IOException, URISyntaxException {
        PnxServiceConnection pnxServiceConnection = mock(PnxServiceConnection.class);
        InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                FULL_PNX_EXAMPLE_1)));
        when(pnxServiceConnection.connect(anyString())).thenReturn(inputStreamReader);
        PnxServices pnxServices = new PnxServices(pnxServiceConnection);
        JsonObject fullPnxResult = pnxServices.getFullPNX("test_test");
        assertTrue(fullPnxResult.getAsJsonArray("docs").get(0).getAsJsonObject().has("pnx"));
    }

    @Test
    public void handlesUriException() throws IOException, URISyntaxException {
        PnxServiceConnection pnxServiceConnection = mock(PnxServiceConnection.class);
        when(pnxServiceConnection.connect(anyString()))
                .thenThrow(new URISyntaxException("docId", PnxServices.WRONG_URL_FOR_PRIMO_API));
        PnxServices pnxServices = new PnxServices(pnxServiceConnection);
        JsonObject expected = new JsonObject();
        JsonObject result = pnxServices.getFullPNX("testtest");
        assertEquals(expected, result);
    }

    @Test
    public void handlesIOException() throws IOException, URISyntaxException {
        PnxServiceConnection pnxServiceConnection = mock(PnxServiceConnection.class);
        when(pnxServiceConnection.connect(anyString()))
                .thenThrow(new IOException(PnxServices.ERROR_WHILE_GETTING_AT_PRIMO_API_FOR));
        PnxServices pnxServices = new PnxServices(pnxServiceConnection);
        JsonObject expected = new JsonObject();
        JsonObject result = pnxServices.getFullPNX("testtest");
        assertEquals(expected, result);
    }

    @Test
    public void removesPrimoPrefixCorrectly() {
        String withPrefix = "TN_BIBSYS_ILS71560264980002201";
        String withoutPrefix = "BIBSYS_ILS71560264980002201";
        PnxServices pnxServices = new PnxServices();
        String prepared = pnxServices.removePrimoRecordPrefix(withPrefix);
        assertEquals(withoutPrefix, prepared);
    }

    @Test
    public void itExtractsCorrectData() {
        JsonObject fullPnxExample1 = JsonParser.parseString(createJson(FULL_PNX_EXAMPLE_1)).getAsJsonObject();
        JsonObject fullPnxExample2 = JsonParser.parseString(createJson(FULL_PNX_EXAMPLE_2)).getAsJsonObject();
        JsonObject fullPnxExample3 = JsonParser.parseString(createJson(FULL_PNX_EXAMPLE_3)).getAsJsonObject();
        JsonObject condensedPnxFromFile1 = JsonParser
                .parseString(createJson(CONDENSED_PNX_EXAMPLE_1))
                .getAsJsonObject();
        JsonObject condensedPnxFromFile2 = JsonParser
                .parseString(createJson(CONDENSED_PNX_EXAMPLE_2))
                .getAsJsonObject();
        JsonObject condensedPnxFromFile3 = JsonParser
                .parseString(createJson(CONDENSED_PNX_EXAMPLE_3))
                .getAsJsonObject();
        PnxServices pnxServices = new PnxServices();
        JsonObject pnxServicesCondensedExample1 = pnxServices.extractUsefulDataFromPnxService(fullPnxExample1);
        JsonObject pnxServicesCondensedExample2 = pnxServices.extractUsefulDataFromPnxService(fullPnxExample2);
        JsonObject pnxServicesCondensedExample3 = pnxServices.extractUsefulDataFromPnxService(fullPnxExample3);
        assertEquals(condensedPnxFromFile1, pnxServicesCondensedExample1);
        assertEquals(condensedPnxFromFile2, pnxServicesCondensedExample2);
        assertEquals(condensedPnxFromFile3, pnxServicesCondensedExample3);
    }

    @Test
    public void itMasksApiKeysCorrectly() {
        PnxServiceConnection pnxServiceConnection = mock(PnxServiceConnection.class);
        PnxServices pnxServices = new PnxServices(pnxServiceConnection);
        String mockApiKey1 = "aeroieardfkjlsfdkjskjljadkjdflsdfkajfdlaøsdfasdfjk";
        String expectedMaskedApiKey_1 = "XXXXXXdfjk";
        String mockApiKey2 = "kjfaølfdkjdfk";
        String expectedMaskedApiKey2 = "<API_KEY>";
        assertEquals(expectedMaskedApiKey_1, pnxServices.getMaskedPrimoRestApiKey(mockApiKey1));
        assertEquals(expectedMaskedApiKey2, pnxServices.getMaskedPrimoRestApiKey(mockApiKey2));
    }
}
