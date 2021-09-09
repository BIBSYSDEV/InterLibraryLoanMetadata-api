package no.unit;


import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MetadataHandlerTest {
    public static final String CONDENSED_PNX_EXAMPLE_1 = "condensed_pnx_example_1.json";
    public static final String DOCUMENT_ID = "BIBSYS_ILS71560264980002201";

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
    public void successfulResponse() {
        Context awsContext = mock(Context.class);
        Map<String, Object> event = new HashMap<>();
        Map<String, String> queryParameters = new HashMap<>();
        String fysiologibok = "BIBSYS_ILS71560264980002201";
        String leksikon = "BIBSYS_ILS71463631120002201";
        JsonObject condensedExample1 = JsonParser.parseString(createJSON(CONDENSED_PNX_EXAMPLE_1)).getAsJsonObject();
        queryParameters.put(MetadataHandler.DOCUMENT_ID_KEY, leksikon);
        event.put(MetadataHandler.QUERY_STRING_PARAMETERS_KEY, queryParameters);
        MetadataHandler app = new MetadataHandler();
        GatewayResponse result = app.handleRequest(event, awsContext);
        System.out.println(result.getBody());
        assertEquals(200, result.getStatusCode());
    }
}
