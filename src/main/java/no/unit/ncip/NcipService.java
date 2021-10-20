package no.unit.ncip;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import nva.commons.core.JacocoGenerated;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NcipService {

    @JacocoGenerated
    private static final transient Logger log = LoggerFactory.getLogger(NcipService.class);
    public static final String FAILED_TO_READ_RESPONSE_BODY_FROM_NCIP_POST = "Failed to read response body from "
        + "ncip POST";
    public static final String FAILED_TO_SET_PAYLOAD_TO_HTTP_POST = "Failed to set payload to HttpPost.";
    public static final String ORDER_SUCCEEDED = "Order succeeded";
    public static final String NS1_PROBLEM_TYPE = "ns1:ProblemType";
    public static final String NS1_PROBLEM_DETAIL = "ns1:ProblemDetail";
    public static final String NCIP_REQUEST_ITEM_FAILED = "NCIP request item failed : ";
    public static final String NCIP_REQUEST_OK = "NCIP request ok : ";
    public static final String SOMETHING_WENT_WRONG_DURING_PARSING_OF_XML_STRUCTURE = "Something went wrong "
        + "during parsing of xmlStructure: ";
    public static final String ERROR_PARSING_ITEM_REQUEST_RESPONSE = "error parsing ItemRequestResponse : ";
    private final transient CloseableHttpClient httpclient;

    public NcipService() {
        httpclient = HttpClients.createDefault();
    }

    public NcipService(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    protected NcipResponse send(String payload, String ncipServerUrl) {
        HttpPost httppost = new HttpPost(ncipServerUrl);
        try {
            httppost.setHeader(CONTENT_TYPE, APPLICATION_XML);
            httppost.setHeader(ACCEPT, APPLICATION_XML);
            httppost.setEntity(new StringEntity(payload));
        } catch (UnsupportedEncodingException e) {
            log.error(FAILED_TO_SET_PAYLOAD_TO_HTTP_POST, e);
        }

        NcipResponse ncipResponse = new NcipResponse();
        try (CloseableHttpResponse response = httpclient.execute(httppost)) {
            try (InputStream inputStream = response.getEntity().getContent()) {
                ncipResponse = this.extractNcipResponse(inputStream);
            }
        } catch (IOException e) {
            ncipResponse.status = HttpURLConnection.HTTP_INTERNAL_ERROR;
            ncipResponse.message = FAILED_TO_READ_RESPONSE_BODY_FROM_NCIP_POST;
            ncipResponse.problemdetail = e.getMessage();
            log.error(FAILED_TO_READ_RESPONSE_BODY_FROM_NCIP_POST, e);
        }
        return ncipResponse;
    }

    protected NcipResponse extractNcipResponse(InputStream inputStream) {
        NcipResponse response = new NcipResponse();
        try {
            Document doc = this.parseXML(inputStream);
            NodeList problemNodeList = doc.getElementsByTagName(NS1_PROBLEM_TYPE);
            if (problemNodeList.getLength() > 0) {
                response.message = problemNodeList.item(0).getTextContent();
                response.status = HttpURLConnection.HTTP_BAD_REQUEST;
                log.warn(NCIP_REQUEST_ITEM_FAILED + response.message);
                NodeList problemDetailNodeList = doc.getElementsByTagName(NS1_PROBLEM_DETAIL);
                if (problemDetailNodeList.getLength() > 0) {
                    response.problemdetail = problemDetailNodeList.item(0).getTextContent();
                }
            } else {
                response.message = ORDER_SUCCEEDED;
                response.status = HttpURLConnection.HTTP_OK;
                log.info(NCIP_REQUEST_OK + response.message);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error(SOMETHING_WENT_WRONG_DURING_PARSING_OF_XML_STRUCTURE + e.getMessage());
            response.message = ERROR_PARSING_ITEM_REQUEST_RESPONSE + inputStream;
            response.status = HttpURLConnection.HTTP_BAD_REQUEST;
        }
        return response;
    }

    private Document parseXML(InputStream xmlStruct) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(null);
        return builder.parse(xmlStruct);
    }
}
