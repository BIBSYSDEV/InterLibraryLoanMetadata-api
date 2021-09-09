package no.unit;

import static nva.commons.apigateway.ApiGatewayHandler.ALLOWED_ORIGIN_ENV;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;
import nva.commons.apigateway.exceptions.GatewayResponseSerializingException;
import nva.commons.core.Environment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GatewayResponseTest {

    private static final String EMPTY_STRING = "";

    public static final String CORS_HEADER = "CORS header";
    public static final String MOCK_BODY = "mock";
    public static final String ERROR_BODY = "error";
    public static final String ERROR_JSON = "{\"error\":\"error\"}";

    @Test
    public void testErrorResponse() throws GatewayResponseSerializingException {
        Environment env = mock(Environment.class);
        GatewayResponse gatewayResponse = new GatewayResponse(env);
        gatewayResponse.setBody(null);
        gatewayResponse.setErrorBody(ERROR_BODY);
        gatewayResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Assertions.assertEquals(ERROR_JSON, gatewayResponse.getBody());
    }

    @Test
    public void testNoCorsHeaders() {
        Environment env = mock(Environment.class);
        final Config config = Config.getInstance();
        config.setCorsHeader(EMPTY_STRING);
        final String corsHeader = config.getCorsHeader();
        GatewayResponse gatewayResponse =
            new GatewayResponse(env, MOCK_BODY, Response.Status.BAD_REQUEST.getStatusCode());
        Assertions.assertFalse(gatewayResponse.getHeaders().containsKey(GatewayResponse.CORS_ALLOW_ORIGIN_HEADER));
        Assertions.assertFalse(gatewayResponse.getHeaders().containsValue(corsHeader));

        when(env.readEnv(ALLOWED_ORIGIN_ENV)).thenReturn(GatewayResponse.CORS_ALLOW_ORIGIN_HEADER);
        config.setCorsHeader(CORS_HEADER);
        GatewayResponse gatewayResponse1 =
            new GatewayResponse(env, MOCK_BODY, Response.Status.BAD_REQUEST.getStatusCode());
        Assertions.assertTrue(gatewayResponse1.getHeaders().containsKey(GatewayResponse.CORS_ALLOW_ORIGIN_HEADER));
    }

}
