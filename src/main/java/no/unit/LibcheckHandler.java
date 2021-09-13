package no.unit;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import no.unit.ill.services.BaseBibliotekBean;
import no.unit.ill.services.BaseBibliotekService;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.util.Map;
import java.util.Objects;

import static no.unit.utils.StringUtils.isEmpty;

public class LibcheckHandler implements RequestHandler<Map<String, Object>, GatewayResponse> {

    public static final String QUERY_STRING_PARAMETERS_KEY = "queryStringParameters";
    public static final String MANDATORY_PARAMETERS_MISSING = "Mandatory parameter 'libuser' is missing.";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "An error occurred, error has been logged";
    public static final String LIBUSER_KEY = "libuser";
    private final transient BaseBibliotekService basebibliotekService;

    public LibcheckHandler() throws JAXBException {
        this.basebibliotekService = new BaseBibliotekService();
    }

    public LibcheckHandler(BaseBibliotekService basebibliotekService) {
        this.basebibliotekService = basebibliotekService;
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
        String libuser = queryStringParameters.get(LIBUSER_KEY);

        if (isEmpty(libuser)) {
            gatewayResponse.setErrorBody(MANDATORY_PARAMETERS_MISSING);
            gatewayResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            return gatewayResponse;
        }

        BaseBibliotekBean libraryData = getLibraryData(libuser);

        JsonObject libcheckJsonObject = new JsonObject();
        libcheckJsonObject.addProperty("isAlmaLibrary", "Alma".equalsIgnoreCase(libraryData.getKatsyst()));
        libcheckJsonObject.addProperty("isNcipLibrary", !isEmpty(libraryData.getNncippServer()));

        return new GatewayResponse(libcheckJsonObject.toString(), Response.Status.OK.getStatusCode());
    }

    protected BaseBibliotekBean getLibraryData(String libuser) {
        return basebibliotekService.libraryLookupByBibnr(libuser);
    }

}
