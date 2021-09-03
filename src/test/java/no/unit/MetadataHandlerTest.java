package no.unit;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import no.unit.ill.GatewayResponse;
import no.unit.ill.MetadataHandler;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

public class MetadataHandlerTest {

    @Test
    public void successfulResponse() {
        Context awsContext = mock(Context.class);
        LambdaLogger logger = mock(LambdaLogger.class);
        Map<String, Object> event = new HashMap<>();
        Map<String, String> queryParameters = new HashMap<>();
        String fysiologibok = "BIBSYS_ILS71560264980002201";
        String leksikon = "BIBSYS_ILS71463631120002201";
        queryParameters.put(MetadataHandler.DOCUMENT_ID_KEY, leksikon);
        event.put(MetadataHandler.QUERY_STRING_PARAMETERS_KEY, queryParameters);
        MetadataHandler app = new MetadataHandler();
        when(awsContext.getLogger()).thenReturn(logger);
        doNothing().when(logger).log(anyString());
        GatewayResponse result = app.handleRequest(event, awsContext);
        System.out.println(result.getBody());
        assertEquals(200, result.getStatusCode());
    }
}
