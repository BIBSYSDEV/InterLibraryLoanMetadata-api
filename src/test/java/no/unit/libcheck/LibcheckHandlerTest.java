package no.unit.libcheck;

import com.amazonaws.services.lambda.runtime.Context;
import jakarta.xml.bind.JAXBException;
import no.unit.services.BaseBibliotekBean;
import no.unit.services.BaseBibliotekService;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.apigateway.exceptions.BadRequestException;
import nva.commons.core.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static no.unit.libcheck.LibcheckHandler.ALMA_KATSYST;
import static no.unit.libcheck.LibcheckHandler.LIBRARY_NOT_FOUND;
import static no.unit.libcheck.LibcheckHandler.LIBUSER_KEY;
import static nva.commons.apigateway.ApiGatewayHandler.ALLOWED_ORIGIN_ENV;
import static nva.commons.apigateway.RequestInfo.MISSING_FROM_QUERY_PARAMETERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LibcheckHandlerTest {

    public static final String MOCK_NCIP_SERVER_URL = "https://ncip.server.url";
    public static final String MOCK_LIBUSER = "1234";

    private Environment environment;
    private LibcheckHandler handler;
    private BaseBibliotekService baseBibliotekService;
    private Context context;

    /**
     * javadoc for checkstyle.
     */
    @BeforeEach
    public void init() throws JAXBException {
        environment = mock(Environment.class);
        baseBibliotekService = mock(BaseBibliotekService.class);
        when(environment.readEnv(ALLOWED_ORIGIN_ENV)).thenReturn("*");
        handler = new LibcheckHandler(environment);
        this.context = mock(Context.class);
    }

    @Test
    void getSuccessStatusCodeReturnsOk() {
        LibcheckResponse libcheckResponse = new LibcheckResponse();
        libcheckResponse.setAlmaLibrary(true);
        libcheckResponse.setNcipLibrary(true);
        Integer statusCode = handler.getSuccessStatusCode(null, libcheckResponse);
        assertEquals(HttpURLConnection.HTTP_OK, statusCode);
    }

    @Test
    void handleLibcheckWithSuccess() throws ApiGatewayException {
        BaseBibliotekBean basebibliotekBean = new BaseBibliotekBean();
        basebibliotekBean.setKatsyst(ALMA_KATSYST);
        basebibliotekBean.setNncippServer(MOCK_NCIP_SERVER_URL);
        when(baseBibliotekService.libraryLookupByBibnr(anyString())).thenReturn(basebibliotekBean);
        var handler = new LibcheckHandler(environment, baseBibliotekService);
        RequestInfo requestInfo = new RequestInfo();
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put(LIBUSER_KEY, MOCK_LIBUSER);
        requestInfo.setQueryParameters(queryParameters);
        var actual = handler.processInput(null, requestInfo, context);
        assertEquals(true, actual.isAlmaLibrary());
        assertEquals(true, actual.isNcipLibrary());
    }

    @Test
    void handlerThrowsExceptionWhenMissingQueryParam() {
        var handler = new LibcheckHandler(environment, baseBibliotekService);
        Exception exception = assertThrows(BadRequestException.class, () -> {
            handler.processInput(null, new RequestInfo(), context);
        });
        assertTrue(exception.getMessage().contains(MISSING_FROM_QUERY_PARAMETERS + LIBUSER_KEY));
    }

    @Test
    void handlerThrowsExceptionWhenLibuserNull() {
        when(baseBibliotekService.libraryLookupByBibnr(anyString())).thenReturn(null);
        var handler = new LibcheckHandler(environment, baseBibliotekService);
        RequestInfo requestInfo = new RequestInfo();
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put(LIBUSER_KEY, MOCK_LIBUSER);
        requestInfo.setQueryParameters(queryParameters);
        Exception exception = assertThrows(BadRequestException.class, () -> {
            handler.processInput(null, requestInfo, context);
        });
        assertTrue(exception.getMessage().contains(LIBRARY_NOT_FOUND + MOCK_LIBUSER));
    }

}
