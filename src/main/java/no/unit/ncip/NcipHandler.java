package no.unit.ncip;

import static java.util.Objects.isNull;

import com.amazonaws.services.lambda.runtime.Context;
import no.unit.GatewayResponse;
import no.unit.utils.NcipUtils;
import no.unit.utils.ParameterException;
import nva.commons.apigateway.ApiGatewayHandler;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.core.Environment;
import nva.commons.core.JacocoGenerated;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NcipHandler extends ApiGatewayHandler<NcipRequest, GatewayResponse> {

    @JacocoGenerated
    private static final transient Logger log = LoggerFactory.getLogger(NcipHandler.class);
    public static final String NO_PARAMETERS_GIVEN_TO_HANDLER = "No parameters given to Handler";
    public static final String NCIP_MESSAGE_IS_NOT_VALID = "NCIP message is not valid: ";
    public static final String NCIP_RESPONSE_FROM_SERVER = "Ill - NCIP response from server: ";
    public static final String NCIP_XML_SEND_TO = "Ill - NCIP xml send to: ";
    public static final String COLON = ": ";
    private transient NcipService ncipService;

    @JacocoGenerated
    public NcipHandler() {
        this(new Environment());
    }

    /**
     * Constructor for injecting used in testing.
     * @param environment environment
     */
    public NcipHandler(Environment environment) {
        super(NcipRequest.class, environment);
        this.ncipService = new NcipService();
    }

    /**
     * Constructor for injecting used in testing.
     * @param environment environment
     */
    public NcipHandler(Environment environment, NcipService ncipService) {
        super(NcipRequest.class, environment);
        this.ncipService = ncipService;
    }



    /**
     * Implements the main logic of the handler. Any exception thrown by this method will be handled by method.
     *
     * @param request     The input object to the method. Usually a deserialized json.
     * @param requestInfo Request headers and path.
     * @param context     the ApiGateway context.
     * @return the Response body that is going to be serialized in json
     * @throws ApiGatewayException all exceptions are caught by writeFailure and mapped to error codes through the
     *                             method
     */
    @Override
    protected GatewayResponse processInput(NcipRequest request, RequestInfo requestInfo,
                                           Context context) throws ApiGatewayException {
        if (isNull(request)) {
            throw new ParameterException(NO_PARAMETERS_GIVEN_TO_HANDLER);
        }
        final NcipTransferMessage transferMessage = request.getTransferMessage();
        log.debug("json input looks like that :" + transferMessage.toString());
        GatewayResponse gatewayResponse = new GatewayResponse(environment);
        if (transferMessage.isValid()) {
            String xmlMessage = NcipUtils.ncipMessageAsXml(transferMessage);
            String ncipServerUrl = transferMessage.getNcipServerUrl();
            log.info(NCIP_XML_SEND_TO + ncipServerUrl + System.lineSeparator() + xmlMessage);
            final NcipResponse ncipResponse = ncipService.send(xmlMessage, ncipServerUrl);
            log.info(NCIP_RESPONSE_FROM_SERVER + ncipServerUrl + System.lineSeparator() + ncipResponse);
            gatewayResponse.setStatusCode(ncipResponse.status);
            if (ncipResponse.status < HttpStatus.SC_BAD_REQUEST) {
                gatewayResponse.setBody(ncipResponse.message);
            } else  {
                gatewayResponse.setErrorBody(ncipResponse.message + COLON + ncipResponse.problemdetail);
            }
        } else {
            log.error(NCIP_MESSAGE_IS_NOT_VALID + transferMessage);
            gatewayResponse.setErrorBody(NCIP_MESSAGE_IS_NOT_VALID + transferMessage);
            gatewayResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }

        return gatewayResponse;
    }

    @Override
    protected Integer getSuccessStatusCode(NcipRequest input, GatewayResponse output) {
        return output.getStatusCode();
    }



}
