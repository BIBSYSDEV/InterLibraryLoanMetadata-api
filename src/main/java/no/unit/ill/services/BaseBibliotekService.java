package no.unit.ill.services;

import no.nb.basebibliotek.generated.BaseBibliotek;
import no.nb.basebibliotek.generated.Eressurser;
import no.nb.basebibliotek.generated.Record;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class BaseBibliotekService {

    private static final transient Logger log = LoggerFactory.getLogger(BaseBibliotekService.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private final transient Unmarshaller jaxbUnmarshaller;
    private final transient BaseBibliotekServiceConnection connection;

    public static final String WRONG_URL_FOR_GET_IN_BASEBIBLIOTEK_SERVICE_FOR = "Wrong Url for get in "
            + "baseBibliotekService for {}";
    public static final String ERROR_WHILE_GETTING_AT_BASEBIBLIOTEK_SERVICE_FOR = "Error while getting at "
            + "basebibliotekService for {}";
    public static final String ERROR_WHILE_UNMARSHALLING_BASEBIBLIOTEK_SERVICE_RESPONSE =
            "Error unmarshalling response from basebibliotekService for {}";


    public BaseBibliotekService() throws JAXBException {
        this.connection = new BaseBibliotekServiceConnection();
        this.jaxbUnmarshaller = JAXBContext.newInstance(BaseBibliotek.class).createUnmarshaller();
    }

    public BaseBibliotekService(BaseBibliotekServiceConnection connection) throws JAXBException {
        this.connection = connection;
        this.jaxbUnmarshaller = JAXBContext.newInstance(BaseBibliotek.class).createUnmarshaller();
    }

    /**
     * Get library information from BaseBibliotekService by given identifier (bibnr).
     *
     * @param identifier identifier (bibnr)
     * @return BaseBibliotekBean
     */
    public BaseBibliotekBean libraryLookupByBibnr(String identifier) {
        BaseBibliotekBean baseBibliotekBean = null;
        try (InputStream document = connection.connect(identifier)) {
            BaseBibliotek baseBibliotek = (BaseBibliotek) jaxbUnmarshaller.unmarshal(document);
            baseBibliotekBean = createBaseBibliotekBean(baseBibliotek);
        } catch (URISyntaxException e) {
            log.error(WRONG_URL_FOR_GET_IN_BASEBIBLIOTEK_SERVICE_FOR, identifier, e);
        } catch (IOException e) {
            log.error(ERROR_WHILE_GETTING_AT_BASEBIBLIOTEK_SERVICE_FOR, identifier, e);
        } catch (JAXBException e) {
            log.error(ERROR_WHILE_UNMARSHALLING_BASEBIBLIOTEK_SERVICE_RESPONSE, identifier, e);
        }
        return baseBibliotekBean;
    }


    private BaseBibliotekBean createBaseBibliotekBean(BaseBibliotek baseBibliotek) {
        Record record = baseBibliotek.getRecord().get(0);

        BaseBibliotekBean baseBibliotekBean;
        String bibnr = record.getBibnr();
        String landkode = record.getLandkode();
        if (StringUtils.isEmpty(bibnr) || StringUtils.isEmpty(landkode)) {
            return null;
        } else {
            baseBibliotekBean = new BaseBibliotekBean();
            baseBibliotekBean.setLandkode(landkode);
            baseBibliotekBean.setBibNr(record.getIsil());
            baseBibliotekBean.setBibKode(record.getBibkode());
            // libraryBean.setBibsysBibcode(extractNodeTextFromDocument("bibsys_bib", xmlDoc));
            baseBibliotekBean.setStengt(record.getStengt());
            baseBibliotekBean.setInst(record.getInst());
            baseBibliotekBean.setPadr(record.getPadr());
            baseBibliotekBean.setPpostnr(record.getPpostnr());
            baseBibliotekBean.setPpoststed(record.getPpoststed());
            baseBibliotekBean.setVadr(record.getVadr());
            baseBibliotekBean.setVpostnr(record.getVpostnr());
            baseBibliotekBean.setVpoststed(record.getVpoststed());
            baseBibliotekBean.setTlf(record.getTlf());
            baseBibliotekBean.setEpostAdr(record.getEpostAdr());
            baseBibliotekBean.setEpostBest(record.getEpostBest());
            baseBibliotekBean.setKatsyst(record.getKatsyst());
            // libraryBean.setEpost_nill(extractNodeTextFromDocument("epost_nill", xmlDoc));
            // libraryBean.setEpost_nillkvitt(extractNodeTextFromDocument("epost_nillkvitt", xmlDoc));
            baseBibliotekBean.setNncippServer(getNncipUri(record));

            baseBibliotekBean.setStengtTil(createDateString(record.getStengtTil()));
            baseBibliotekBean.setStengtFra(createDateString(record.getStengtFra()));

            // Hack : If padr is empty set content of ppoststed
            if (baseBibliotekBean.getPadr() == null && baseBibliotekBean.getPpoststed() != null) {
                baseBibliotekBean.setPadr(baseBibliotekBean.getPpoststed());
            }

            // Hack: If vadr is empty set content of vpoststed
            if (baseBibliotekBean.getVadr() == null && baseBibliotekBean.getVpoststed() != null) {
                baseBibliotekBean.setVadr(baseBibliotekBean.getVpoststed());
            }
        }
        return baseBibliotekBean;
    }

    private String createDateString(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        synchronized (dateFormat) {
            return dateFormat.format(xmlGregorianCalendar.toGregorianCalendar().getTime());
        }
    }

    private String getNncipUri(Record record) {
        Eressurser eressurser = record.getEressurser();
        List<JAXBElement<String>> elementList = eressurser.getOAIOrSRUOrArielIp();
        for (JAXBElement<String> element : elementList) {
            if ("nncip_uri".equals(element.getName().getLocalPart())) {
                return element.getValue().trim();
            }
        }
        return null;
    }

}
