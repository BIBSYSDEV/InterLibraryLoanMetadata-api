package no.unit.ncip;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NcipRequestTest {

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
    public static final String EMPTY_STRING = "";


    @Test
    public void testValidWithAllParams() {
        NcipRequest msg = new NcipRequest(TO_AGENCY_ID, FROM_AGENCY_ID, ISBN_VALUE,
            USER_IDENTIFIER_VALUE, AUTHOR, TITLE, PUBLISHER, PUBLICATION_DATE, PLACE_OF_PUBLICATION,
            BIBLIOGRAPHIC_RECORD_IDENTIFIER, BIBLIOGRAPHIC_RECORD_IDENTIFIER_CODE, TYPE, REQUEST_TYPE, COMMENT,
            NCIP_SERVER_URL);
        assertTrue(msg.isValid());
    }

    @Test
    public void testValidWithoutSomeParams() {
        NcipRequest msg = new NcipRequest(TO_AGENCY_ID, FROM_AGENCY_ID, ISBN_VALUE,
            USER_IDENTIFIER_VALUE, AUTHOR, TITLE, PUBLISHER, PUBLICATION_DATE, PLACE_OF_PUBLICATION,
            EMPTY_STRING, BIBLIOGRAPHIC_RECORD_IDENTIFIER_CODE, TYPE, REQUEST_TYPE, COMMENT,
            NCIP_SERVER_URL);
        assertFalse(msg.isValid());

        msg = new NcipRequest(TO_AGENCY_ID, FROM_AGENCY_ID, ISBN_VALUE,
            USER_IDENTIFIER_VALUE, AUTHOR, TITLE, PUBLISHER, PUBLICATION_DATE, PLACE_OF_PUBLICATION,
            BIBLIOGRAPHIC_RECORD_IDENTIFIER, EMPTY_STRING, TYPE, REQUEST_TYPE, COMMENT,
            NCIP_SERVER_URL);
        assertFalse(msg.isValid());

        msg = new NcipRequest(TO_AGENCY_ID, EMPTY_STRING, ISBN_VALUE,
            USER_IDENTIFIER_VALUE, AUTHOR, TITLE, PUBLISHER, PUBLICATION_DATE, PLACE_OF_PUBLICATION,
            BIBLIOGRAPHIC_RECORD_IDENTIFIER, BIBLIOGRAPHIC_RECORD_IDENTIFIER_CODE, TYPE, REQUEST_TYPE, COMMENT,
            NCIP_SERVER_URL);
        assertFalse(msg.isValid());

        msg = new NcipRequest(EMPTY_STRING, FROM_AGENCY_ID, ISBN_VALUE,
            USER_IDENTIFIER_VALUE, AUTHOR, TITLE, PUBLISHER, PUBLICATION_DATE, PLACE_OF_PUBLICATION,
            BIBLIOGRAPHIC_RECORD_IDENTIFIER, BIBLIOGRAPHIC_RECORD_IDENTIFIER_CODE, TYPE, REQUEST_TYPE, COMMENT,
            NCIP_SERVER_URL);
        assertFalse(msg.isValid());

        msg = new NcipRequest(TO_AGENCY_ID, FROM_AGENCY_ID, ISBN_VALUE,
            EMPTY_STRING, AUTHOR, TITLE, PUBLISHER, PUBLICATION_DATE, PLACE_OF_PUBLICATION,
            BIBLIOGRAPHIC_RECORD_IDENTIFIER, BIBLIOGRAPHIC_RECORD_IDENTIFIER_CODE, TYPE, REQUEST_TYPE, COMMENT,
            NCIP_SERVER_URL);
        assertFalse(msg.isValid());

        msg = new NcipRequest(TO_AGENCY_ID, FROM_AGENCY_ID, EMPTY_STRING,
            USER_IDENTIFIER_VALUE, AUTHOR, TITLE, PUBLISHER, PUBLICATION_DATE, PLACE_OF_PUBLICATION,
            BIBLIOGRAPHIC_RECORD_IDENTIFIER, BIBLIOGRAPHIC_RECORD_IDENTIFIER_CODE, TYPE, REQUEST_TYPE, COMMENT,
            NCIP_SERVER_URL);
        assertTrue(msg.isValid());
    }

}