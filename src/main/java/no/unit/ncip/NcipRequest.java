package no.unit.ncip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nva.commons.core.JacocoGenerated;
import org.apache.commons.lang3.StringUtils;

public class NcipRequest {

    public static final String BESTILT_FRA_ORIA = "Bestilt fra Oria";
    public transient String toAgencyId;
    public transient String fromAgencyId;
    public transient String isbnValue;
    public transient String userIdentifierValue;
    public transient String author;
    public transient String title;
    public transient String publisher;
    public transient String publicationDate;
    public transient String placeOfPublication;
    public transient String bibliographicRecordIdentifier;
    public transient String bibliographicRecordIdentifierCode;
    public transient String type;
    public transient String requestType;
    public transient String comment = BESTILT_FRA_ORIA;
    public transient String ncipServerUrl;

    @JsonCreator
    @JacocoGenerated
    @SuppressWarnings("PMD.ExcessiveParameterList")
    public NcipRequest(@JsonProperty("toAgencyId") String toAgencyId,
                       @JsonProperty("fromAgencyId") String fromAgencyId,
                       @JsonProperty("isbnValue") String isbnValue,
                       @JsonProperty("userIdentifierValue") String userIdentifierValue,
                       @JsonProperty("author") String author,
                       @JsonProperty("title") String title,
                       @JsonProperty("publisher") String publisher,
                       @JsonProperty("publicationDate") String publicationDate,
                       @JsonProperty("placeOfPublication") String placeOfPublication,
                       @JsonProperty("bibliographicRecordIdentifier") String bibliographicRecordIdentifier,
                       @JsonProperty("bibliographicRecordIdentifierCode") String bibliographicRecordIdentifierCode,
                       @JsonProperty("type") String type,
                       @JsonProperty("requestType") String requestType,
                       @JsonProperty("comment") String comment,
                       @JsonProperty("ncipServerUrl") String ncipServerUrl) {
        this.toAgencyId = toAgencyId;
        this.fromAgencyId = fromAgencyId;
        this.isbnValue = isbnValue;
        this.userIdentifierValue = userIdentifierValue;
        this.author = author;
        this.title = title;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.placeOfPublication = placeOfPublication;
        this.bibliographicRecordIdentifier = bibliographicRecordIdentifier;
        this.bibliographicRecordIdentifierCode = bibliographicRecordIdentifierCode;
        this.type = type;
        this.requestType = requestType;
        if (StringUtils.isNotEmpty(comment)) {
            this.comment = comment;
        }
        this.ncipServerUrl = ncipServerUrl;
    }


    @Override
    public String toString() {
        return "NcipRequest{" +
                "toAgencyId='" + toAgencyId + '\'' +
                ", fromAgencyId='" + fromAgencyId + '\'' +
                ", isbnValue='" + isbnValue + '\'' +
                ", userIdentifierValue='" + "Retracted" + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", placeOfPublication='" + placeOfPublication + '\'' +
                ", bibliographicRecordIdentifier='" + bibliographicRecordIdentifier + '\'' +
                ", bibliographicRecordIdentifierCode='" + bibliographicRecordIdentifierCode + '\'' +
                ", type='" + type + '\'' +
                ", requestType='" + requestType + '\'' +
                ", comment='" + comment + '\'' +
                ", ncipServerUrl='" + ncipServerUrl + '\'' +
                '}';
    }

    /**
     * Checks if sufficient data is send over.
     * @return [TRUE] if valid
     */
    public boolean isValid() {
        if (StringUtils.isBlank(bibliographicRecordIdentifier)) {
            return false;
        }
        if (StringUtils.isBlank(bibliographicRecordIdentifierCode)) {
            return false;
        }
        if (StringUtils.isBlank(fromAgencyId)) {
            return false;
        }
        if (StringUtils.isBlank(toAgencyId)) {
            return false;
        }
        if (StringUtils.isBlank(userIdentifierValue)) {
            return false;
        }
        return !StringUtils.isBlank(ncipServerUrl);
    }

}
