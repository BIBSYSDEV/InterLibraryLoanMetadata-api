package no.unit.ncip;

import static nva.commons.apigateway.ApiGatewayHandler.ALLOWED_ORIGIN_ENV;
import static nva.commons.core.JsonUtils.objectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.file.Path;
import no.unit.utils.ParameterException;
import nva.commons.apigateway.GatewayResponse;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.core.Environment;
import nva.commons.core.ioutils.IoUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NcipHandlerTest {

    private Environment environment;
    private NcipHandler handler;
    private NcipService ncipService;
    private Context context;
    public static final String NCIP_TRANSFER_MESSAGE = "ncipTransferMessage.json";
    public static final String INCOMPLETE_NCIP_TRANSFER_MESSAGE = "incompleteNcipTransferMessage.json";
    public static final String MESSAGE = "message";
    public static final String SUCCESS = "Success";
    public static final String FAILURE = "failure";

    /**
     * javadoc for checkstyle.
     */
    @BeforeEach
    public void init() {
        environment = mock(Environment.class);
        ncipService = mock(NcipService.class);
        when(environment.readEnv(ALLOWED_ORIGIN_ENV)).thenReturn("*");
        handler = new NcipHandler(environment);
        this.context = mock(Context.class);
    }

    @Test
    void getSuccessStatusCodeReturnsOK() {
        NcipHandler handler = new NcipHandler(environment);
        var response =  new no.unit.GatewayResponse(environment, MESSAGE, HttpStatus.SC_OK);
        Integer statusCode = handler.getSuccessStatusCode(null, response);
        assertEquals(statusCode, HttpStatus.SC_OK);
    }


    @Test
    void handlerThrowsExceptionWithEmptyRequest()  {
        Exception exception = assertThrows(ParameterException.class, () -> {
            handler.processInput(null, new RequestInfo(), mock(Context.class));
        });

        assertTrue(exception.getMessage().contains(NcipHandler.NO_PARAMETERS_GIVEN_TO_HANDLER));
    }

    @Test
    void handleNcipMessageWithSuccess() throws ApiGatewayException, JsonProcessingException {
        String msg = IoUtils.stringFromResources(Path.of(NCIP_TRANSFER_MESSAGE));
        final NcipTransferMessage ncipTransferMessage = objectMapper.readValue(msg, NcipTransferMessage.class);
        NcipResponse ncipResponse = new NcipResponse();
        ncipResponse.status = HttpStatus.SC_OK;
        ncipResponse.message = SUCCESS;
        when(ncipService.send(anyString(), anyString())).thenReturn(ncipResponse);
        NcipRequest request = new NcipRequest(ncipTransferMessage);
        var handler = new NcipHandler(environment, ncipService);
        var actual = handler.processInput(request, new RequestInfo(), context);
        assertEquals(HttpStatus.SC_OK, actual.getStatusCode());
        assertEquals(SUCCESS, actual.getBody());
    }

    @Test
    void testMissingMandatoryParamsInNcipTransferMessage() throws ApiGatewayException, JsonProcessingException {
        NcipResponse ncipResponse = new NcipResponse();
        ncipResponse.status = HttpStatus.SC_BAD_REQUEST;
        ncipResponse.message = FAILURE;
        when(ncipService.send(anyString(), anyString())).thenReturn(ncipResponse);
        String msg = IoUtils.stringFromResources(Path.of(INCOMPLETE_NCIP_TRANSFER_MESSAGE));
        NcipTransferMessage ncipTransferMessage = objectMapper.readValue(msg, NcipTransferMessage.class);
        NcipRequest request = new NcipRequest(ncipTransferMessage);
        var handler = new NcipHandler(environment, ncipService);
        var actual = handler.processInput(request, new RequestInfo(), context);
        assertEquals(HttpStatus.SC_BAD_REQUEST, actual.getStatusCode());
        assertTrue(actual.getBody().contains(NcipHandler.NCIP_MESSAGE_IS_NOT_VALID));
    }
}
