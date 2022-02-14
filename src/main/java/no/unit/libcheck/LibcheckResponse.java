package no.unit.libcheck;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LibcheckResponse {

    @JsonProperty("isAlmaLibrary")
    private boolean almaLibrary;

    @JsonProperty("ncip_server_url")
    private String ncipServerUrl;

    public boolean isAlmaLibrary() {
        return almaLibrary;
    }

    public void setAlmaLibrary(boolean almaLibrary) {
        this.almaLibrary = almaLibrary;
    }

    public String getNcipServerUrl() {
        return ncipServerUrl;
    }

    public void setNcipServerUrl(String ncipServerUrl) {
        this.ncipServerUrl = ncipServerUrl;
    }
}
