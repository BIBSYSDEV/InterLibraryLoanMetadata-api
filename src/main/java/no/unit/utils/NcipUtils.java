package no.unit.utils;

import no.unit.ncip.NcipTransferMessage;

public class NcipUtils {

    /**
     * Generates xml formattted NCIP-message.
     * @param msg NcipTransferMessage object that holds ncip data.
     * @return xml formatted NCIP-message
     */
    public static String ncipMessageAsXml(NcipTransferMessage msg) {
        StringBuilder ncipXML = new StringBuilder();
        ncipXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            .append("<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3")
            .append(".org/2001/XMLSchema-instance\"\n")
            .append("                 ns1:version=\"http://www.niso.org/schemas/ncip/v2_02/ncip_v2_02.xsd\"\n")
            .append("                 xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso")
            .append(".org/schemas/ncip/v2_02/ncip_v2_02.xsd\">\n")
            .append("<!--Usage in NNCIPP 1.0 is in use-case 3, call #8: A user request home library to order an item,")
            .append(" from a external library.  -->\n")
            .append("    <ns1:ItemRequested>\n")
            .append("        <!-- The InitiationHeader, stating from- and to-agency, is mandatory. -->\n")
            .append("        <ns1:InitiationHeader>\n")
            .append("            <ns1:FromAgencyId>\n")
            .append("                <ns1:AgencyId>").append(msg.fromAgencyId).append("</ns1:AgencyId>\n")
            .append("            </ns1:FromAgencyId>\n")
            .append("            <ns1:ToAgencyId>\n")
            .append("                <ns1:AgencyId>").append(msg.toAgencyId).append("</ns1:AgencyId>\n")
            .append("            </ns1:ToAgencyId>\n")
            .append("        </ns1:InitiationHeader>\n")
            .append("        <!-- The UserId must be unique in the scope of Home Library -->\n")
            .append("        <ns1:UserId>\n")
            .append("            <ns1:UserIdentifierValue>").append(msg.userIdentifierValue)
            .append("</ns1:UserIdentifierValue>\n")
            .append("        </ns1:UserId>\n")
            .append("        <!-- NCIP demands use of either a ItemId or a BibliographicId. -->\n")
            .append("        <!-- The BibliographicId must uniquely identify the requested Item in its own scope and ")
            .append(" NCIP demands a RequesId when a BibliographicId is used -->\n")
            .append("        <ns1:BibliographicId>\n")
            .append("            <ns1:BibliographicRecordId>\n")
            .append("                <ns1:BibliographicRecordIdentifier>").append(msg.bibliographicRecordIdentifier)
            .append("</ns1:BibliographicRecordIdentifier>\n")
            .append("         <!-- Supported BibliographicRecordIdentifierCode is OwnerLocalRecordID, ISBN, ISSN and")
            .append(" EAN -->\n")
            .append("         <!-- Supported values of OwnerLocalRecordID is simplyfied to 'LocalId' - each system ")
            .append("know it's own values. -->\n")
            .append("                <ns1:BibliographicRecordIdentifierCode>")
            .append(msg.bibliographicRecordIdentifierCode).append("</ns1:BibliographicRecordIdentifierCode>\n")
            .append("            </ns1:BibliographicRecordId>\n")
            .append("        </ns1:BibliographicId>\n")
            .append("        <ns1:RequestId>\n")
            .append("            <!-- The Home library should fill out both AgencyId and generate a legal ")
            .append("RequestIdentifierValue -->\n")
            .append("            <ns1:AgencyId/>\n")
            .append("            <!-- The RequestIdentifierValue must be part of the RequestId-->\n")
            .append("            <!-- This value should be empty; Home library should generate this value IF the ")
            .append("request is accepted and the Item is orded, see use-case 2 call #3 -->\n")
            .append("            <ns1:RequestIdentifierValue/>\n")
            .append("        </ns1:RequestId>\n")
            .append("        <!-- The RequestId must be created by the initializing AgencyId and it has to be ")
            .append("globally unique -->\n")
            .append("        <!-- The RequestType must be one of the following: -->\n")
            .append("        <!-- Physical, a loan (of a physical item, create a reservation if not available) -->\n")
            .append("        <!-- Non-Returnable, a copy of a physical item - that is not required to return -->\n")
            .append("        <!-- PhysicalNoReservation, a loan (of a physical item), do NOT create a reservation if ")
            .append("not available -->\n")
            .append("        <!-- LII, a patron initialized physical loan request, threat as a physical loan request ")
            .append("-->\n        <!-- LIINoReservation, a patron initialized physical loan request, do NOT create a")
            .append(" reservation if not available -->\n")
            .append("        <!-- Depot, a border case; some libraries get a box of (foreign language) books from")
            .append(" the national library -->\n")
            .append("        <!-- If your library don't receive 'Depot'-books; just respond with a \"Unknown Value ")
            .append("From Known Scheme\"-ProblemType -->\n")
            .append("        <ns1:RequestType>").append(msg.requestType).append("</ns1:RequestType>\n")
            .append("        <!-- RequestScopeType is mandatory and must be \"Title\", signaling that the request is")
            .append(" on title-level -->\n")
            .append("        <!-- (and not Item-level - even though the request was on a Id that uniquely identify ")
            .append("the requested Item) -->\n")
            .append("        <ns1:RequestScopeType>Title</ns1:RequestScopeType>\n")
            .append("        <!-- Include ItemOptionalFields.BibliographicDescription if you wish to receive ")
            .append("Bibliographic data in the response -->\n")
            .append("        <ns1:ItemOptionalFields>\n")
            .append("            <ns1:BibliographicDescription>\n")
            .append("                <ns1:Author>").append(msg.author).append("</ns1:Author>\n")
            .append("                <ns1:PlaceOfPublication>").append(msg.placeOfPublication)
            .append("</ns1:PlaceOfPublication>\n")
            .append("                <ns1:PublicationDate>").append(msg.publicationDate)
            .append("</ns1:PublicationDate>\n")
            .append("                <ns1:Publisher>").append(msg.publisher).append("</ns1:Publisher>\n")
            .append("                <ns1:Title>").append(msg.title).append("</ns1:Title>\n")
            .append("                <ns1:BibliographicLevel>").append(msg.type).append("</ns1:BibliographicLevel>\n")
            .append("                <ns1:MediumType>").append(msg.type).append("</ns1:MediumType>\n")
            .append("            </ns1:BibliographicDescription>\n")
            .append("        </ns1:ItemOptionalFields>\n")
            .append("        <ns1:Ext>\n")
            .append("            <ns1:ItemNote>").append(msg.comment).append("</ns1:ItemNote>\n")
            .append("        </ns1:Ext>\n")
            .append("    </ns1:ItemRequested>\n")
            .append("</ns1:NCIPMessage>\n");
        return ncipXML.toString();
    } 
    
    
    
    
}
