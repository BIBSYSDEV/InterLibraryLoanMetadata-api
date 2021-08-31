package no.unit;

public class GatewayResponse {
    private int statusCode;
    private String responseBody;

    public GatewayResponse(int statusCode, String responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setStatusCode(int status) {
        this.statusCode = status;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
