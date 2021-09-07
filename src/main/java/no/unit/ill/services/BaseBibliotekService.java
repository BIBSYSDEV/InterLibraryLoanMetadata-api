package no.unit.ill.services;

import no.nb.basebibliotek.generated.BaseBibliotek;
import no.nb.basebibliotek.generated.Eressurser;
import no.nb.basebibliotek.generated.Record;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class BaseBibliotekService {

    private final Unmarshaller jaxbUnmarshaller;
    private final transient BaseBibliotekServiceConnection connection;


    public BaseBibliotekService() throws JAXBException {
        this.connection = new BaseBibliotekServiceConnection();
        this.jaxbUnmarshaller = JAXBContext.newInstance(BaseBibliotek.class).createUnmarshaller();
    }

    public BaseBibliotekService(BaseBibliotekServiceConnection connection) throws JAXBException {
        this.connection = connection;
        this.jaxbUnmarshaller = JAXBContext.newInstance(BaseBibliotek.class).createUnmarshaller();
    }

    public BaseBibliotekBean libraryLookupByBibnr(String identifier) throws IOException, URISyntaxException, JAXBException {
        BaseBibliotekBean baseBibliotekBean = null;
        try (InputStream document = connection.connect(identifier)) {
            BaseBibliotek baseBibliotek = (BaseBibliotek) jaxbUnmarshaller.unmarshal(document);
            baseBibliotekBean = createBaseBibliotekBean(baseBibliotek);
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
            baseBibliotekBean.setBibnr(record.getIsil());
            baseBibliotekBean.setBibkode(record.getBibkode());
//            libraryBean.setBibsysBibcode(extractNodeTextFromDocument("bibsys_bib", xmlDoc));
            baseBibliotekBean.setStengt(record.getStengt());
            baseBibliotekBean.setInst(record.getInst());
            baseBibliotekBean.setPadr(record.getPadr());
            baseBibliotekBean.setPpostnr(record.getPpostnr());
            baseBibliotekBean.setPpoststed(record.getPpoststed());
            baseBibliotekBean.setVadr(record.getVadr());
            baseBibliotekBean.setVpostnr(record.getVpostnr());
            baseBibliotekBean.setVpoststed(record.getVpoststed());
            baseBibliotekBean.setTlf(record.getTlf());
            baseBibliotekBean.setEpost_adr(record.getEpostAdr());
            baseBibliotekBean.setEpost_best(record.getEpostBest());
            baseBibliotekBean.setKatsyst(record.getKatsyst());
//            libraryBean.setEpost_nill(extractNodeTextFromDocument("epost_nill", xmlDoc));
//            libraryBean.setEpost_nillkvitt(extractNodeTextFromDocument("epost_nillkvitt", xmlDoc));
            baseBibliotekBean.setNncipp_server(getNncipUri(record));

            baseBibliotekBean.setStengt_til(createDateString(record.getStengtTil()));
            baseBibliotekBean.setStengt_fra(createDateString(record.getStengtFra()));

            // Hack : If padr is empty set content of ppoststed
            if (baseBibliotekBean.getPadr() == null) {
                if (baseBibliotekBean.getPpoststed() != null) {
                    baseBibliotekBean.setPadr(baseBibliotekBean.getPpoststed());
                }
            }

            // Hack: If vadr is empty set content of vpoststed
            if (baseBibliotekBean.getVadr() == null) {
                if (baseBibliotekBean.getVpoststed() != null) {
                    baseBibliotekBean.setVadr(baseBibliotekBean.getVpoststed());
                }
            }
        }
        return baseBibliotekBean;
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
