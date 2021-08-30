package no.unit.xservice;


import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetXServiceHandlerTest {

    @Test
    public void successfulResponse () {
        GetXServiceHandler getXServiceHandler = new GetXServiceHandler();
        GatewayResponse result = getXServiceHandler.handleRequest(null, null);
        assertEquals(result.getStatusCode(), 200);
    }
}
