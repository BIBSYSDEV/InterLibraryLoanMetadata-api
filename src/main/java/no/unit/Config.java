package no.unit;

import no.unit.utils.StringUtils;

public class Config {

    public static final String MISSING_ENVIRONMENT_VARIABLES = "Missing environment variables";
    public static final String CORS_ALLOW_ORIGIN_HEADER_ENVIRONMENT_NAME = "ALLOWED_ORIGIN";
    public static final String INSTITUTIONSERVICE_ENDPOINT_KEY = "INSTITUTIONSERVICE_ENDPOINT";
    public static final String BASEBIBLIOTEKSERVICE_ENDPOINT_KEY = "BASEBIBLIOTEKSERVICE_ENDPOINT";

    private String corsHeader;

    private String institutionServiceHost;
    private String baseBibliotekServiceHost;

    private Config() {
    }

    private static class LazyHolder {

        private static final Config INSTANCE = new Config();

        static {
            INSTANCE.setInstitutionServiceHost(System.getenv(INSTITUTIONSERVICE_ENDPOINT_KEY));
            INSTANCE.setBasebibliotekServiceHost(System.getenv(INSTITUTIONSERVICE_ENDPOINT_KEY));
            INSTANCE.setCorsHeader(System.getenv(CORS_ALLOW_ORIGIN_HEADER_ENVIRONMENT_NAME));
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
        if (StringUtils.isEmpty(institutionServiceHost) ||
                StringUtils.isEmpty(baseBibliotekServiceHost)) {
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

    public String getBasebibliotekServiceHost() {
        return baseBibliotekServiceHost;
    }

    protected void setBasebibliotekServiceHost(String institutionServiceHost) {
        this.institutionServiceHost = institutionServiceHost;
    }

    public String getCorsHeader() {
        return corsHeader;
    }

    public void setCorsHeader(String corsHeader) {
        this.corsHeader = corsHeader;
    }

}
