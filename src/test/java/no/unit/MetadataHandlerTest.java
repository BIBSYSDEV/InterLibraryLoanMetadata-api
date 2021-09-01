package no.unit;


import no.unit.ill.GatewayResponse;
import no.unit.ill.MetadataHandler;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class MetadataHandlerTest {

    @Test
    public void successfulResponse() {
        MetadataHandler app = new MetadataHandler();
        GatewayResponse result = app.handleRequest(null, null);
        assertEquals(result.getStatusCode(), 200);
    }
}
