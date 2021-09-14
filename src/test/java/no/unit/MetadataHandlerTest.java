package no.unit;

import static nva.commons.apigateway.ApiGatewayHandler.ALLOWED_ORIGIN_ENV;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.lambda.runtime.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;
import no.unit.ill.services.PnxServices;
import nva.commons.core.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetadataHandlerTest {

    public static final String CONDENSED_PNX_EXAMPLE_1 = "condensed_pnx_example_1.json";
    private Environment environment;
    private PnxServices pnxServices;
    private MetadataHandler app;
    private Context awsContext;

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
    public void noRecordIdSet() {
        when(environment.readEnv(ALLOWED_ORIGIN_ENV)).thenReturn(GatewayResponse.CORS_ALLOW_ORIGIN_HEADER);
        Map<String, Object> event = new HashMap<>();
        String condensedExample1 = createJson(CONDENSED_PNX_EXAMPLE_1);
        when(pnxServices.getPnxData(anyString())).thenReturn(condensedExample1);
        GatewayResponse result = app.handleRequest(null, awsContext);
        GatewayResponse result2 = app.handleRequest(event, awsContext);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatusCode());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result2.getStatusCode());
    }

    @Test
    public void recordIdIsEmptyString() {
        Map<String, Object> event = new HashMap<>();
        Map<String, String> queryParameters = new HashMap<>();
        String leksikon = "";
        String condensedExample1 = createJson(CONDENSED_PNX_EXAMPLE_1);
        queryParameters.put(MetadataHandler.DOCUMENT_ID_KEY, leksikon);
        event.put(MetadataHandler.QUERY_STRING_PARAMETERS_KEY, queryParameters);
        when(pnxServices.getPnxData(anyString())).thenReturn(condensedExample1);
        Context awsContext = mock(Context.class);
        GatewayResponse result = app.handleRequest(event, awsContext);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatusCode());
    }



    @Test
    public void successfulResponse() {
        Map<String, Object> event = new HashMap<>();
        Map<String, String> queryParameters = new HashMap<>();
        String leksikon = "BIBSYS_ILS71463631120002201";
        String condensedExample1 = createJson(CONDENSED_PNX_EXAMPLE_1);
        queryParameters.put(MetadataHandler.DOCUMENT_ID_KEY, leksikon);
        event.put(MetadataHandler.QUERY_STRING_PARAMETERS_KEY, queryParameters);
        when(pnxServices.getPnxData(anyString())).thenReturn(condensedExample1);
        final GatewayResponse result = app.handleRequest(event, awsContext);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatusCode());
    }
}
