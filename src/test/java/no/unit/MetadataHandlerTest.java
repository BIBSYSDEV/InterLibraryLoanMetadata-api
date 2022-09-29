package no.unit;

import static no.unit.MetadataHandler.DOCUMENT_ID_KEY;
import static no.unit.MetadataHandler.FINAL_LOGGER_DEBUG_MESSAGE;
import static no.unit.MetadataHandler.NO_PARAMETERS_GIVEN_TO_HANDLER;
import static nva.commons.apigateway.ApiGatewayHandler.ALLOWED_ORIGIN_ENV;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import no.unit.services.BaseBibliotekBean;
import no.unit.services.BaseBibliotekService;
import no.unit.services.PnxServices;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.apigateway.exceptions.BadRequestException;
import nva.commons.core.Environment;
import nva.commons.logutils.LogUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetadataHandlerTest {

    public static final String MISSING_FROM_QUERY_PARAMETERS = "Missing from query parameters: ";
    public static final String CONDENSED_PNX_EXAMPLE_4 = "condensed_pnx_example_4.json";
    private static final String MOCK_DOCUMENT_ID = "BIBSYS_ILS71560264980002201";
    private static final String MOCK_INSTITUTION_CODE = "AHUS";
    private Environment environment;
    private PnxServices pnxServices;
    private BaseBibliotekService baseBibliotekService;
    private MetadataHandler handler;
    private Context awsContext;

    private JsonObject createJson(String filename) {

        StringBuilder contentBuilder = new StringBuilder();
        int i;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (stream != null) {
                while ((i = stream.read()) != -1) {
                    contentBuilder.append((char) i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonParser.parseString(contentBuilder.toString()).getAsJsonObject();
    }

    /**
     * javadoc for checkstyle.
     */
    @BeforeEach
    public void init() {
        environment = mock(Environment.class);
        pnxServices = mock(PnxServices.class);
        baseBibliotekService = mock(BaseBibliotekService.class);
        awsContext = mock(Context.class);
        when(environment.readEnv(ALLOWED_ORIGIN_ENV)).thenReturn("*");
        handler = new MetadataHandler(environment, pnxServices, baseBibliotekService);
    }

    @Test
    public void testingDefaultConstructor() throws JAXBException {
        //for test coverage
        MetadataHandler handler = new MetadataHandler();
    }

    @Test
    public void handlerThrowsExceptionWithEmptyRequest() {
        Exception exception = assertThrows(BadRequestException.class, () -> {
            handler.processInput(null, null, awsContext);
        });
        assertTrue(exception.getMessage().contains(NO_PARAMETERS_GIVEN_TO_HANDLER));
    }

    @Test
    public void handlerThrowsExceptionWithMissingQueryParam() {
        Exception exception = assertThrows(BadRequestException.class, () -> {
            handler.processInput(null, new RequestInfo(), awsContext);
        });
        assertTrue(exception.getMessage().contains(MISSING_FROM_QUERY_PARAMETERS + DOCUMENT_ID_KEY));
    }

    @Test
    void getSuccessStatusCodeReturnsOk() {
        Integer statusCode = handler.getSuccessStatusCode(null, new MetadataResponse());
        assertEquals(HttpURLConnection.HTTP_OK, statusCode);
    }

    @Test
    public void handlerSuccess() throws ApiGatewayException {
        var appender = LogUtils.getTestingAppenderForRootLogger();

        BaseBibliotekBean basebibliotekBean = new BaseBibliotekBean();
        basebibliotekBean.setBibNr("1023001");
        when(baseBibliotekService.libraryLookupByBibnr(anyString())).thenReturn(basebibliotekBean);

        JsonObject jsonObject = createJson(CONDENSED_PNX_EXAMPLE_4);
        when(pnxServices.getPnxData(anyString())).thenReturn(jsonObject);

        RequestInfo requestInfo = new RequestInfo();
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put(DOCUMENT_ID_KEY, MOCK_DOCUMENT_ID);
        requestInfo.setQueryParameters(queryParameters);

        var actual = handler.processInput(null, requestInfo, awsContext);
        assertEquals(MOCK_INSTITUTION_CODE, actual.libraries.get(0).institution_code);
        assertEquals(MOCK_DOCUMENT_ID, actual.record_id);
        assertFalse(actual.creator.contains(MetadataHandler.DOLLAR_Q_PREFIX));
        assertTrue(appender.getMessages().contains(FINAL_LOGGER_DEBUG_MESSAGE));
    }

    @Test
    public void testMmsIdMap() {
        JsonObject jsonObject = createJson(CONDENSED_PNX_EXAMPLE_4);
        final Map<String, String> mmsidMap = handler.getMmsidMap(jsonObject);
        assertNotNull(mmsidMap.get("NTNU_UB"));
    }

}
