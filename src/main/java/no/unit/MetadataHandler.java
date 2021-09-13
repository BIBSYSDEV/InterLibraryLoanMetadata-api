package no.unit;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import no.unit.ill.services.PnxServices;

import javax.ws.rs.core.Response;
import java.util.Objects;

import nva.commons.core.Environment;
import java.util.Map;
import no.unit.ill.services.InstitutionService;

import static no.unit.utils.StringUtils.isEmpty;

@SuppressWarnings({"PMD.UnusedPrivateMethod"})
public class MetadataHandler implements RequestHandler<Map<String, Object>, GatewayResponse> {
    public static final String QUERY_STRING_PARAMETERS_KEY = "queryStringParameters";
    public static final String MANDATORY_PARAMETERS_MISSING = "Mandatory parameters 'document_id' is missing.";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "An error occurred, error has been logged";
    public static final String DOCUMENT_ID_KEY = "document_id";
    private final transient PnxServices pnxServices;

    public MetadataHandler() {
        this.pnxServices = new PnxServices();
    }

    public MetadataHandler(PnxServices pnxServices) {
        this.pnxServices = pnxServices;
    }


    private final transient Environment environment;

    public MetadataHandler() {
        this.environment = new Environment();
    }

    public MetadataHandler(Environment environment) {
        this.environment = environment;
    }

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        GatewayResponse gatewayResponse = new GatewayResponse();


        if (Objects.isNull(input) || !input.containsKey(QUERY_STRING_PARAMETERS_KEY)) {
            gatewayResponse.setErrorBody(MANDATORY_PARAMETERS_MISSING);
            gatewayResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            return gatewayResponse;
        }
        @SuppressWarnings("unchecked")
        Map<String, String> queryStringParameters = (Map<String, String>) input.get(QUERY_STRING_PARAMETERS_KEY);
        String documentId = queryStringParameters.get(DOCUMENT_ID_KEY);

        if (isEmpty(documentId)) {
            gatewayResponse.setErrorBody(MANDATORY_PARAMETERS_MISSING);
            gatewayResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            return gatewayResponse;
        }
        String pnxServiceObject = getXServiceData(documentId);
        // InstitutionService institutionService = new InstitutionService();
        // final String libraryCode = institutionService.get("oriaCode", "NTNU_UB", "oriaDefaultNCIPserver");
        // pnxServiceObject.add("libraryCode", libraryCode);
        return new GatewayResponse(pnxServiceObject, Response.Status.OK.getStatusCode());
    }

    protected String getXServiceData(String documentId) {
        return pnxServices.getPnxData(documentId);
    }

}
