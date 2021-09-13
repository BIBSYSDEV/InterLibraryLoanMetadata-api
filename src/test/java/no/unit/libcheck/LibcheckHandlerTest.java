package no.unit.libcheck;

import com.amazonaws.services.lambda.runtime.Context;
import no.unit.ill.services.BaseBibliotekService;
import nva.commons.core.Environment;
import org.junit.jupiter.api.BeforeEach;

import javax.xml.bind.JAXBException;

import static nva.commons.apigateway.ApiGatewayHandler.ALLOWED_ORIGIN_ENV;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LibcheckHandlerTest {

    private Environment environment;
    private LibcheckHandler handler;
    private BaseBibliotekService baseBibliotekService;
    private Context context;
    public static final String MOCK_RESPONSE_BODY = "{\"isAlmaLibrary\":\"true\",\"isNcipLibrary\":\"true\"}";
    public static final String MOCK_LIBUSER = "1234";
    public static final String LIBUSER = "libuser";

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



}
