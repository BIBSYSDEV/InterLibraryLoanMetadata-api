package no.unit;

import no.nb.basebibliotek.generated.BaseBibliotek;
import no.nb.basebibliotek.generated.Eressurser;
import no.nb.basebibliotek.generated.Record;
import nva.commons.utils.Environment;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class BaseBibliotekClient {


    private static final String BASEBIBLIOTEK_BIBNR_URL_KEY = "BASEBIBLIOTEK_BIBNR_URL";

    protected transient String basebibliotekBibnrUrl;

    private final Unmarshaller jaxbUnmarshaller;

    private final HttpConnectionWrapper httpConnectionWrapper;

    public BaseBibliotekClient(Environment environment) throws JAXBException {
        this.basebibliotekBibnrUrl = environment.readEnv(BASEBIBLIOTEK_BIBNR_URL_KEY);
        this.httpConnectionWrapper = new HttpConnectionWrapper();
        this.jaxbUnmarshaller = JAXBContext.newInstance(BaseBibliotek.class).createUnmarshaller();
    }

    public BaseBibliotekClient(String basebibliotekBibnrUrl, HttpConnectionWrapper httpConnectionWrapper) throws JAXBException {
        this.basebibliotekBibnrUrl = basebibliotekBibnrUrl;
        this.httpConnectionWrapper = httpConnectionWrapper;
        this.jaxbUnmarshaller = JAXBContext.newInstance(BaseBibliotek.class).createUnmarshaller();
    }

    public LibraryBean libraryLookupByBibnr(String bibnrInput) {
        LibraryBean libraryBean = null;
        try {
            String url = basebibliotekBibnrUrl + bibnrInput;
            InputStream document = httpConnectionWrapper.getResourceAsInputStreamUsingByteArray(url);
            BaseBibliotek baseBibliotek = (BaseBibliotek) jaxbUnmarshaller.unmarshal(document);
            libraryBean = createLibraryBean(baseBibliotek);
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
        return libraryBean;
    }


    private LibraryBean createLibraryBean(BaseBibliotek baseBibliotek) {
        Record record = baseBibliotek.getRecord().get(0);

        LibraryBean libraryBean;
        String bibnr = record.getBibnr();
        String landkode = record.getLandkode();
        if (StringUtils.isEmpty(bibnr) || StringUtils.isEmpty(landkode)) {
            return null;
        } else {
            libraryBean = new LibraryBean();
            libraryBean.setLandkode(landkode);
            libraryBean.setBibnr(record.getIsil());
            libraryBean.setBibkode(record.getBibkode());
//            libraryBean.setBibsysBibcode(extractNodeTextFromDocument("bibsys_bib", xmlDoc));
            libraryBean.setStengt(record.getStengt());
            libraryBean.setInst(record.getInst());
            libraryBean.setPadr(record.getPadr());
            libraryBean.setPpostnr(record.getPpostnr());
            libraryBean.setPpoststed(record.getPpoststed());
            libraryBean.setVadr(record.getVadr());
            libraryBean.setVpostnr(record.getVpostnr());
            libraryBean.setVpoststed(record.getVpoststed());
            libraryBean.setTlf(record.getTlf());
            libraryBean.setEpost_adr(record.getEpostAdr());
            libraryBean.setEpost_best(record.getEpostBest());
            libraryBean.setKatsyst(record.getKatsyst());
//            libraryBean.setEpost_nill(extractNodeTextFromDocument("epost_nill", xmlDoc));
//            libraryBean.setEpost_nillkvitt(extractNodeTextFromDocument("epost_nillkvitt", xmlDoc));
            libraryBean.setNncipp_server(getNncipUri(record));

            libraryBean.setStengt_til(createDateString(record.getStengtTil()));
            libraryBean.setStengt_fra(createDateString(record.getStengtFra()));

            // Hack : If padr is empty set content of ppoststed
            if (libraryBean.getPadr() == null) {
                if (libraryBean.getPpoststed() != null) {
                    libraryBean.setPadr(libraryBean.getPpoststed());
                }
            }

            // Hack: If vadr is empty set content of vpoststed
            if (libraryBean.getVadr() == null) {
                if (libraryBean.getVpoststed() != null) {
                    libraryBean.setVadr(libraryBean.getVpoststed());
                }
            }
        }
        return libraryBean;
    }

    private String createDateString(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(xmlGregorianCalendar.toGregorianCalendar().getTime());
    }

    private String getNncipUri(Record record) {
        Eressurser eressurser = record.getEressurser();
        List<JAXBElement<String>> elementList = eressurser.getOAIOrSRUOrArielIp();
        for (JAXBElement<String> element : elementList) {
            if("nncip_uri".equals(element.getName().getLocalPart())) {
                return element.getValue().trim();
            }
        }
        return null;
    }

}
