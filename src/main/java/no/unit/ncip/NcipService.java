package no.unit.ncip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class NcipService {

    private CloseableHttpClient httpclient;

    public NcipService() {
        httpclient = HttpClients.createDefault();
    }

    public NcipService(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    protected NcipResponse send(String payload, String ncipServerUrl) {
        httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(ncipServerUrl);

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("param-1", "12345"));
        params.add(new BasicNameValuePair("param-2", "Hello!"));
        try {
            httppost.setEntity(new StringEntity(payload));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NcipResponse ncipResponse = new NcipResponse();
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ncipResponse.status = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String responseBody = "";
        try {
            responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ncipResponse.message = responseBody;
        return ncipResponse;
    }
}
