package no.unit.ill.services;

import static no.unit.ill.services.InstitutionService.ERROR_WHILE_GETTING_AT_INSTITUTION_SERVICE_FOR;
import static no.unit.ill.services.InstitutionService.WRONG_URL_FOR_GET_IN_INSTITUTION_SERVICE_FOR;
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
        InstitutionService institutionService = new InstitutionService(); //a bit cheating for codeCoverage
        InstitutionServiceConnection connection = mock(InstitutionServiceConnection.class);
        InputStreamReader inputStreamReader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(
            INST_SERVICE_RESPONSE_NTNU_UB_JSON));
        when(connection.connect(anyString(), anyString())).thenReturn(inputStreamReader);
        institutionService = new InstitutionService(connection);
        final String result = institutionService.get(CONTEXT, IDENTIFIER, TARGET_VALUE);
        String expected = EXPECTED_LIBRARYCODE;
        assertEquals(expected, result);
    }

    @Test
    public void testGetWithUriSyntaxException() throws IOException, URISyntaxException {
        InstitutionServiceConnection connection = mock(InstitutionServiceConnection.class);
        when(connection.connect(anyString(), anyString())).thenThrow(
            new URISyntaxException(CONTEXT, WRONG_URL_FOR_GET_IN_INSTITUTION_SERVICE_FOR));
        final InstitutionService institutionService = new InstitutionService(connection);
        final String result = institutionService.get(CONTEXT, IDENTIFIER, TARGET_VALUE);
        assertEquals("", result);
    }

    @Test
    public void testGetWithIOException() throws IOException, URISyntaxException {
        InstitutionServiceConnection connection = mock(InstitutionServiceConnection.class);
        when(connection.connect(anyString(), anyString())).thenThrow(
            new IOException(ERROR_WHILE_GETTING_AT_INSTITUTION_SERVICE_FOR));
        final InstitutionService institutionService = new InstitutionService(connection);
        final String result = institutionService.get(CONTEXT, IDENTIFIER, TARGET_VALUE);
        assertEquals("", result);
    }

}