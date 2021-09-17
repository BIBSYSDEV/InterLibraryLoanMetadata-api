package no.unit.ncip;

import static no.unit.ncip.NcipHandler.DASH;
import static no.unit.ncip.NcipHandler.NCIP_MESSAGE_IS_NOT_VALID;
import static nva.commons.apigateway.ApiGatewayHandler.ALLOWED_ORIGIN_ENV;
import static nva.commons.core.JsonUtils.objectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.HttpURLConnection;
import java.nio.file.Path;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.apigateway.exceptions.BadRequestException;
import nva.commons.core.Environment;
import nva.commons.core.ioutils.IoUtils;
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
        var response =  new NcipResponse();
        Integer statusCode = handler.getSuccessStatusCode(null, response);
        assertEquals(statusCode, HttpURLConnection.HTTP_OK);
    }


    @Test
    void handlerThrowsExceptionWithEmptyRequest()  {
        Exception exception = assertThrows(BadRequestException.class, () -> {
            handler.processInput(null, new RequestInfo(), mock(Context.class));
        });
        assertTrue(exception.getMessage().contains(NcipHandler.NO_PARAMETERS_GIVEN_TO_HANDLER));
    }

    @Test
    void handleNcipMessageWithSuccess() throws ApiGatewayException, JsonProcessingException {
        String msg = IoUtils.stringFromResources(Path.of(NCIP_TRANSFER_MESSAGE));
        final NcipTransferMessage ncipTransferMessage = objectMapper.readValue(msg, NcipTransferMessage.class);
        NcipResponse ncipResponse = new NcipResponse();
        ncipResponse.status = HttpURLConnection.HTTP_OK;
        ncipResponse.message = SUCCESS;
        when(ncipService.send(anyString(), anyString())).thenReturn(ncipResponse);
        NcipRequest request = new NcipRequest(ncipTransferMessage);
        var handler = new NcipHandler(environment, ncipService);
        var actual = handler.processInput(request, new RequestInfo(), context);
        assertEquals(HttpURLConnection.HTTP_OK, actual.status);
        assertEquals(SUCCESS, actual.message);
    }

    @Test
    void testMissingMandatoryParamsInNcipTransferMessage() throws JsonProcessingException {
        String msg = IoUtils.stringFromResources(Path.of(INCOMPLETE_NCIP_TRANSFER_MESSAGE));
        NcipTransferMessage ncipTransferMessage = objectMapper.readValue(msg, NcipTransferMessage.class);
        NcipRequest request = new NcipRequest(ncipTransferMessage);
        var handler = new NcipHandler(environment, ncipService);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            handler.processInput(request, new RequestInfo(), context);
        });
        assertTrue(exception.getMessage().contains(NCIP_MESSAGE_IS_NOT_VALID));
    }

    @Test
    void testProblemResponseFromNcipServer() throws JsonProcessingException {
        NcipResponse ncipResponse = new NcipResponse();
        ncipResponse.status = HttpURLConnection.HTTP_BAD_REQUEST;
        ncipResponse.message = MESSAGE;
        ncipResponse.problemdetail = FAILURE;
        when(ncipService.send(anyString(), anyString())).thenReturn(ncipResponse);
        String msg = IoUtils.stringFromResources(Path.of(NCIP_TRANSFER_MESSAGE));
        NcipTransferMessage ncipTransferMessage = objectMapper.readValue(msg, NcipTransferMessage.class);
        NcipRequest request = new NcipRequest(ncipTransferMessage);
        var handler = new NcipHandler(environment, ncipService);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            handler.processInput(request, new RequestInfo(), context);
        });
        assertTrue(exception.getMessage().contains(MESSAGE + DASH + FAILURE));
    }
}
