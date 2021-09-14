package no.unit;

import static java.util.Objects.isNull;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.unit.ill.services.PnxServices;
import no.unit.utils.ParameterException;
import nva.commons.apigateway.ApiGatewayHandler;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.core.Environment;
import nva.commons.core.JacocoGenerated;
import nva.commons.core.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataHandler extends ApiGatewayHandler<Void, MetadataResponse> {

    @JacocoGenerated
    private static final transient Logger log = LoggerFactory.getLogger(MetadataHandler.class);
    public static final String NO_PARAMETERS_GIVEN_TO_HANDLER = "No parameters given to Handler";
    public static final String QUERY_STRING_PARAMETERS_KEY = "queryStringParameters";
    public static final String MANDATORY_PARAMETERS_MISSING = "Mandatory parameters 'document_id' is missing.";
    public static final String DOCUMENT_ID_KEY = "document_id";
    public static final int LENGTH_OF_LIBRARYCODE = 7;
    public static final String EMPTY_STRING = "";
    public static final String UNDERSCORE = "_";
    public static final String COULD_NOT_READ_LIBRARY_CODE = "Could not read libraryCode from: {}";
    private final transient PnxServices pnxServices;
    private final Gson gson = new Gson();

    @JacocoGenerated
    public MetadataHandler() {
        this(new Environment(), new PnxServices());
    }

    public MetadataHandler(Environment environment) {
        this(environment, new PnxServices());
    }

    public MetadataHandler(Environment environment, PnxServices pnxServices) {
        super(Void.class, environment);
        this.pnxServices = pnxServices;
    }

    @Override
    protected MetadataResponse processInput(Void input, RequestInfo requestInfo, Context context)
        throws ApiGatewayException {
        if (isNull(requestInfo)) {
            throw new ParameterException(NO_PARAMETERS_GIVEN_TO_HANDLER);
        }
        final String documentId = requestInfo.getQueryParameter(DOCUMENT_ID_KEY);
        if (StringUtils.isEmpty(documentId)) {
            throw new ParameterException(MANDATORY_PARAMETERS_MISSING);
        }
        JsonObject pnxServiceObject = getPnxServiceData(documentId);
        return generateMetadatResponse(pnxServiceObject);
    }

    private MetadataResponse generateMetadatResponse(JsonObject pnxServiceObject) {
        MetadataResponse response = new MetadataResponse();
        response.record_id = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTED_RECORD_ID_KEY);
        response.source = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTED_SOURCE_KEY);
        response.isbn = getArrayAsString(pnxServiceObject, PnxServices.ISBN);
        response.publicationPlace = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTION_PUBLICATION_PLACE_KEY);
        response.b_title = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTED_B_TITLE_KEY);
        response.volume = getArrayAsString(pnxServiceObject, PnxServices.VOLUME);
        response.pages = getArrayAsString(pnxServiceObject, PnxServices.PAGES);
        response.creation_year = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTED_CREATION_YEAR_KEY);
        response.creator = getArrayAsString(pnxServiceObject, PnxServices.CREATOR);
        response.display_title = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTED_DISPLAY_TITLE_KEY);
        response.publisher = getArrayAsString(pnxServiceObject, PnxServices.PUBLISHER);

        Map<String, String> mmsidMap = new HashMap<>();
        final JsonArray mmsidArray = pnxServiceObject.getAsJsonArray(PnxServices.EXTRACTED_MMS_ID_KEY);
        for (JsonElement jsonElement : mmsidArray) {
            final String input = jsonElement.getAsString();
            final String[] split = input.split(UNDERSCORE);
            mmsidMap.put(split[1], split[2]);
        }
        final JsonArray libArray = pnxServiceObject.getAsJsonArray(PnxServices.EXTRACTED_LIBRARIES_KEY);
        for (JsonElement jsonElement : libArray) {
            final String input = jsonElement.getAsString();
            if (input.length() > LENGTH_OF_LIBRARYCODE) {
                String libraryCode = input.substring(input.length() - LENGTH_OF_LIBRARYCODE);
                String institutionCode = input.replace(libraryCode, EMPTY_STRING);
                MetadataResponse.Library library = response.new Library();
                library.library_code = libraryCode;
                library.institution_code = institutionCode;
                library.mms_id = mmsidMap.get(institutionCode);
                response.libraries.add(library);
            } else {
                log.error(COULD_NOT_READ_LIBRARY_CODE, input);
            }
        }
        return response;
    }

    private String getArrayAsString(JsonObject pnxServiceObject, String key) {
        final JsonArray jsonArray = pnxServiceObject.getAsJsonArray(key);
        List jsonObjList = gson.fromJson(jsonArray, ArrayList.class);
        final String joinedStr = String.join(", ", jsonObjList);
        return joinedStr;
    }

    protected JsonObject getPnxServiceData(String documentId) {
        return pnxServices.getPnxData(documentId);
    }

    @Override
    protected Integer getSuccessStatusCode(Void input, MetadataResponse output) {
        return HttpStatus.SC_OK;
    }
}
