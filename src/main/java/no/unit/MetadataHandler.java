package no.unit;

import static java.util.Objects.isNull;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.xml.bind.JAXBException;

import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import no.unit.MetadataResponse.Library;
import no.unit.services.BaseBibliotekBean;
import no.unit.services.BaseBibliotekService;
import no.unit.services.PnxServices;
import nva.commons.apigateway.ApiGatewayHandler;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.ApiGatewayException;
import nva.commons.apigateway.exceptions.BadRequestException;
import nva.commons.core.Environment;
import nva.commons.core.JacocoGenerated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataHandler extends ApiGatewayHandler<Void, MetadataResponse> {

    public static final ZoneId NORWAY_ZONE_ID = ZoneId.of(ZoneOffset.of("+01:00").getId());
    @JacocoGenerated
    private static final transient Logger log = LoggerFactory.getLogger(MetadataHandler.class);
    public static final String NO_PARAMETERS_GIVEN_TO_HANDLER = "No parameters given to Handler";
    public static final String DOCUMENT_ID_KEY = "document_id";
    public static final int LENGTH_OF_LIBRARYCODE = 7;
    public static final String EMPTY_STRING = "";
    public static final String UNDERSCORE = "_";
    public static final String PREFIX_47BIBSYS = "47BIBSYS";
    public static final String COULD_NOT_READ_LIBRARY_CODE = "Could not read libraryCode from: {}";
    public static final String SKIP_LIBRARY_BECAUSE_OF_FAULTY_RESPONSE = "Skip library {} because of faulty response.";
    public static final String RESPONSE_OBJECT = "ResponseObject: ";
    public static final String COMMA_DELIMITER = ", ";
    public static final String NCIP_TEST_SERVER_URL = "https://ncip.server.url";
    private final transient PnxServices pnxServices;
    private final transient BaseBibliotekService baseBibliotekService;
    private final transient Gson gson = new Gson();


    @JacocoGenerated
    public MetadataHandler() throws JAXBException {
        this(new Environment(), new PnxServices(), new BaseBibliotekService());
    }

    public MetadataHandler(Environment environment, PnxServices pnxServices, BaseBibliotekService baseBibliotekService) {
        super(Void.class, environment);
        this.pnxServices = pnxServices;
        this.baseBibliotekService = baseBibliotekService;
    }

    @Override
    protected MetadataResponse processInput(Void input, RequestInfo requestInfo, Context context)
            throws ApiGatewayException {
        if (isNull(requestInfo)) {
            throw new BadRequestException(NO_PARAMETERS_GIVEN_TO_HANDLER);
        }
        Date start = new Date();
        log.info("Start: "+ start );
        final String documentId = requestInfo.getQueryParameter(DOCUMENT_ID_KEY);
        JsonObject pnxServiceObject = getPnxServiceData(documentId);
        log.info("Reading Pnx done: " + new Date());
        final MetadataResponse response = generateMetadatResponse(pnxServiceObject);
        log.info("Response down: " + new Date());
        return response;
    }

    private MetadataResponse generateMetadatResponse(JsonObject pnxServiceObject) {
        MetadataResponse response = new MetadataResponse();
        response.record_id = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTED_RECORD_ID_KEY);
        response.source = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTED_SOURCE_KEY);
        response.isbn = getArrayAsString(pnxServiceObject, PnxServices.ISBN);
        response.publication_place = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTION_PUBLICATION_PLACE_KEY);
        response.b_title = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTED_B_TITLE_KEY);
        response.volume = getArrayAsString(pnxServiceObject, PnxServices.VOLUME);
        response.pages = getArrayAsString(pnxServiceObject, PnxServices.PAGES);
        response.creation_year = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTED_CREATION_YEAR_KEY);
        response.creator = getArrayAsString(pnxServiceObject, PnxServices.CREATOR);
        response.display_title = getArrayAsString(pnxServiceObject, PnxServices.EXTRACTED_DISPLAY_TITLE_KEY);
        response.publisher = getArrayAsString(pnxServiceObject, PnxServices.PUBLISHER);
        log.info("Parsing PNX done: " + new Date());
        response.libraries.addAll(getLibraries(pnxServiceObject));
        log.info("Parsing libraries to response done: " + new Date());
        log.debug(RESPONSE_OBJECT + gson.toJson(response));
        return response;
    }

    private Collection<Library> getLibraries(JsonObject pnxServiceObject) {

        List<CompletableFuture<Library>> futures = new ArrayList<>();

        Map<String, String> mmsidMap = getMmsidMap(pnxServiceObject);
        final JsonArray libArray = pnxServiceObject.getAsJsonArray(PnxServices.EXTRACTED_LIBRARIES_KEY);
        log.info("Start iterating PNX libraries: " + new Date());
        for (JsonElement jsonElement : libArray) {
            final String input = jsonElement.getAsString();
            if (input.length() > LENGTH_OF_LIBRARYCODE) {
                String libraryCode = input.substring(input.length() - LENGTH_OF_LIBRARYCODE);
                String institutionCode = input.replace(libraryCode, EMPTY_STRING);
                String mmsId = mmsidMap.get(institutionCode);
                CompletableFuture<Library> future = CompletableFuture.supplyAsync(() -> generateLibrary(mmsId, libraryCode, institutionCode));
                futures.add(future);
            } else {
                log.error(COULD_NOT_READ_LIBRARY_CODE, input);
            }
        }


        CompletableFuture<Void> combinedFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        CompletableFuture<List<Library>> allCompletableFuture = combinedFutures.thenApply(future -> {
            return futures.stream().map(completableFuture -> completableFuture.join())
                    .collect(Collectors.toList());

        });


        CompletableFuture<List<Library>> completableFuture = allCompletableFuture.toCompletableFuture();

        List<Library> finalList = new ArrayList<>();
        try {
            finalList = completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }

        log.info("End iterating PNX libraries: " + new Date());
        return finalList;

    }

    private Library generateLibrary(String mmsId, String libraryCode,
                                    String institutionCode) {
        log.info("Start getting from BaseBibliotek: " + new Date());
        Library library = createLibrary(libraryCode);
        log.info("End getting from BaseBibliotek: " + new Date());
        library.library_code = libraryCode;
        library.institution_code = institutionCode;
        library.mms_id = mmsId;

        return library;
    }


    private Library createLibrary(String libraryCode) {
        log.info("CreateLibrary: " + libraryCode);
        final BaseBibliotekBean baseBibliotekBean = baseBibliotekService.libraryLookupByBibnr(libraryCode);
        final Library library = new Library();
        if (baseBibliotekBean != null) {
            library.display_name = baseBibliotekBean.getInst();
            library.available_for_loan = baseBibliotekBean.isOpenAtDate(LocalDate.now(NORWAY_ZONE_ID));
            if("dev".equalsIgnoreCase(Config.getInstance().getStage())) {
                library.ncip_server_url = NCIP_TEST_SERVER_URL;
            } else {
                library.ncip_server_url = baseBibliotekBean.getNncippServer();
            }
        }
        return library;
    }

    protected Map<String, String> getMmsidMap(JsonObject pnxServiceObject) {
        Map<String, String> mmsidMap = new ConcurrentHashMap<>();
        final JsonArray mmsidArray = pnxServiceObject.getAsJsonArray(PnxServices.EXTRACTED_MMS_ID_KEY);
        for (JsonElement jsonElement : mmsidArray) {
            final String input = jsonElement.getAsString();
            final String[] split = input.split(UNDERSCORE);
            final String mmsId = split[split.length - 1];
            String inst = input.replace(UNDERSCORE + mmsId, EMPTY_STRING);
            inst = inst.replace(PREFIX_47BIBSYS + UNDERSCORE, EMPTY_STRING);
            mmsidMap.put(inst, mmsId);
        }
        return mmsidMap;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private String getArrayAsString(JsonObject pnxServiceObject, String key) {
        final JsonElement jsonArray = pnxServiceObject.get(key);
        List jsonObjList = gson.fromJson(jsonArray, List.class);
        return isNull(jsonObjList) ? EMPTY_STRING : String.join(COMMA_DELIMITER, jsonObjList);
    }

    protected JsonObject getPnxServiceData(String documentId) {
        return pnxServices.getPnxData(documentId);
    }

    @Override
    protected Integer getSuccessStatusCode(Void input, MetadataResponse output) {
        return HttpURLConnection.HTTP_OK;
    }
}
