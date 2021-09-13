package no.unit;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import no.unit.ill.services.BaseBibliotekBean;
import no.unit.ill.services.BaseBibliotekService;
import nva.commons.apigateway.ApiGatewayHandler;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.core.Environment;
import nva.commons.core.JacocoGenerated;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isEmpty;


public class LibcheckHandler extends ApiGatewayHandler<Void, GatewayResponse> {

    public static final String LIBUSER_KEY = "libuser";

    private final transient BaseBibliotekService basebibliotekService;


    @JacocoGenerated
    public LibcheckHandler() throws JAXBException {
        this(new Environment());
    }

    /**
     * Constructor for injecting used in testing.
     * @param environment environment
     */
    public LibcheckHandler(Environment environment) throws JAXBException {
        super(Void.class, environment);
        this.basebibliotekService = new BaseBibliotekService();
    }

    /**
     * Constructor for injecting used in testing.
     * @param environment environment
     */
    public LibcheckHandler(Environment environment, BaseBibliotekService basebibliotekService) {
        super(Void.class, environment);
        this.basebibliotekService = basebibliotekService;
    }


    @Override
    protected GatewayResponse processInput(Void input, RequestInfo requestInfo, Context context) {

        String libuser = requestInfo.getQueryParameters().get(LIBUSER_KEY);
        BaseBibliotekBean libraryData = basebibliotekService.libraryLookupByBibnr(libuser);

        JsonObject libcheckJsonObject = new JsonObject();
        libcheckJsonObject.addProperty("isAlmaLibrary", "Alma".equalsIgnoreCase(libraryData.getKatsyst()));
        libcheckJsonObject.addProperty("isNcipLibrary", !isEmpty(libraryData.getNncippServer()));

        return new GatewayResponse(environment, libcheckJsonObject.toString(), HttpURLConnection.HTTP_OK);
    }

    @Override
    protected Integer getSuccessStatusCode(Void input, GatewayResponse output) {
        return HttpURLConnection.HTTP_OK;
    }
}
