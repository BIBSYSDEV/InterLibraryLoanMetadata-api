package no.unit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPConnectionWrapper {

    public static final int READ_TIMEOUT = 4_000;
    public static final int CONNECT_TIMEOUT = 8_000;

    //Must read connection to BaseBibliotek with byteArray because newlines in values (inst)
    public InputStream getResourceAsInputStreamUsingByteArray(String urlString) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString);
        try {
            return convertToByteArrayInputStream(connection.getInputStream());
        } catch (Exception e) {
            throw new IOException("Connection failed. Could not read from connection", e);

        }
    }

    private HttpURLConnection getHttpURLConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Connection Failed : HTTP error code : " + connection.getResponseCode());
        }
        return connection;
    }

    private InputStream convertToByteArrayInputStream(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return new ByteArrayInputStream(baos.toByteArray());
    }

}