//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.10 at 03:06:42 PM COT 
//


package oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AdditionalAccountIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CustomerAssignedAccountIDType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.axteroid.ose.server.ubl20.gateway.request.MyNamespacePrefixMapper;


/**
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:qdt="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;ccts:ComponentType&gt;ABIE&lt;/ccts:ComponentType&gt;&lt;ccts:DictionaryEntryName&gt;Road Transport. Details&lt;/ccts:DictionaryEntryName&gt;&lt;ccts:Definition&gt;Describes a road transport vehicle.&lt;/ccts:Definition&gt;&lt;ccts:ObjectClass&gt;Road Transport&lt;/ccts:ObjectClass&gt;
 *
 *         &lt;/ccts:Component&gt;
 * </pre>
 * <p/>
 * <p/>
 * <p/>
 * <p>Java class for RoadTransportType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="RoadTransportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}LicensePlateID"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SUNATShipmentStage", propOrder = {
        "sunatCarrierParty"
})
public class SunatShipmentStageType {


    @XmlElement(name = "SUNATCarrierParty",namespace = MyNamespacePrefixMapper.SAC_URI)
    protected SunatCarrierPartyType sunatCarrierParty;

    public SunatCarrierPartyType getSunatCarrierParty() {
        return sunatCarrierParty;
    }

    public void setSunatCarrierParty(SunatCarrierPartyType sunatCarrierParty) {
        this.sunatCarrierParty = sunatCarrierParty;
    }
}
