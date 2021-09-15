package no.unit.ill.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstitutionService {

    private static final transient Logger log = LoggerFactory.getLogger(InstitutionService.class);
    public static final String ATTRIBUTES = "attributes";
    public static final String WRONG_URL_FOR_GET_IN_INSTITUTION_SERVICE_FOR = "Wrong Url for get in "
        + "institutionService for {}/{}";
    public static final String ERROR_WHILE_GETTING_AT_INSTITUTION_SERVICE_FOR = "error while getting at "
        + "institutionService for {}/{}";
    public static final String ORIA_CODE = "oriaCode";
    public static final String ORIA_DEFAULT_NCI_PSERVER = "oriaDefaultNCIPserver";

    private final transient InstitutionServiceConnection connection;

    public InstitutionService() {
        this.connection = new InstitutionServiceConnection();
    }

    public InstitutionService(InstitutionServiceConnection connection) {
        this.connection = connection;
    }

    /**
     * Get targetValue (e.g. oriaDefaultNCIPserver) from InstitutionService by given identifier (e.g. NTNU_UB) and
     * context (e.g. oriaCode).
     *
     * @param context context (e.g. oriaCode)
     * @param identifier identifier (e.g. NTNU_UB)
     * @param targetValue targetValue (e.g. oriaDefaultNCIPserver)
     * @return result value (e.g. libraryCode of the NCIPserver)
     */
    public String get(String context, String identifier, String targetValue) {
        String resultStr = "";
        try (InputStreamReader streamReader = connection.connect(context, identifier)) {
            String json = new BufferedReader(streamReader)
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonObject(ATTRIBUTES).getAsJsonArray(targetValue);
            resultStr = jsonArray.get(0).getAsString();
        } catch (URISyntaxException e) {
            log.error(WRONG_URL_FOR_GET_IN_INSTITUTION_SERVICE_FOR, context, identifier, e);
        } catch (IOException e) {
            log.error(ERROR_WHILE_GETTING_AT_INSTITUTION_SERVICE_FOR, context, identifier, e);
        }
        return resultStr;
    }

    public String getInstituitionDefaultLibraryCode(String institutionCode) {
        return this.get(ORIA_CODE, institutionCode, ORIA_DEFAULT_NCI_PSERVER);
    }
}
