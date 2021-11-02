package no.unit.ncip;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NcipUtils {

    public static final String ISIL_PREFIX = "NO-";
    public static final String RETRACTED = "Retracted";
    public static final String REGEX_PATTERN_REPLACE_TAG_CONTENT =
            "<ns1:UserIdentifierValue>(.*?)</ns1:UserIdentifierValue>";

    /**
     * Generates xml formattted NCIP-message.
     * @param ncipRequest NcipTransferMessage object that holds ncip data.
     * @return xml formatted NCIP-message
     */
    public static String ncipMessageAsXml(NcipRequest ncipRequest) {
        String ncipXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3"
            + ".org/2001/XMLSchema-instance\"\n"
            + "                 ns1:version=\"http://www.niso.org/schemas/ncip/v2_02/ncip_v2_02.xsd\"\n"
            + "                 xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso"
            + ".org/schemas/ncip/v2_02/ncip_v2_02.xsd\">\n"
            + "    <ns1:ItemRequested>\n"
            + "        <ns1:InitiationHeader>\n"
            + "            <ns1:FromSystemId>ORIA_NCIP_ILL</ns1:FromSystemId>"
            + "            <ns1:FromAgencyId>\n"
            + "                <ns1:AgencyId>" + ISIL_PREFIX + ncipRequest.fromAgencyId + "</ns1:AgencyId>\n"
            + "            </ns1:FromAgencyId>\n"
            + "            <ns1:ToAgencyId>\n"
            + "                <ns1:AgencyId>" + ISIL_PREFIX + ncipRequest.toAgencyId + "</ns1:AgencyId>\n"
            + "            </ns1:ToAgencyId>\n"
            + "        </ns1:InitiationHeader>\n"
            + "        <ns1:UserId>\n"
            + "            <ns1:UserIdentifierValue>" + ncipRequest.userIdentifierValue + "</ns1:UserIdentifierValue>\n"
            + "        </ns1:UserId>\n"
            + "        <ns1:BibliographicId>\n"
            + "            <ns1:BibliographicRecordId>\n"
            + "                <ns1:BibliographicRecordIdentifier>" + ncipRequest.bibliographicRecordIdentifier
            + "</ns1:BibliographicRecordIdentifier>\n"
            + "                <ns1:BibliographicRecordIdentifierCode>"
            + ncipRequest.bibliographicRecordIdentifierCode + "</ns1:BibliographicRecordIdentifierCode>\n"
            + "            </ns1:BibliographicRecordId>\n"
            + "        </ns1:BibliographicId>\n"
            + "        <ns1:RequestId>\n"
            + "            <ns1:AgencyId/>\n"
            + "            <ns1:RequestIdentifierValue/>\n"
            + "        </ns1:RequestId>\n"
            + "        <ns1:RequestType>" + ncipRequest.requestType + "</ns1:RequestType>\n"
            + "        <ns1:RequestScopeType>Title</ns1:RequestScopeType>\n"
            + "        <ns1:ItemOptionalFields>\n"
            + "            <ns1:BibliographicDescription>\n"
            + "                <ns1:Author>" + ncipRequest.author + "</ns1:Author>\n"
            + "                <ns1:PlaceOfPublication>" + ncipRequest.placeOfPublication
            + "</ns1:PlaceOfPublication>\n"
            + "                <ns1:PublicationDate>" + ncipRequest.publicationDate + "</ns1:PublicationDate>\n"
            + "                <ns1:Publisher>" + ncipRequest.publisher + "</ns1:Publisher>\n"
            + "                <ns1:Title>" + ncipRequest.title + "</ns1:Title>\n"
            + "                <ns1:BibliographicLevel>" + ncipRequest.type + "</ns1:BibliographicLevel>\n"
            + "                <ns1:MediumType>" + ncipRequest.type + "</ns1:MediumType>\n"
            + "            </ns1:BibliographicDescription>\n"
            + "        </ns1:ItemOptionalFields>\n"
            + "        <ns1:Ext>\n"
            + "            <ns1:ItemNote>" + ncipRequest.comment + "</ns1:ItemNote>\n"
            + "        </ns1:Ext>\n"
            + "    </ns1:ItemRequested>\n"
            + "</ns1:NCIPMessage>\n";
        return ncipXML;
    }

    protected static String retractUserIdentifier(String xml) {
        Pattern pattern = Pattern.compile(REGEX_PATTERN_REPLACE_TAG_CONTENT);
        Matcher matcher = pattern.matcher(xml);

        String modifiedXml = xml;
        if (matcher.find()) {
            modifiedXml = xml.replaceAll(matcher.group(1), RETRACTED);
        }
        return modifiedXml;
    }
    
    
}
