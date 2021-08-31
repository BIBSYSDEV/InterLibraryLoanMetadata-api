package no.unit;

import com.sun.syndication.io.SAXBuilder;
import nva.commons.utils.Environment;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class BaseBibliotekClient {


    private static final String BASEBIBLIOTEK_BIBNR_URL = "BASEBIBLIOTEK_BIBNR_URL";

    private static String basebibliotekBibnrUrl;

    private HTTPConnectionWrapper httpConnectionWrapper;

    public BaseBibliotekClient(Environment environment) {
        this.basebibliotekBibnrUrl = environment.readEnv(BASEBIBLIOTEK_BIBNR_URL);
        this.httpConnectionWrapper = new HTTPConnectionWrapper();
    }

    public BaseBibliotekClient(String basebibliotekBibnrUrl, HTTPConnectionWrapper httpConnectionWrapper) {
        this.basebibliotekBibnrUrl = basebibliotekBibnrUrl;
        this.httpConnectionWrapper = httpConnectionWrapper;
    }

    public LibraryBean libraryLookupByBibnr(String bibnrInput) {
        LibraryBean libraryBean = null;
        try {
            String url = basebibliotekBibnrUrl + bibnrInput;
            SAXBuilder builder = new SAXBuilder(false);
            InputStream document = httpConnectionWrapper.getResourceAsInputStreamUsingByteArray(url);
            libraryBean = parseResultFromService(builder.build(document));
        } catch (IOException | JDOMException ex) {
//            log.error("Could not retrieve library. Bibnr=" + bibnrInput, ex);
        }
        return libraryBean;
    }


    private LibraryBean parseResultFromService(Document xmlDoc) throws JDOMException {
        LibraryBean libbean;
        String bibnr = extractNodeTextFromDocument("bibnr", xmlDoc);
        String landkode = extractNodeTextFromDocument("landkode", xmlDoc);
        if (StringUtils.isEmpty(bibnr) || StringUtils.isEmpty(landkode)) {
//            log.warn("Bibnr and/or landkode was empty: [bibnr={}]; [landkode={}]", bibnr, landkode);
            return null;
        } else {
            libbean = new LibraryBean();
            libbean.setLandkode(landkode);
            String isilID = landkode.toUpperCase() + "-" + bibnr;   // Prefix to get  ISIL-number for shared partners
            libbean.setBibnr(isilID);
            libbean.setBibkode(extractNodeTextFromDocument("bibkode", xmlDoc));
            libbean.setBibsysBibcode(extractNodeTextFromDocument("bibsys_bib", xmlDoc));
            libbean.setStengt(extractNodeTextFromDocument("stengt", xmlDoc));
            libbean.setInst(extractNodeTextFromDocument("inst", xmlDoc));
            libbean.setPadr(extractNodeTextFromDocument("padr", xmlDoc));
            libbean.setPpostnr(extractNodeTextFromDocument("ppostnr", xmlDoc));
            libbean.setPpoststed(extractNodeTextFromDocument("ppoststed", xmlDoc));
            libbean.setVadr(extractNodeTextFromDocument("vadr", xmlDoc));
            libbean.setVpostnr(extractNodeTextFromDocument("vpostnr", xmlDoc));
            libbean.setVpoststed(extractNodeTextFromDocument("vpoststed", xmlDoc));
            libbean.setTlf(extractNodeTextFromDocument("tlf", xmlDoc));
            libbean.setEpost_adr(extractNodeTextFromDocument("epost_adr", xmlDoc));
            libbean.setEpost_best(extractNodeTextFromDocument("epost_best", xmlDoc));
            libbean.setKatsyst(extractNodeTextFromDocument("katsyst", xmlDoc));
            libbean.setEpost_nill(extractNodeTextFromDocument("epost_nill", xmlDoc));
            libbean.setEpost_nillkvitt(extractNodeTextFromDocument("epost_nillkvitt", xmlDoc));
            libbean.setNncipp_server(StringUtils.trimToEmpty(extractNodeTextFromDocument("nncip_uri", xmlDoc)));
            libbean.setStengt_til(extractNodeTextFromDocument("stengt_til", xmlDoc));
            libbean.setStengt_fra(extractNodeTextFromDocument("stengt_fra", xmlDoc));
            // Hack : If padr is empty set content of ppoststed

            if (libbean.getPadr().isEmpty()) {
                if (!libbean.getPpoststed().isEmpty()) {
                    libbean.setPadr(libbean.getPpoststed());
                }
            }
            // Hack: If vadr is empty set content of vpoststed
            if (libbean.getVadr().isEmpty()) {
                if (!libbean.getVpoststed().isEmpty()) {
                    libbean.setVadr(libbean.getVpoststed());
                }
            }
        }
//        log.debug(libbean.toString());
        return libbean;
    }

    private String extractNodeTextFromDocument(String nodename, Document xmlDoc) throws JDOMException {
        XPath xpath = XPath.newInstance("//*[local-name() = '" + nodename + "']");
        List<Element> nodes = xpath.selectNodes(xmlDoc);
        String nodeText = "";
        if (nodes.size() > 0) {
            Element e = nodes.get(0);
            nodeText = e.getValue();
        }
        return nodeText;
    }
}
