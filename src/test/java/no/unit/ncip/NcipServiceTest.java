package no.unit.ncip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;

class NcipServiceTest {

    public static final String PAYLOAD = "payload";
    public static final String NCIP_SERVER_URL = "ncipServerUrl";

    @Test
    public void testSendWithSuccess() throws IOException {
        CloseableHttpClient httpclient = mock(CloseableHttpClient.class);
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        NcipService ncipService = new NcipService(httpclient);
        when(response.getEntity()).thenReturn(entity);
        when(httpclient.execute(any(HttpPost.class))).thenReturn(response);
        final NcipResponse ncipResponse = ncipService.send(PAYLOAD, NCIP_SERVER_URL);
        assertEquals(HttpStatus.SC_OK, ncipResponse.status);
        assertNull(ncipResponse.message);
    }

}