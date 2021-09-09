package no.unit;


import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.unit.pnxservice.Pnxervices;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MetadataHandlerTest {
    public static final String CONDENSED_PNX_EXAMPLE_1 = "condensed_pnx_example_1.json";

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
    public void testingDefaultConstructor(){
        //for test coverage
        MetadataHandler app = new MetadataHandler();
    }

    @Test
    public void noRecordIdSet(){
        Context awsContext = mock(Context.class);
        Pnxervices pnxervices = mock(Pnxervices.class);
        Map<String, Object> event = new HashMap<>();
        JsonObject condensedExample1 = JsonParser.parseString(createJSON(CONDENSED_PNX_EXAMPLE_1)).getAsJsonObject();
        MetadataHandler app = new MetadataHandler(pnxervices);
        when(pnxervices.getPnxData(anyString())).thenReturn(condensedExample1);
        GatewayResponse result = app.handleRequest(null, awsContext);
        GatewayResponse result2 = app.handleRequest(event, awsContext);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatusCode());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result2.getStatusCode());
    }

    @Test
    public void recordIdIsEmptyString(){
        Context awsContext = mock(Context.class);
        Pnxervices pnxervices = mock(Pnxervices.class);
        Map<String, Object> event = new HashMap<>();
        Map<String, String> queryParameters = new HashMap<>();
        String leksikon = "";
        JsonObject condensedExample1 = JsonParser.parseString(createJSON(CONDENSED_PNX_EXAMPLE_1)).getAsJsonObject();
        queryParameters.put(MetadataHandler.DOCUMENT_ID_KEY, leksikon);
        event.put(MetadataHandler.QUERY_STRING_PARAMETERS_KEY, queryParameters);
        MetadataHandler app = new MetadataHandler(pnxervices);
        when(pnxervices.getPnxData(anyString())).thenReturn(condensedExample1);
        GatewayResponse result = app.handleRequest(event, awsContext);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatusCode());
    }



    @Test
    public void successfulResponse() {
        Context awsContext = mock(Context.class);
        Pnxervices pnxervices = mock(Pnxervices.class);
        Map<String, Object> event = new HashMap<>();
        Map<String, String> queryParameters = new HashMap<>();
        String leksikon = "BIBSYS_ILS71463631120002201";
        JsonObject condensedExample1 = JsonParser.parseString(createJSON(CONDENSED_PNX_EXAMPLE_1)).getAsJsonObject();
        queryParameters.put(MetadataHandler.DOCUMENT_ID_KEY, leksikon);
        event.put(MetadataHandler.QUERY_STRING_PARAMETERS_KEY, queryParameters);
        MetadataHandler app = new MetadataHandler(pnxervices);
        when(pnxervices.getPnxData(anyString())).thenReturn(condensedExample1);
        GatewayResponse result = app.handleRequest(event, awsContext);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatusCode());
    }
}
