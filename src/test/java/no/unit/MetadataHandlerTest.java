package no.unit;

import static nva.commons.apigateway.ApiGatewayHandler.ALLOWED_ORIGIN_ENV;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;
import no.unit.ill.services.PnxServices;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.core.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetadataHandlerTest {

    public static final String CONDENSED_PNX_EXAMPLE_1 = "condensed_pnx_example_1.json";
    private Environment environment;
    private PnxServices pnxServices;
    private MetadataHandler app;
    private Context awsContext;

    private JsonObject createJson(String filename) {

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
        return JsonParser.parseString(contentBuilder.toString()).getAsJsonObject();
    }

    /**
     * javadoc for checkstyle.
     */
    @BeforeEach
    public void init() {
        environment = mock(Environment.class);
        pnxServices = mock(PnxServices.class);
        awsContext = mock(Context.class);
        when(environment.readEnv(ALLOWED_ORIGIN_ENV)).thenReturn("*");
        app = new MetadataHandler(environment, pnxServices);
    }

    @Test
    public void testingDefaultConstructor() {
        //for test coverage
        MetadataHandler app = new MetadataHandler();
    }

    @Test
    public void noRecordIdSet() throws ApiGatewayException {
        when(environment.readEnv(ALLOWED_ORIGIN_ENV)).thenReturn(GatewayResponse.CORS_ALLOW_ORIGIN_HEADER);
        JsonObject condensedExample1 = createJson(CONDENSED_PNX_EXAMPLE_1);
        when(pnxServices.getPnxData(anyString())).thenReturn(condensedExample1);
        MetadataResponse result = app.processInput(null,null, awsContext);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result);
    }

    @Test
    public void recordIdIsEmptyString() {
        Map<String, Object> event = new HashMap<>();
        Map<String, String> queryParameters = new HashMap<>();
        String leksikon = "";
        JsonObject condensedExample1 = createJson(CONDENSED_PNX_EXAMPLE_1);
        queryParameters.put(MetadataHandler.DOCUMENT_ID_KEY, leksikon);
        event.put(MetadataHandler.QUERY_STRING_PARAMETERS_KEY, queryParameters);
        when(pnxServices.getPnxData(anyString())).thenReturn(condensedExample1);
        Context awsContext = mock(Context.class);
//        MetadataResponse result = app.processInput(event, awsContext);
//        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result);
    }



    @Test
    public void successfulResponse() {
        Map<String, Object> event = new HashMap<>();
        Map<String, String> queryParameters = new HashMap<>();
        String leksikon = "BIBSYS_ILS71463631120002201";
        JsonObject condensedExample1 = createJson(CONDENSED_PNX_EXAMPLE_1);
        queryParameters.put(MetadataHandler.DOCUMENT_ID_KEY, leksikon);
        event.put(MetadataHandler.QUERY_STRING_PARAMETERS_KEY, queryParameters);
        when(pnxServices.getPnxData(anyString())).thenReturn(condensedExample1);
//        final MetadataResponse result = app.processInput(event, awsContext);
//        assertEquals(Response.Status.OK.getStatusCode(), result);
    }
}
