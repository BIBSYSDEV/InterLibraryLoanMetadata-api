package no.unit.ill.services;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@SuppressWarnings({"PMD.GodClass", "PMD.TooManyFields"})
public class BaseBibliotekBean {

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");

    private String bibNr = ""; //1160101
    private String bibKode = ""; //NTNU/TEK
    private String inst = ""; //navn
    private String landkode = "";
    // Post address - P.O.Box / mail address
    private String padr = "";
    private String ppostnr = "";
    private String ppoststed = "";
    // Visit address - Street address for on-the-door packet delivery
    private String vadr = "";
    private String vpostnr = "";
    private String vpoststed = "";
    private String tlf = "";
    private String epostAdr = "";
    private String katsyst = "";
    private String epostNill = "";
    private String epostNillkvitt = "";
    private String epostBest = "";
    private String nncippServer = "";
    private Optional<LocalDate> stengtFra = Optional.empty();
    private Optional<LocalDate> stengtTil = Optional.empty();
    private String stengt = "";
    private String bibsysBibcode = "";

    public String getBibNr() {
        return bibNr;
    }

    public void setBibNr(String bibNr) {
        this.bibNr = bibNr;
    }

    public String getBibKode() {
        return bibKode;
    }

    public void setBibKode(String bibKode) {
        this.bibKode = bibKode;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }

    public String getLandkode() {
        return landkode;
    }

    public void setLandkode(String landkode) {
        this.landkode = landkode;
    }

    public String getPadr() {
        return padr;
    }

    public void setPadr(String padr) {
        this.padr = padr;
    }

    public String getPpostnr() {
        return ppostnr;
    }

    public void setPpostnr(String ppostnr) {
        this.ppostnr = ppostnr;
    }

    public String getPpoststed() {
        return ppoststed;
    }

    public void setPpoststed(String ppoststed) {
        this.ppoststed = ppoststed;
    }

    public String getVadr() {
        return vadr;
    }

    public void setVadr(String vadr) {
        this.vadr = vadr;
    }

    public String getVpostnr() {
        return vpostnr;
    }

    public void setVpostnr(String vpostnr) {
        this.vpostnr = vpostnr;
    }

    public String getVpoststed() {
        return vpoststed;
    }

    public void setVpoststed(String vpoststed) {
        this.vpoststed = vpoststed;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getEpostAdr() {
        return epostAdr;
    }

    public void setEpostAdr(String epostAdr) {
        this.epostAdr = epostAdr;
    }

    public String getKatsyst() {
        return katsyst;
    }

    public void setKatsyst(String katsyst) {
        this.katsyst = katsyst;
    }

    public String getEpostNill() {
        return epostNill;
    }

    public void setEpostNill(String epostNill) {
        this.epostNill = epostNill;
    }

    public String getEpostNillkvitt() {
        return epostNillkvitt;
    }

    public void setEpostNillkvitt(String epostNillkvitt) {
        this.epostNillkvitt = epostNillkvitt;
    }

    public String getEpostBest() {
        return epostBest;
    }

    public void setEpostBest(String epostBest) {
        this.epostBest = epostBest;
    }

    public String getNncippServer() {
        return nncippServer;
    }

    public void setNncippServer(String nncippServer) {
        this.nncippServer = nncippServer;
    }


    public String getStengtTil() {
        return stengtTil.map(date -> date.format(dateTimeFormatter)).orElse("");
    }

    public void setStengtTil(String stengtTil) {
        try {
            this.stengtTil = Optional.of(LocalDate.parse(stengtTil, dateTimeFormatter));
        } catch (Exception e) {
            this.stengtTil = Optional.empty();
        }
    }


    public String getStengtFra() {
        return stengtFra.map(date -> date.format(dateTimeFormatter)).orElse("");
    }

    public void setStengtFra(String stengtFra) {
        try {
            this.stengtFra = Optional.of(LocalDate.parse(stengtFra, dateTimeFormatter));
        } catch (Exception e) {
            this.stengtFra = Optional.empty();
        }
    }

    public String getBibsysBibcode() {
        return bibsysBibcode;
    }

    public void setBibsysBibcode(String bibsysBibcode) {
        this.bibsysBibcode = bibsysBibcode;
    }

    public String getStengt() {
        return stengt;
    }

    public void setStengt(String stengt) {
        this.stengt = stengt;
    }

    public boolean isOpenAtDate(LocalDate date) {
        if (StringUtils.isNotEmpty(stengt)) {
            return false;
        }
        boolean startDatePresent = stengtFra.isPresent();
        boolean enDatePresent = stengtTil.isPresent();
        boolean isAfterBeginning = stengtFra.map(from -> !from.isAfter(date)).orElse(false);
        boolean isBeforeEnd = stengtTil.map(until -> !until.isBefore(date)).orElse(false);
        boolean twoDatesAvailable =
                startDatePresent && enDatePresent && isAfterBeginning && isBeforeEnd;
        boolean beginningAvailable = startDatePresent && !enDatePresent && isAfterBeginning;
        boolean endAvailable = !startDatePresent && enDatePresent && isBeforeEnd;

        return !(twoDatesAvailable || beginningAvailable || endAvailable);
    }

    public boolean dateIsDefined() {
        return this.stengtTil.isPresent() || this.stengtFra.isPresent();
    }

}