package no.unit;

import nva.commons.core.StringUtils;

public class Config {

    public static final String MISSING_ENVIRONMENT_VARIABLES = "Missing environment variables";
    public static final String PRIMO_SERVICE_ENDPOINT_KEY = "PRIMO_SERVICE_ENDPOINT";
    public static final String PRIMO_SERVICE_API_KEY = "PRIMO_SERVICE_API";
    public static final String BASEBIBLIOTEKSERVICE_ENDPOINT_KEY = "BASEBIBLIOTEKSERVICE_ENDPOINT";
    public static final String STAGE_KEY = "STAGE";

    private String basebibliotekServiceHost;

    private String primoRestApiHost;
    private String primoRestApiKey;

    private String stage;

    private Config() {
    }

    private static class LazyHolder {

        private static final Config INSTANCE = new Config();

        static {
            INSTANCE.setBasebibliotekServiceHost(System.getenv(BASEBIBLIOTEKSERVICE_ENDPOINT_KEY));
            INSTANCE.setPrimoRestApiHost(System.getenv(PRIMO_SERVICE_ENDPOINT_KEY));
            INSTANCE.setPrimoRestApiKey(System.getenv(PRIMO_SERVICE_API_KEY));
            INSTANCE.setStage(System.getenv(STAGE_KEY));
        }
    }

    public static Config getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Checking if configs is present.
     *
     * @return <code>TRUE</code> if property is present.
     */
    public boolean checkProperties() {
        if (StringUtils.isEmpty(basebibliotekServiceHost) ||
                StringUtils.isEmpty(primoRestApiHost) ||
                StringUtils.isEmpty(primoRestApiKey) ||
                StringUtils.isEmpty(stage)
        ) {
            throw new IllegalStateException(MISSING_ENVIRONMENT_VARIABLES);
        }
        return true;
    }

    public String getBasebibliotekServiceHost() {
        return basebibliotekServiceHost;
    }

    protected void setBasebibliotekServiceHost(String basebibliotekServiceHost) {
        this.basebibliotekServiceHost = basebibliotekServiceHost;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
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

}
