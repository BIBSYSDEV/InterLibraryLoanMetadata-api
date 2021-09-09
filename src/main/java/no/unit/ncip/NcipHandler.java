package no.unit.ncip;

import static java.util.Objects.isNull;

import com.amazonaws.services.lambda.runtime.Context;
import java.util.Map;
import java.util.Objects;
import no.unit.GatewayResponse;
import no.unit.ill.services.InstitutionService;
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

    public static final String NO_PARAMETERS_GIVEN_TO_HANDLER = "No parameters given to Handler";
    private static final transient Logger log = LoggerFactory.getLogger(NcipHandler.class);
    public static final String NCIP_MESSAGE_IS_NOT_VALID = "Ncip message is not valid: ";

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
        final NcipMessage message = request.getMessage();
        log.debug("json input looks like that :" + message.toString());
        GatewayResponse gatewayResponse = new GatewayResponse(environment);
        if (message.isValid()) {
            //TODO: the real action comes here
        } else {
            log.error(NCIP_MESSAGE_IS_NOT_VALID + message);
            gatewayResponse.setErrorBody(NCIP_MESSAGE_IS_NOT_VALID + message);
            gatewayResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }

        InstitutionService institutionService = new InstitutionService();
        final String libraryCode = institutionService.get("oriaCode", "NTNU_UB", "oriaDefaultNCIPserver");
        gatewayResponse.setStatusCode(200);
        gatewayResponse.setBody("libraryCode=" + libraryCode);
        return gatewayResponse;
    }

    @Override
    protected Integer getSuccessStatusCode(NcipRequest input, GatewayResponse output) {
        return output.getStatusCode();
    }

}
