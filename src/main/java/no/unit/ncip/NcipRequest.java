package no.unit.ncip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nva.commons.core.JacocoGenerated;
import org.apache.commons.lang3.StringUtils;

public class NcipRequest {

    public static final String TO_AGENCY_ID = "toAgencyId";
    public static final String FROM_AGENCY_ID = "fromAgencyId";
    public static final String ISBN_VALUE = "isbnValue";
    public static final String USER_IDENTIFIER_VALUE = "userIdentifierValue";
    public static final String AUTHOR = "author";
    public static final String TITLE = "title";
    public static final String PUBLISHER = "publisher";
    public static final String PUBLICATION_DATE = "publicationDate";
    public static final String PLACE_OF_PUBLICATION = "placeOfPublication";
    public static final String BIBLIOGRAPHIC_RECORD_IDENTIFIER = "bibliographicRecordIdentifier";
    public static final String BIBLIOGRAPHIC_RECORD_IDENTIFIER_CODE = "bibliographicRecordIdentifierCode";
    public static final String TYPE = "type";
    public static final String REQUEST_TYPE = "requestType";
    public static final String COMMENT = "comment";
    public static final String NCIP_SERVER_URL = "ncipServerUrl";
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
    public transient String comment;
    public transient String ncipServerUrl;

    @JacocoGenerated
    @JsonCreator
    @SuppressWarnings("PMD.ExcessiveParameterList")
    public NcipRequest(@JsonProperty(TO_AGENCY_ID) String toAgencyId,
                       @JsonProperty(FROM_AGENCY_ID) String fromAgencyId,
                       @JsonProperty(ISBN_VALUE) String isbnValue,
                       @JsonProperty(USER_IDENTIFIER_VALUE) String userIdentifierValue,
                       @JsonProperty(AUTHOR) String author,
                       @JsonProperty(TITLE) String title,
                       @JsonProperty(PUBLISHER) String publisher,
                       @JsonProperty(PUBLICATION_DATE) String publicationDate,
                       @JsonProperty(PLACE_OF_PUBLICATION) String placeOfPublication,
                       @JsonProperty(BIBLIOGRAPHIC_RECORD_IDENTIFIER) String bibliographicRecordIdentifier,
                       @JsonProperty(BIBLIOGRAPHIC_RECORD_IDENTIFIER_CODE)
                                   String bibliographicRecordIdentifierCode,
                       @JsonProperty(TYPE) String type,
                       @JsonProperty(REQUEST_TYPE) String requestType,
                       @JsonProperty(COMMENT) String comment,
                       @JsonProperty(NCIP_SERVER_URL) String ncipServerUrl) {
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
        this.comment = comment;
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
