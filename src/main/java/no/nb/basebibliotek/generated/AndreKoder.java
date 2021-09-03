//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.09.03 at 08:06:05 AM CEST 
//


package no.nb.basebibliotek.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{http://nb.no/BaseBibliotek}bibkode_alt"/>
 *         &lt;element ref="{http://nb.no/BaseBibliotek}bibkode_gml"/>
 *         &lt;element ref="{http://nb.no/BaseBibliotek}bibnr_gml"/>
 *         &lt;element ref="{http://nb.no/BaseBibliotek}bibnr_midl"/>
 *         &lt;element ref="{http://nb.no/BaseBibliotek}bibsys_avd"/>
 *         &lt;element ref="{http://nb.no/BaseBibliotek}bibsys_bib"/>
 *         &lt;element ref="{http://nb.no/BaseBibliotek}bibsys_utl"/>
 *         &lt;element ref="{http://nb.no/BaseBibliotek}oclc_kode"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bibkodeAltOrBibkodeGmlOrBibnrGml"
})
@XmlRootElement(name = "andre_koder")
public class AndreKoder {

    @XmlElementRefs({
        @XmlElementRef(name = "bibsys_bib", namespace = "http://nb.no/BaseBibliotek", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "bibsys_avd", namespace = "http://nb.no/BaseBibliotek", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "oclc_kode", namespace = "http://nb.no/BaseBibliotek", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "bibnr_gml", namespace = "http://nb.no/BaseBibliotek", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "bibkode_gml", namespace = "http://nb.no/BaseBibliotek", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "bibnr_midl", namespace = "http://nb.no/BaseBibliotek", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "bibkode_alt", namespace = "http://nb.no/BaseBibliotek", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "bibsys_utl", namespace = "http://nb.no/BaseBibliotek", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<String>> bibkodeAltOrBibkodeGmlOrBibnrGml;

    /**
     * Gets the value of the bibkodeAltOrBibkodeGmlOrBibnrGml property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bibkodeAltOrBibkodeGmlOrBibnrGml property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBibkodeAltOrBibkodeGmlOrBibnrGml().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<String>> getBibkodeAltOrBibkodeGmlOrBibnrGml() {
        if (bibkodeAltOrBibkodeGmlOrBibnrGml == null) {
            bibkodeAltOrBibkodeGmlOrBibnrGml = new ArrayList<JAXBElement<String>>();
        }
        return this.bibkodeAltOrBibkodeGmlOrBibnrGml;
    }

}
