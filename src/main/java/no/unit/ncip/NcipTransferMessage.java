package no.unit.ncip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import nva.commons.core.JacocoGenerated;
import org.apache.commons.lang3.StringUtils;

public class NcipTransferMessage {

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
    public final String toAgencyId;
    public final String fromAgencyId;
    private final String isbnValue;
    public final String userIdentifierValue;
    public final String author;
    public final String title;
    public final String publisher;
    public final String publicationDate;
    public final String placeOfPublication;
    public final String bibliographicRecordIdentifier;
    public final String bibliographicRecordIdentifierCode;
    public final String type;
    public final String requestType;
    public final String comment;
    private final String ncipServerUrl;

    /**
     * Creates and NcipTransferMessage with given properties.
     */
    @JacocoGenerated
    @JsonCreator
    @SuppressWarnings("PMD.ExcessiveParameterList")
    public NcipTransferMessage(@JsonProperty(TO_AGENCY_ID) String toAgencyId,
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
        return !StringUtils.isBlank(userIdentifierValue);
    }

    @JacocoGenerated
    public String getNcipServerUrl() {
        return ncipServerUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NcipTransferMessage that = (NcipTransferMessage) o;
        return Objects.equals(toAgencyId, that.toAgencyId) && Objects.equals(fromAgencyId,
            that.fromAgencyId) && Objects.equals(isbnValue, that.isbnValue) && Objects.equals(
            userIdentifierValue, that.userIdentifierValue) && Objects.equals(author, that.author)
            && Objects.equals(title, that.title) && Objects.equals(publisher, that.publisher)
            && Objects.equals(publicationDate, that.publicationDate) && Objects.equals(
            placeOfPublication, that.placeOfPublication) && Objects.equals(bibliographicRecordIdentifier,
            that.bibliographicRecordIdentifier) && Objects.equals(bibliographicRecordIdentifierCode,
            that.bibliographicRecordIdentifierCode) && Objects.equals(type, that.type)
            && Objects.equals(requestType, that.requestType) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toAgencyId, fromAgencyId, isbnValue, userIdentifierValue, author, title, publisher,
            publicationDate, placeOfPublication, bibliographicRecordIdentifier, bibliographicRecordIdentifierCode, type,
            requestType, comment);
    }

}
