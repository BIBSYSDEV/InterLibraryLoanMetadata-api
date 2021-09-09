package no.unit;

import no.unit.utils.StringUtils;

public class Config {

    public static final String MISSING_ENVIRONMENT_VARIABLES = "Missing environment variables";
    public static final String CORS_ALLOW_ORIGIN_HEADER_ENVIRONMENT_NAME = "ALLOWED_ORIGIN";
    public static final String INSTITUTIONSERVICE_ENDPOINT_KEY = "INSTITUTIONSERVICE_ENDPOINT";
    public static final String PRIMO_SERVICE_ENDPOINT_KEY = "PRIMO_SERVICE_ENDPOINT";
    public static final String PRIMO_SERVICE_API_KEY = "PRIMO_SERVICE_API";

    private String corsHeader;

    private String institutionServiceHost;

    private String primoRestApiHost;
    private String primoRestApiKey;

    private Config() {
    }

    private static class LazyHolder {

        private static final Config INSTANCE = new Config();

        static {
            INSTANCE.setInstitutionServiceHost(System.getenv(INSTITUTIONSERVICE_ENDPOINT_KEY));
            INSTANCE.setCorsHeader(System.getenv(CORS_ALLOW_ORIGIN_HEADER_ENVIRONMENT_NAME));
            INSTANCE.setPrimoRestApiHost(System.getenv(PRIMO_SERVICE_ENDPOINT_KEY));
            INSTANCE.setPrimoRestApiKey(System.getenv(PRIMO_SERVICE_API_KEY));
        }
    }

    public static Config getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Checking if authoritySruHost is present.
     *
     * @return <code>TRUE</code> if property is present.
     */
    public boolean checkProperties() {
        if (StringUtils.isEmpty(institutionServiceHost)) {
            throw new RuntimeException(MISSING_ENVIRONMENT_VARIABLES);
        }
        return true;
    }

    protected void setInstitutionServiceHost(String institutionServiceHost) {
        this.institutionServiceHost = institutionServiceHost;
    }

    public String getInstitutionServiceHost() {
        return institutionServiceHost;
    }

    public String getPrimoRestApiHost() {
        return primoRestApiHost;
    }

    public void setPrimoRestApiHost(String primoRestApiHost) {
        this.primoRestApiHost = primoRestApiHost;
    }

    public String getPrimoRestApiKey() {
        return primoRestApiKey;
    }

    public void setPrimoRestApiKey(String primoRestApiKey) {
        this.primoRestApiKey = primoRestApiKey;
    }

    public String getCorsHeader() {
        return corsHeader;
    }

    public void setCorsHeader(String corsHeader) {
        this.corsHeader = corsHeader;
    }

}