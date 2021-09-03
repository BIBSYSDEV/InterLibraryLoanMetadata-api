package no.unit.xservice;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;

public class XServices {

    private static final String PRIMO_RECORD_PREFIX = "TN_";
   // private static final Namespace PRIMO_NM_BIB = Namespace.getNamespace("p", "http://www.exlibrisgroup.com/xsd/primo/primo_nm_bib");
    private final LambdaLogger logger;

    private HTTPConnectionWrapper connection = new HTTPConnectionWrapper();


    public XServices (Context awsContext) {
        logger = awsContext.getLogger();

    }

    public PNXBean callPrimoXservices(String docID) {
        PNXBean bean = new PNXBean();
        bean.setValid(true);
        docID = removePrimoRecordPrefix(docID);
        return getPnxBean(bean, generateUrl(docID));
    }

    private String generateUrl(String docID) {
        String erlingJiraPages = "https://bibsys-almaprimo.hosted.exlibrisgroup.com/primo-explore/fulldisplay?vid=NB&search_scope=default_scope&tab=default_tab&docid=BIBSYS_ILS71560264980002201&lang=no_NO&context=L&adaptor=Local%20Search%20Engine&isFrbr=true&showPnx=true&query=any,contains,999920556049902201&sortby=date&facet=frbrgroupid,include,207071061&offset=0";
        String gammelILLKode =  "https://bibsys-almaprimo.hosted.exlibrisgroup.com/PrimoWebServices/xservice"
                + "/search/brief"
                + "?institution=BIBSYS_ILS"
                + "&onCampus=true"
                + "&query=rid,exact,"
                + docID
                + "&indx=1"
                + "&bulkSize=2"
                + "&dym=true";
        String restAPIStuffs = "https://bibsys-almaprimo.hosted.exlibrisgroup.com/PrimoWebServices/v1/pnxs"
                + "/L"
                + "/" + docID
                + "?inst=BIBSYS_ILS"
                + "&lang=no_NO";
        String publicKey = "https://bibsys-almaprimo.hosted.exlibrisgroup.com/primo_library/libweb/webservices/rest/primo-explore/v1/insetPublicKey";
        String intercept = "https://bibsys-almaprimo.hosted.exlibrisgroup.com/primo_library/libweb/webservices/rest/primo-explore/v1/pnxs/L/BIBSYS_ILS71560264980002201?vid=NB&lang=no_NO&search_scope=default_scope&adaptor=Local%20Search%20Engine&isFrbr=true&showPnx=true";
        String search = "https://bibsys-almaprimo.hosted.exlibrisgroup.com/primo_library/libweb/primo/v1/search?vid=NB&tab=default_tab&scope=default_scope&q=any,contains,999920556049902201";
        return gammelILLKode;
    }

    private String removePrimoRecordPrefix(String docID) {
        return docID.replaceFirst(PRIMO_RECORD_PREFIX, "");
    }

    public JSONObject doStuff(String document_id) {
        String docID = removePrimoRecordPrefix(document_id);
        String url = generateUrl(docID);
        System.out.println("URL:" + url);
        return getXmlJSONObject(url);
    }

    private JSONObject getXmlJSONObject (String url) {
        String response;
        try {
            response = connection.getResourceAsString(url);
        }catch (IOException e) {
            System.out.println("COULD NOT CONNECT TO EXIBRIS XSERVICE");
            System.out.println(e.getMessage());
            logger.log("COULD NOT CONNECT TO EXIBRIS XSERVICE");
            return null;
        }
        JSONObject extractedData;
        try {
            JSONObject xmlJSONObject = XML.toJSONObject(response);
            System.out.println(xmlJSONObject);
            extractedData = extractUsefulDataFromXservice(xmlJSONObject);
            System.out.println("XML" + response);
        } catch (JSONException e) {
            logger.log("Could not parse data from Exibris Xservice at " + url + e.getMessage());
            return null;
        }
        return  extractedData;
    }

    private JSONObject extractUsefulDataFromXservice(JSONObject response) throws JSONException {
        JSONObject record = response.getJSONObject("sear:SEGMENTS").getJSONObject("sear:JAGROOT").getJSONObject("sear:RESULT").getJSONObject("sear:DOCSET").getJSONObject("sear:DOC").getJSONObject("PrimoNMBib").getJSONObject("record");
        JSONObject addata = record.getJSONObject("addata");
        JSONObject display = record.getJSONObject("display");
        JSONObject facets = record.getJSONObject("facets");
        JSONObject search = record.getJSONObject("search");
        JSONObject control = record.getJSONObject("control");
        JSONObject extractedData = new JSONObject();

        if(search.has("sourceid")){
            extractedData.put("source", search.get("sourceid"));
        }else if (control.has("sourceid")){
            extractedData.put("source", control.get("sourceid"));
        }
        if(control.has("recordid")) {
            extractedData.put("record_id", control.get("recordid"));
        }
        if (addata.has("isbn")) {
            extractedData.put("isbn", search.get("isbn"));
        }
        if(addata.has("issn")){
            extractedData.put("issn",addata.get("issn"));
        }
        if(addata.has("cop")){
            extractedData.put("publicationPlace", addata.get("cop"));
        }
        if(addata.has("btitle")){
            extractedData.put("b_title", addata.get("btitle"));
        }
        if(addata.has("atitle")){
            extractedData.put("a_title", addata.get("atitle"));
        }
        if(addata.has("volume")){
            extractedData.put("volume", addata.get("volume"));
        }
        if(addata.has("lad11")){
            //if there's only one lad11 record, it has been parsed as a string by XML.toJSONObject, else it's an JSONArray
            var lad11 = addata.get("lad11");
            if (lad11 instanceof String ) {
                extractedData.put("mms_id", new JSONArray(lad11));
            }
            extractedData.put("mms_id", lad11);

        }else {
            extractedData.put("mms_id", new JSONArray());
        }
        if(display.has("creationdate")){
            extractedData.put("creation_year", display.get("creationdate"));
        }
        if(display.has("creator")) {
            extractedData.put("creator", display.get("creator"));
        }
        if(display.has("title")){
            extractedData.put("display_title", display.get("title"));
        }
        if(facets.has("library")){
            extractedData.put("libraries", facets.get("library"));
        }else {
            extractedData.put("libraries", new JSONArray());
        }
        return extractedData;
    }

    private PNXBean populatePnxBean(PNXBean bean, JSONObject response) {
        JSONObject search = response.getJSONObject("sear:SEGMENTS").getJSONObject("sear:JAGROOT").getJSONObject("sear:RESULT").getJSONObject("sear:DOCSET").getJSONObject("sear:DOC").getJSONObject("PrimoNMBib").getJSONObject("record").getJSONObject("search");
        bean.setIsbn(search.getString("isbn"));
        return bean;
    }

    private PNXBean getPnxBean(PNXBean bean, String url) {
        String response;

        try {
            response = connection.getResourceAsString(url);
        } catch (IOException e) {
            bean.setValid(false);
            return bean;
        }

        try {
           // bean = convertXMLresponseToPnxBean(response);
            JSONObject xmlJSONObject = XML.toJSONObject(response);
            JSONObject libraries = xmlJSONObject.getJSONObject("sear:SEGMENTS").getJSONObject("sear:JAGROOT").getJSONObject("sear:RESULT").getJSONObject("sear:DOCSET").getJSONObject("sear:DOC");
            String jsonPrettyPrintString = libraries.toString(4);
        } catch (JSONException e) {
            logger.log("Could not parse data from Exibris Xservice at " + url + e);
            bean.setValid(false);
        }
        return bean;
    }


    /*
    public PNXBean convertXMLresponseToPnxBean(String response) throws JDOMException, IOException {


        SAXBuilder builder = new SAXBuilder(false);
        org.jdom.Document xmlDoc = builder.build(new StringReader(response));

        XPath xpath = XPath.newInstance("//p:addata/*");
        xpath.addNamespace(PRIMO_NM_BIB);
        List<Element> nodes = xpath.selectNodes(xmlDoc);
        Iterator<Element> i = nodes.iterator();

        PNXBean bean = new PNXBean();
        String spage = "";
        String epage = "";

        while (i.hasNext()) {
            org.jdom.Element element = i.next();
            String key = element.getName();
            String value = element.getText();
            if (key.equalsIgnoreCase("spage") && value != null) {
                spage = value;
            } else if (key.equalsIgnoreCase("epage") && value != null) {
                epage = value;
            }
            switch (key) {
                case "atitle":
                    bean.setAtitle(value);
                    break;
                case "btitle":
                    bean.setTitle(value);
                    break;
                case "au":
                    bean.setCreator(value);
                    break;
                case "date":
                    bean.setYear(value);
                    break;
                case "jtitle":
                    bean.setJtitle(value);
                    break;
                case "issn":
                    bean.setIssn(value);
                    break;
                case "eissn":
                    bean.setEissn(value);
                    break;
                case "isbn":
                    bean.setIsbn(value);
                    break;
                case "cop":
                    bean.setPlaceOfPublication(value);
                    break;
                case "issue":
                    bean.setIssue(value);
                    break;
                case "volume":
                    bean.setVolume(value);
                    break;
            }
        }
        if (!spage.isEmpty() && !epage.isEmpty()) {
            bean.setPages(spage + " - " + epage);
        } else if (!spage.isEmpty()) {
            bean.setPages(spage);
        }

        String type = getSingleElementFromXMLNotNull(xmlDoc, "//p:display/p:type");
        String title = getSingleElementFromXMLNotNull(xmlDoc, "//p:display/p:title");
        if (type.equalsIgnoreCase("article")) {
            bean.setAtitle(title);
        }
        bean.setType(type);

        bean.setResourceTypes(getXpathSubTagFromXmlDoc(xmlDoc, "rsrctype", XPath.newInstance("//p:search/*")));

        bean.setPublisher(getSingleElementFromXMLNotNull(xmlDoc, "//p:display/p:publisher"));
        bean.setPublisher(getSingleElementFromXMLNotNull(xmlDoc, "//p:display/p:publisher"));
        bean.setRecordid(getSingleElementFromXMLNotNull(xmlDoc, "//p:control/p:recordid"));

        String sourceid = getSingleElementFromXMLNotNull(xmlDoc, "//p:control/p:sourceid");
        if (!sourceid.isEmpty()) {
            bean.setSource(sourceid + " - oria.no");
        }

        bean.setLibraries(getXpathSubTagFromXmlDoc(xmlDoc, "library", XPath.newInstance("//p:facets/*")));
        bean.setAlmaids(getXpathSubTagFromXmlDoc(xmlDoc, "almaid", XPath.newInstance("//p:control/*")));
        bean.setAlmaMms_ids(getXpathSubTagFromXmlDoc(xmlDoc, "lad11", XPath.newInstance("//p:addata/*")));
        bean.setRTA_ids(getXpathSubTagFromXmlDoc(xmlDoc, "lad12", XPath.newInstance("//p:addata/*")));

        bean.setPhotoCopyAllowed(true);
        if (bean.getType().equalsIgnoreCase("audio") || bean.getType().equalsIgnoreCase("video")) {
            bean.setPhotoCopyAllowed(false);
        } else {
            xpath = XPath.newInstance("//p:facets/*");
            xpath.addNamespace(PRIMO_NM_BIB);
            List facetNodes = xpath.selectNodes(xmlDoc);
            i = facetNodes.iterator();
            while (i.hasNext()) {
                org.jdom.Element element = i.next();
                String key = element.getName();
                if (key.equalsIgnoreCase("rsrctype")) {
                    String value = element.getText();
                    if (value.equalsIgnoreCase("e-journals")) {
                        bean.setPhotoCopyAllowed(false);
                        break;
                    }
                }
            }
        }

        if (StringUtils.isEmpty(bean.getRecordid())) {
            // Xservices failed no correct response from server or service is not responding
            bean.setValid(false);
        }

        return bean;

    }

     */

    /*
    private ArrayList<String> getXpathSubTagFromXmlDoc(Document xmlDoc, String subtag, XPath xpath) throws JDOMException {
        ArrayList<String> idList = new ArrayList<>();
        xpath.addNamespace(PRIMO_NM_BIB);
        List<Element> nodes = xpath.selectNodes(xmlDoc);
        for (Element element : nodes) {
            String key = element.getName();
            String value = element.getText();
            if (key.equalsIgnoreCase(subtag) && value != null) {
                idList.add(value);
            }
        }
        return idList;
    }

     */

    /*
    private String getSingleElementFromXMLNotNull(Document xmlDoc, String path) throws JDOMException {
        XPath xpath = XPath.newInstance(path);
        xpath.addNamespace(XServices.PRIMO_NM_BIB);
        org.jdom.Element e = (org.jdom.Element) xpath.selectSingleNode(xmlDoc);
        if (e == null) {
            return "";
        } else {
            return e.getText();
        }
    }

     */


}


