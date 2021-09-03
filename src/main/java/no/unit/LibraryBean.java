package no.unit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LibraryBean {

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");

    private String bibnr = ""; //1160101
    private String bibkode = ""; //NTNU/TEK
    private String inst = ""; //navn
    private String landkode = "";
    // Post address   -   P.O.Box / mail address
    private String padr = "";
    private String ppostnr = "";
    private String ppoststed = "";
    // Visit address  -   Street address for on-the-door packet delivery
    private String vadr = "";
    private String vpostnr = "";
    private String vpoststed = "";
    private String tlf = "";
    private String epost_adr = "";
    private String katsyst = "";
    private String epost_nill = "";
    private String epost_nillkvitt = "";
    private String epost_best = "";
    private String nncipp_server = "";
    private Optional<LocalDate> stengt_fra = Optional.empty();
    private Optional<LocalDate> stengt_til = Optional.empty();
    private String stengt = "";
    private String bibsysBibcode = "";


    public String getBibnr() {
        return bibnr;
    }

    public void setBibnr(String bibnr) {
        this.bibnr = bibnr;
    }

    public String getBibkode() {
        return bibkode;
    }

    public void setBibkode(String bibkode) {
        this.bibkode = bibkode;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
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

    public String getLandkode() {
        return landkode;
    }

    public void setLandkode(String landkode) {
        this.landkode = landkode;
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

    public String getEpost_adr() {
        return epost_adr;
    }

    public void setEpost_adr(String epost_adr) {
        this.epost_adr = epost_adr;
    }

    public String getKatsyst() {
        return katsyst;
    }

    public void setKatsyst(String katsyst) {
        this.katsyst = katsyst;

    }

    public String getEpost_nill() {
        return epost_nill;
    }

    public void setEpost_nill(String epost_nill) {
        this.epost_nill = epost_nill;

    }

    public String getEpost_nillkvitt() {
        return epost_nillkvitt;
    }

    public void setEpost_nillkvitt(String epost_nillkvitt) {
        this.epost_nillkvitt = epost_nillkvitt;

    }

    public String getEpost_best() {
        return epost_best;
    }

    public void setEpost_best(String epost_best) {
        this.epost_best = epost_best;

    }

    public String getNncipp_server() {
        return nncipp_server;
    }

    public void setNncipp_server(String nncipp_server) {
        this.nncipp_server = nncipp_server;
    }


    public boolean isOpenAtDate(LocalDate date) {
        if (stengt.length() > 0) return false;

        boolean startDatePresent = stengt_fra.isPresent();
        boolean enDatePresent = stengt_til.isPresent();
        boolean isAfterBeginning = stengt_fra.map(from -> !from.isAfter(date)).orElse(false);
        boolean isBeforeEnd = stengt_til.map(until -> !until.isBefore(date)).orElse(false);
        boolean twoDatesAvailable =
                startDatePresent && enDatePresent && isAfterBeginning && isBeforeEnd;
        boolean beginningAvailable = startDatePresent && !enDatePresent && isAfterBeginning;
        boolean endAvailable = !startDatePresent && enDatePresent && isBeforeEnd;

        return !(twoDatesAvailable || beginningAvailable || endAvailable);
    }


    public String getStengt_til() {
        return stengt_til.map(date -> date.format(dateTimeFormatter)).orElse("");
    }

    public void setStengt_til(String stengt_til) {
        try {
            this.stengt_til = Optional.of(LocalDate.parse(stengt_til, dateTimeFormatter));
        } catch (Exception e) {
            this.stengt_til = Optional.empty();
        }
    }

    public boolean dateIsDefined() {
        return this.stengt_til.isPresent() || this.stengt_fra.isPresent();
    }

    public String getStengt_fra() {
        return stengt_fra.map(date -> date.format(dateTimeFormatter)).orElse("");
    }

    public void setStengt_fra(String stengt_fra) {
        try {
            this.stengt_fra = Optional.of(LocalDate.parse(stengt_fra, dateTimeFormatter));
        } catch (Exception e) {
            this.stengt_fra = Optional.empty();
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

    @Override
    public String toString() {
        return "LibraryBean{" +
                "bibnr='" + bibnr + '\'' +
                ", bibkode='" + bibkode + '\'' +
                ", inst='" + inst + '\'' +
                ", landkode='" + landkode + '\'' +
                ", padr='" + padr + '\'' +
                ", ppostnr='" + ppostnr + '\'' +
                ", ppoststed='" + ppoststed + '\'' +
                ", vadr='" + vadr + '\'' +
                ", vpostnr='" + vpostnr + '\'' +
                ", vpoststed='" + vpoststed + '\'' +
                ", tlf='" + tlf + '\'' +
                ", epost_adr='" + epost_adr + '\'' +
                ", katsyst='" + katsyst + '\'' +
                ", epost_nill='" + epost_nill + '\'' +
                ", epost_nillkvitt='" + epost_nillkvitt + '\'' +
                ", epost_best='" + epost_best + '\'' +
                ", nncipp_server='" + nncipp_server + '\'' +
                ", stengt_fra=" + stengt_fra +
                ", stengt_til=" + stengt_til +
                ", stengt='" + stengt + '\'' +
                ", bibsysBibcode='" + bibsysBibcode + '\'' +
                '}';
    }

}