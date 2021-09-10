package no.unit;


import com.amazonaws.services.lambda.runtime.Context;
import no.unit.ill.services.PnxServices;
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
    public void testingDefaultConstructor() {
        //for test coverage
        MetadataHandler app = new MetadataHandler();
    }

    @Test
    public void noRecordIdSet() {
        Context awsContext = mock(Context.class);
        PnxServices pnxServices = mock(PnxServices.class);
        Map<String, Object> event = new HashMap<>();
        String condensedExample1 = createJson(CONDENSED_PNX_EXAMPLE_1);
        MetadataHandler app = new MetadataHandler(pnxServices);
        when(pnxServices.getPnxData(anyString())).thenReturn(condensedExample1);
        GatewayResponse result = app.handleRequest(null, awsContext);
        GatewayResponse result2 = app.handleRequest(event, awsContext);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatusCode());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result2.getStatusCode());
    }

    @Test
    public void recordIdIsEmptyString() {
        PnxServices pnxServices = mock(PnxServices.class);
        Map<String, Object> event = new HashMap<>();
        Map<String, String> queryParameters = new HashMap<>();
        String leksikon = "";
        String condensedExample1 = createJson(CONDENSED_PNX_EXAMPLE_1);
        queryParameters.put(MetadataHandler.DOCUMENT_ID_KEY, leksikon);
        event.put(MetadataHandler.QUERY_STRING_PARAMETERS_KEY, queryParameters);
        MetadataHandler app = new MetadataHandler(pnxServices);
        when(pnxServices.getPnxData(anyString())).thenReturn(condensedExample1);
        Context awsContext = mock(Context.class);
        GatewayResponse result = app.handleRequest(event, awsContext);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatusCode());
    }



    @Test
    public void successfulResponse() {
        PnxServices pnxServices = mock(PnxServices.class);
        Map<String, Object> event = new HashMap<>();
        Map<String, String> queryParameters = new HashMap<>();
        String leksikon = "BIBSYS_ILS71463631120002201";
        String condensedExample1 = createJson(CONDENSED_PNX_EXAMPLE_1);
        queryParameters.put(MetadataHandler.DOCUMENT_ID_KEY, leksikon);
        event.put(MetadataHandler.QUERY_STRING_PARAMETERS_KEY, queryParameters);
        MetadataHandler app = new MetadataHandler(pnxServices);
        when(pnxServices.getPnxData(anyString())).thenReturn(condensedExample1);
        Context awsContext = mock(Context.class);
        GatewayResponse result = app.handleRequest(event, awsContext);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatusCode());
    }
}
