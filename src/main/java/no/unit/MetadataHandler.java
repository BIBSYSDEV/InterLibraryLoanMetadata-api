package no.unit;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;
import java.util.Objects;
import javax.ws.rs.core.Response;
import no.unit.ill.services.PnxServices;
import nva.commons.apigateway.exceptions.GatewayResponseSerializingException;
import nva.commons.core.Environment;
import nva.commons.core.JacocoGenerated;
import nva.commons.core.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"PMD.UnusedPrivateMethod"})
public class MetadataHandler implements RequestHandler<Map<String, Object>, GatewayResponse> {

    @JacocoGenerated
    private static final transient Logger log = LoggerFactory.getLogger(MetadataHandler.class);
    public static final String QUERY_STRING_PARAMETERS_KEY = "queryStringParameters";
    public static final String MANDATORY_PARAMETERS_MISSING = "Mandatory parameters 'document_id' is missing.";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "An error occurred, error has been logged";
    public static final String DOCUMENT_ID_KEY = "document_id";
    private transient PnxServices pnxServices;
    private final transient Environment environment;

    public MetadataHandler() {
        this(new Environment(), new PnxServices());
    }

    public MetadataHandler(Environment environment) {
        this(environment, new PnxServices());
    }

    public MetadataHandler(Environment environment, PnxServices pnxServices) {
        this.environment = environment;
        this.pnxServices = pnxServices;
    }


    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        GatewayResponse gatewayResponse = new GatewayResponse(environment);
        if (Objects.isNull(input) || !input.containsKey(QUERY_STRING_PARAMETERS_KEY)) {
            try {
                gatewayResponse.setErrorBody(MANDATORY_PARAMETERS_MISSING);
            } catch (GatewayResponseSerializingException e) {
                log.error(INTERNAL_SERVER_ERROR_MESSAGE, e);
            }
            gatewayResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            return gatewayResponse;
        }
        @SuppressWarnings("unchecked")
        Map<String, String> queryStringParameters = (Map<String, String>) input.get(QUERY_STRING_PARAMETERS_KEY);
        String documentId = queryStringParameters.get(DOCUMENT_ID_KEY);

        if (StringUtils.isEmpty(documentId)) {
            try {
                gatewayResponse.setErrorBody(MANDATORY_PARAMETERS_MISSING);
            } catch (GatewayResponseSerializingException e) {
                log.error(INTERNAL_SERVER_ERROR_MESSAGE, e);
            }
            gatewayResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            return gatewayResponse;
        }
        String pnxServiceObject = getPnxServiceData(documentId);
        // InstitutionService institutionService = new InstitutionService();
        // final String libraryCode = institutionService.get("oriaCode", "NTNU_UB", "oriaDefaultNCIPserver");
        // pnxServiceObject.add("libraryCode", libraryCode);
        return new GatewayResponse(environment, pnxServiceObject, Response.Status.OK.getStatusCode());
    }

    protected String getPnxServiceData(String documentId) {
        return pnxServices.getPnxData(documentId);
    }

}
