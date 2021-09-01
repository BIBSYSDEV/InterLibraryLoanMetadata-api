package no.unit.ill;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.ws.rs.core.Response;
import java.util.Objects;


import java.util.Map;

import static no.unit.utils.StringUtils.isEmpty;

public class MetadataHandler implements RequestHandler<Map<String, Object>, GatewayResponse> {
    public static final String QUERY_STRING_PARAMETERS_KEY = "queryStringParameters";
    public static final String MANDATORY_PARAMETERS_MISSING = "Mandatory parameters 'document_id' is missing.";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "An error occurred, error has been logged";
    public static final String DOCUMENT_ID_KEY = "document_id";

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        GatewayResponse gatewayResponse = new GatewayResponse();



        if (Objects.isNull(input) || !input.containsKey(QUERY_STRING_PARAMETERS_KEY)) {
            gatewayResponse.setErrorBody(MANDATORY_PARAMETERS_MISSING);
            gatewayResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            return gatewayResponse;
        }
        Map<String, String> queryStringParameters = (Map<String, String>) input.get(QUERY_STRING_PARAMETERS_KEY);
        String documentId = queryStringParameters.get(DOCUMENT_ID_KEY);

        if(isEmpty(documentId)) {
            gatewayResponse.setErrorBody(MANDATORY_PARAMETERS_MISSING);
            gatewayResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            return gatewayResponse;
        }
        return new GatewayResponse("test", 200);
    }
}
