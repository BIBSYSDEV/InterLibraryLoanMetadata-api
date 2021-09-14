package no.unit.ncip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;

class NcipServiceTest {

    public static final String NCIP_RESPONSE_FAILURE = "ncipResponseFailure.xml";
    public static final String PAYLOAD = "payload";
    public static final String NCIP_SERVER_URL = "ncipServerUrl";
    public static final String FAILURE = "failure";
    public static final String NOT_RENEWABLE = "Not Renewable";

    @Test
    public void testSendWithFailure() throws IOException {
        CloseableHttpClient httpclient = mock(CloseableHttpClient.class);
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_BAD_REQUEST);
        NcipService ncipService = new NcipService(httpclient);
        when(response.getEntity()).thenReturn(entity);
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(NCIP_RESPONSE_FAILURE);
        when(entity.getContent()).thenReturn(inputStream);
        when(httpclient.execute(any(HttpPost.class))).thenReturn(response);
        final NcipResponse ncipResponse = ncipService.send(PAYLOAD, NCIP_SERVER_URL);
        assertEquals(HttpStatus.SC_BAD_REQUEST, ncipResponse.status);
        assertEquals(NOT_RENEWABLE, ncipResponse.message);
    }

    @Test
    public void testSendWithExceptionExpected() throws IOException {
        CloseableHttpClient httpclient = mock(CloseableHttpClient.class);
        NcipService ncipService = new NcipService(httpclient);
        when(httpclient.execute(any(HttpPost.class))).thenThrow(new IOException(FAILURE));
        final NcipResponse ncipResponse = ncipService.send("", NCIP_SERVER_URL);
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, ncipResponse.status);
        assertEquals(NcipService.FAILED_TO_READ_RESPONSE_BODY_FROM_NCIP_POST, ncipResponse.message);
        assertEquals(FAILURE, ncipResponse.problemdetail);
    }

}