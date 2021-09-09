package no.unit;


import nva.commons.core.Environment;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class MetadataHandlerTest {

    @Test
    public void successfulResponse() {
        Environment env = mock(Environment.class);
        MetadataHandler app = new MetadataHandler(env);
        GatewayResponse result = app.handleRequest(null, null);
        assertEquals(result.getStatusCode(), 200);
    }
}
