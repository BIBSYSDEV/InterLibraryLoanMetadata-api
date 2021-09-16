package no.unit.ncip;

import no.unit.ncip.NcipTransferMessage;

public class NcipUtils {

    /**
     * Generates xml formattted NCIP-message.
     * @param msg NcipTransferMessage object that holds ncip data.
     * @return xml formatted NCIP-message
     */
    public static String ncipMessageAsXml(NcipTransferMessage msg) {
        String ncipXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3"
            + ".org/2001/XMLSchema-instance\"\n"
            + "                 ns1:version=\"http://www.niso.org/schemas/ncip/v2_02/ncip_v2_02.xsd\"\n"
            + "                 xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso"
            + ".org/schemas/ncip/v2_02/ncip_v2_02.xsd\">\n"
            + "<!--Usage in NNCIPP 1.0 is in use-case 3, call #8: A user request home library to order an item,"
            + " from a external library.  -->\n"
            + "    <ns1:ItemRequested>\n"
            + "        <!-- The InitiationHeader, stating from- and to-agency, is mandatory. -->\n"
            + "        <ns1:InitiationHeader>\n"
            + "            <ns1:FromAgencyId>\n"
            + "                <ns1:AgencyId>" + msg.fromAgencyId + "</ns1:AgencyId>\n"
            + "            </ns1:FromAgencyId>\n"
            + "            <ns1:ToAgencyId>\n"
            + "                <ns1:AgencyId>" + msg.toAgencyId + "</ns1:AgencyId>\n"
            + "            </ns1:ToAgencyId>\n"
            + "        </ns1:InitiationHeader>\n"
            + "        <!-- The UserId must be unique in the scope of Home Library -->\n"
            + "        <ns1:UserId>\n"
            + "            <ns1:UserIdentifierValue>" + msg.userIdentifierValue
            + "</ns1:UserIdentifierValue>\n"
            + "        </ns1:UserId>\n"
            + "        <!-- NCIP demands use of either a ItemId or a BibliographicId. -->\n"
            + "        <!-- The BibliographicId must uniquely identify the requested Item in its own scope and "
            + " NCIP demands a RequesId when a BibliographicId is used -->\n"
            + "        <ns1:BibliographicId>\n"
            + "            <ns1:BibliographicRecordId>\n"
            + "                <ns1:BibliographicRecordIdentifier>" + msg.bibliographicRecordIdentifier
            + "</ns1:BibliographicRecordIdentifier>\n"
            + "         <!-- Supported BibliographicRecordIdentifierCode is OwnerLocalRecordID, ISBN, ISSN and"
            + " EAN -->\n"
            + "         <!-- Supported values of OwnerLocalRecordID is simplyfied to 'LocalId' - each system "
            + "know it's own values. -->\n"
            + "                <ns1:BibliographicRecordIdentifierCode>"
            + msg.bibliographicRecordIdentifierCode + "</ns1:BibliographicRecordIdentifierCode>\n"
            + "            </ns1:BibliographicRecordId>\n"
            + "        </ns1:BibliographicId>\n"
            + "        <ns1:RequestId>\n"
            + "            <!-- The Home library should fill out both AgencyId and generate a legal "
            + "RequestIdentifierValue -->\n"
            + "            <ns1:AgencyId/>\n"
            + "            <!-- The RequestIdentifierValue must be part of the RequestId-->\n"
            + "            <!-- This value should be empty; Home library should generate this value IF the "
            + "request is accepted and the Item is orded, see use-case 2 call #3 -->\n"
            + "            <ns1:RequestIdentifierValue/>\n"
            + "        </ns1:RequestId>\n"
            + "        <!-- The RequestId must be created by the initializing AgencyId and it has to be "
            + "globally unique -->\n"
            + "        <!-- The RequestType must be one of the following: -->\n"
            + "        <!-- Physical, a loan (of a physical item, create a reservation if not available) -->\n"
            + "        <!-- Non-Returnable, a copy of a physical item - that is not required to return -->\n"
            + "        <!-- PhysicalNoReservation, a loan (of a physical item), do NOT create a reservation if "
            + "not available -->\n"
            + "        <!-- LII, a patron initialized physical loan request, threat as a physical loan request "
            + "-->\n        <!-- LIINoReservation, a patron initialized physical loan request, do NOT create a"
            + " reservation if not available -->\n"
            + "        <!-- Depot, a border case; some libraries get a box of (foreign language) books from"
            + " the national library -->\n"
            + "        <!-- If your library don't receive 'Depot'-books; just respond with a \"Unknown Value "
            + "From Known Scheme\"-ProblemType -->\n"
            + "        <ns1:RequestType>" + msg.requestType + "</ns1:RequestType>\n"
            + "        <!-- RequestScopeType is mandatory and must be \"Title\", signaling that the request is"
            + " on title-level -->\n"
            + "        <!-- (and not Item-level - even though the request was on a Id that uniquely identify "
            + "the requested Item) -->\n"
            + "        <ns1:RequestScopeType>Title</ns1:RequestScopeType>\n"
            + "        <!-- Include ItemOptionalFields.BibliographicDescription if you wish to receive "
            + "Bibliographic data in the response -->\n"
            + "        <ns1:ItemOptionalFields>\n"
            + "            <ns1:BibliographicDescription>\n"
            + "                <ns1:Author>" + msg.author + "</ns1:Author>\n"
            + "                <ns1:PlaceOfPublication>" + msg.placeOfPublication
            + "</ns1:PlaceOfPublication>\n"
            + "                <ns1:PublicationDate>" + msg.publicationDate
            + "</ns1:PublicationDate>\n"
            + "                <ns1:Publisher>" + msg.publisher + "</ns1:Publisher>\n"
            + "                <ns1:Title>" + msg.title + "</ns1:Title>\n"
            + "                <ns1:BibliographicLevel>" + msg.type + "</ns1:BibliographicLevel>\n"
            + "                <ns1:MediumType>" + msg.type + "</ns1:MediumType>\n"
            + "            </ns1:BibliographicDescription>\n"
            + "        </ns1:ItemOptionalFields>\n"
            + "        <ns1:Ext>\n"
            + "            <ns1:ItemNote>" + msg.comment + "</ns1:ItemNote>\n"
            + "        </ns1:Ext>\n"
            + "    </ns1:ItemRequested>\n"
            + "</ns1:NCIPMessage>\n";
        return ncipXML;
    } 
    
    
    
    
}
