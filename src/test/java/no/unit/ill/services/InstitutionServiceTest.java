package no.unit.ill.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class InstitutionServiceTest {

    public static final String INST_SERVICE_RESPONSE_NTNU_UB_JSON = "instServiceResponseNTNU_UB.json";
    public static final String CONTEXT = "oriaCode";
    public static final String IDENTIFIER = "NTNU_UB";
    public static final String TARGET_VALUE = "oriaDefaultNCIPserver";
    public static final String EXPECTED_LIBRARYCODE = "1160106";

    @Test
    public void testGet() throws IOException, URISyntaxException {
        InstitutionServiceConnection connection = mock(InstitutionServiceConnection.class);
        InputStreamReader inputStreamReader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(
            INST_SERVICE_RESPONSE_NTNU_UB_JSON));
        when(connection.connect(anyString(), anyString())).thenReturn(inputStreamReader);
        final InstitutionService institutionService = new InstitutionService(connection);
        final String result = institutionService.get(CONTEXT, IDENTIFIER, TARGET_VALUE);
        String expected = EXPECTED_LIBRARYCODE;
        assertEquals(expected, result);
    }

}