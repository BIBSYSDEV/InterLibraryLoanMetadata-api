package no.unit.libcheck;

import com.amazonaws.services.lambda.runtime.Context;
import jakarta.xml.bind.JAXBException;
import java.util.Map;
import no.unit.MetadataResponse;
import no.unit.services.BaseBibliotekBean;
import no.unit.services.BaseBibliotekService;
import nva.commons.apigateway.ApiGatewayHandler;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.apigateway.exceptions.BadRequestException;
import nva.commons.core.Environment;
import nva.commons.core.JacocoGenerated;

import java.net.HttpURLConnection;
import nva.commons.core.StringUtils;

import static org.apache.commons.lang3.StringUtils.isEmpty;


public class LibcheckHandler extends ApiGatewayHandler<Void, LibcheckResponse> {

    public static final String HEALTHCHECK_KEY = "healthcheck";
    public static final String LIBUSER_KEY = "libuser";
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
    protected LibcheckResponse processInput(Void input, RequestInfo requestInfo, Context context)
            throws ApiGatewayException {

        LibcheckResponse libcheckResponse = new LibcheckResponse();

        Map<String, String> parameters = requestInfo.getQueryParameters();
        if (parameters.containsKey(HEALTHCHECK_KEY)) {
            return libcheckResponse;
        }
        String libuser = requestInfo.getQueryParameter(LIBUSER_KEY);

        BaseBibliotekBean libraryData = getLibraryData(libuser);

        libcheckResponse.setAlmaLibrary(ALMA_KATSYST.equalsIgnoreCase(libraryData.getKatsyst()));
        libcheckResponse.setNcipLibrary(!isEmpty(libraryData.getNncippServer()));

        return libcheckResponse;
    }

    @Override
    protected Integer getSuccessStatusCode(Void input, LibcheckResponse output) {
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
