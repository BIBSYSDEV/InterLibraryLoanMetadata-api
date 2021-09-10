package no.unit.ncip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import nva.commons.core.JacocoGenerated;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NcipService {

    @JacocoGenerated
    private static final transient Logger log = LoggerFactory.getLogger(NcipService.class);
    public static final String FAILED_TO_READ_RESPONSE_BODY_FROM_NCIP_POST = "Failed to read response body from ncip "
        + "POST";
    public static final String FAILED_TO_SET_PAYLOAD_TO_HTTP_POST = "Failed to set payload to HttpPost.";
    private final transient CloseableHttpClient httpclient;

    public NcipService() {
        httpclient = HttpClients.createDefault();
    }

    public NcipService(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    protected NcipResponse send(String payload, String ncipServerUrl) {
        HttpPost httppost = new HttpPost(ncipServerUrl);
        try {
            httppost.setEntity(new StringEntity(payload));
        } catch (UnsupportedEncodingException e) {
            log.error(FAILED_TO_SET_PAYLOAD_TO_HTTP_POST, e);
        }

        NcipResponse ncipResponse = new NcipResponse();
        try (CloseableHttpResponse response = httpclient.execute(httppost)) {
            ncipResponse.status = response.getStatusLine().getStatusCode();
            ncipResponse.message = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            ncipResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
            ncipResponse.message = FAILED_TO_READ_RESPONSE_BODY_FROM_NCIP_POST;
            ncipResponse.problemdetail = e.getMessage();
            log.error(FAILED_TO_READ_RESPONSE_BODY_FROM_NCIP_POST, e);
        }
        return ncipResponse;
    }
}
