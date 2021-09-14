package no.unit.libcheck;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import no.unit.GatewayResponse;
import no.unit.ill.services.BaseBibliotekBean;
import no.unit.ill.services.BaseBibliotekService;
import nva.commons.apigateway.ApiGatewayHandler;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.apigateway.exceptions.BadRequestException;
import nva.commons.core.Environment;
import nva.commons.core.JacocoGenerated;

import javax.xml.bind.JAXBException;
import java.net.HttpURLConnection;

import static org.apache.commons.lang3.StringUtils.isEmpty;


public class LibcheckHandler extends ApiGatewayHandler<Void, GatewayResponse> {

    public static final String LIBUSER_KEY = "libuser";
    public static final String IS_ALMA_LIBRARY = "isAlmaLibrary";
    public static final String IS_NCIP_LIBRARY = "isNcipLibrary";
    public static final String ALMA_KATSYST = "Alma";
    public static final String LIBRARY_NOT_FOUND = "Library not found: ";

    private final transient BaseBibliotekService basebibliotekService;

    @JacocoGenerated
    public LibcheckHandler() throws JAXBException {
        this(new Environment());
    }

    /**
     * Constructor for injecting used in testing.
     *
     * @param environment environment
     */
    public LibcheckHandler(Environment environment) throws JAXBException {
        super(Void.class, environment);
        this.basebibliotekService = new BaseBibliotekService();
    }

    /**
     * Constructor for injecting used in testing.
     *
     * @param environment environment
     */
    public LibcheckHandler(Environment environment, BaseBibliotekService basebibliotekService) {
        super(Void.class, environment);
        this.basebibliotekService = basebibliotekService;
    }

    @Override
    protected GatewayResponse processInput(Void input, RequestInfo requestInfo, Context context)
            throws ApiGatewayException {

        String libuser = requestInfo.getQueryParameter(LIBUSER_KEY);

        BaseBibliotekBean libraryData = getLibraryData(libuser);

        JsonObject libcheckJsonObject = new JsonObject();
        libcheckJsonObject.addProperty(IS_ALMA_LIBRARY, ALMA_KATSYST.equalsIgnoreCase(libraryData.getKatsyst()));
        libcheckJsonObject.addProperty(IS_NCIP_LIBRARY, !isEmpty(libraryData.getNncippServer()));

        return new GatewayResponse(environment, libcheckJsonObject.toString(), HttpURLConnection.HTTP_OK);
    }

    @Override
    protected Integer getSuccessStatusCode(Void input, GatewayResponse output) {
        return HttpURLConnection.HTTP_OK;
    }

    private BaseBibliotekBean getLibraryData(String libuser) throws BadRequestException {
        BaseBibliotekBean libraryData = basebibliotekService.libraryLookupByBibnr(libuser);
        if (libraryData != null) {
            return libraryData;
        }
        throw new BadRequestException(LIBRARY_NOT_FOUND + libuser);
    }
}
