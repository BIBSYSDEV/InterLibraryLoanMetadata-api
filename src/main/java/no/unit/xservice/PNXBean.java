package no.unit.xservice;

import no.unit.utils.StringUtils;

import java.util.List;

public class PNXBean {

    private static final String PRINT_JOURNALS = "print-journals";
    private String date = "";
    private String patronid = "";
    private String recordid = "";
    private String itemid = "";
    private String type = "";
    private String comment = "";
    private String localBorrowerId = "";
    private String source = "";
    private String creator = "";
    private String pages = "";
    private String issue = "";
    private String volume = "";
    private String year = "";
    private String orderFrom = "";
    private String toAgency = "";
    private String isbn = "";
    private String issn = "";
    private String atitle = "";
    private String jtitle = "";
    private String title = "";
    private String vid = "";
    private String copyOrLoan = "";
    private String author = "";
    private String mmsId = "";
    private String placeOfPublication = "";
    private String publisher = "";
    private List<String> libraries;
    private List<String> almaids;
    private List<String> almaMms_ids;
    private List<String> RTA_ids;
    private String eissn = "";
    private boolean isPhotoCopyAllowed = true;
    private boolean isValid = false;
    private String barcode = "";
    private List<String> resourceTypes;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = StringUtils.defaultString(publisher);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = StringUtils.defaultString(date);
    }

    public String getPatronid() {
        return patronid;
    }

    public void setPatronid(String patronid) {
        this.patronid = StringUtils.defaultString(patronid);
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = StringUtils.defaultString(recordid);
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = StringUtils.defaultString(itemid);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = StringUtils.defaultString(type);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = StringUtils.defaultString(comment);
    }

    public String getLocalBorrowerId() {
        return localBorrowerId;
    }

    public void setLocalBorrowerId(String localBorrowerId) {
        this.localBorrowerId = StringUtils.defaultString(localBorrowerId);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = StringUtils.defaultString(source);
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = StringUtils.defaultString(creator);
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = StringUtils.defaultString(pages);
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = StringUtils.defaultString(issue);
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = StringUtils.defaultString(volume);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = StringUtils.defaultString(year);
    }

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = StringUtils.defaultString(orderFrom);
    }

    public String getToAgency() {
        return toAgency;
    }

    public void setToAgency(String toAgency) {
        this.toAgency = StringUtils.defaultString(toAgency);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = StringUtils.defaultString(isbn);
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = StringUtils.defaultString(issn);
    }

    public String getAtitle() {
        return atitle;
    }

    public void setAtitle(String atitle) {
        this.atitle = StringUtils.defaultString(atitle);
    }

    public String getJtitle() {
        return jtitle;
    }

    public void setJtitle(String jtitle) {
        this.jtitle = StringUtils.defaultString(jtitle);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = StringUtils.defaultString(title);
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = StringUtils.defaultString(vid);
    }

    public String getCopyOrLoan() {
        return copyOrLoan;
    }

    public void setCopyOrLoan(String copyOrLoan) {
        this.copyOrLoan = StringUtils.defaultString(copyOrLoan);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = StringUtils.defaultString(author);
    }

    public String getMmsId() {
        return mmsId;
    }

    public void setMmsId(String mmsId) {
        this.mmsId = StringUtils.defaultString(mmsId);
    }

    public List<String> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<String> libraries) {
        this.libraries = libraries;
    }

    public List<String> getAlmaids() {
        return almaids;
    }

    public void setAlmaids(List<String> almaids) {
        this.almaids = almaids;
    }

    public List<String> getAlmaMms_ids() {
        return almaMms_ids;
    }

    public void setAlmaMms_ids(List<String> almaMms_ids) {
        this.almaMms_ids = almaMms_ids;
    }

    public String getEissn() {
        return eissn;
    }

    public void setEissn(String eissn) {
        this.eissn = StringUtils.defaultString(eissn);
    }

    public boolean isPhotoCopyAllowed() {
        return isPhotoCopyAllowed;
    }

    public void setPhotoCopyAllowed(boolean photoCopyAllowed) {
        isPhotoCopyAllowed = photoCopyAllowed;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public void setPlaceOfPublication(String placeOfPublication) {
        this.placeOfPublication = StringUtils.defaultString(placeOfPublication);
    }

    public String getPlaceOfPublication() {
        return placeOfPublication;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<String> getResourceTypes() {
        return resourceTypes;
    }

    public void setResourceTypes(List<String> resourceTypes) {
        this.resourceTypes = resourceTypes;
    }

    public boolean isIssueMode() {
        return resourceTypes.contains(PRINT_JOURNALS);
    }

    public void setRTA_ids(List<String> RTA_ids) {
        this.RTA_ids = RTA_ids;
    }

    public List<String> getRTA_ids() {
        return RTA_ids;
    }
}

