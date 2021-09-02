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

    private final InstitutionServiceConnection connection;

    public InstitutionService() {
        this.connection = new InstitutionServiceConnection();
    }

    public InstitutionService(InstitutionServiceConnection connection) {
        this.connection = connection;
    }

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
            log.error("Wrong Url for get in institutionService for {}/{}", context, identifier, e);
        } catch (IOException e) {
            log.error("error while getting at institutionService for {}/{}", context, identifier, e);
        } catch (NullPointerException e) {
            log.error("did not get nice data from institutionService for {}/{}", context, identifier, e);
        }
        return resultStr;
    }
}
