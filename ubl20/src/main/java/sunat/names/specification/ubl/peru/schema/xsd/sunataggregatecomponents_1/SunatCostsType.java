//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.10 at 03:06:42 PM COT 
//


package sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.axteroid.ose.server.ubl20.gateway.request.MyNamespacePrefixMapper;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.RoadTransportType;


/**
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Component xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:qdt="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;ccts:ComponentType&gt;ABIE&lt;/ccts:ComponentType&gt;&lt;ccts:DictionaryEntryName&gt;Consolidated Invoice Line Details&lt;/ccts:DictionaryEntryName&gt;&lt;ccts:Definition&gt;Information about a Consolidated Invoice Line.&lt;/ccts:Definition&gt;&lt;ccts:ObjectClass&gt;Consolidated  Invoice Line&lt;/ccts:ObjectClass&gt;
 *
 * 				&lt;/ccts:Component&gt;
 * </pre>
 * <p/>
 * <p/>
 * <p/>
 * <p>Java class for AdditionalInformationType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="AdditionalInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1}AdditionalMonetaryTotal" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1}AdditionalProperty" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SunatCostsType", propOrder = {
        "roadTransport",
})
public class SunatCostsType {

    @XmlElement(name = "RoadTransport",namespace = MyNamespacePrefixMapper.CAC_URI)
    protected RoadTransportType roadTransport;

	public RoadTransportType getRoadTransport() {
		if(roadTransport==null){
			roadTransport = new RoadTransportType();
		}
		return roadTransport;
	}

	public void setRoadTransport(RoadTransportType roadTransport) {
		this.roadTransport = roadTransport;
	}
}
