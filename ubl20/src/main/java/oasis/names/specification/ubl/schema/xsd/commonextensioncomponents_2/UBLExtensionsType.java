//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.10 at 03:06:42 PM COT 
//


package oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DriverPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.MaritimeTransportType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.RoadTransportType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.SunatCarrierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DeliveryDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.GrossWeightMeasureType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.QuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ReturnabilityIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.StreetNameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TransportMeansTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TransportModeCodeType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalInformationType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalMonetaryTotalType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalPropertyType;


/**
 * A container for all extensions present in the document.
 * <p/>
 * <p/>
 * <p>Java class for UBLExtensionsType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="UBLExtensionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2}UBLExtension" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UBLExtensionsType", propOrder = {
        "ublExtension"
})
public class UBLExtensionsType {

    @XmlElement(name = "UBLExtension", required = true, namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2")
    protected List<UBLExtensionType> ublExtension;

    @XmlTransient
    protected UBLExtensionType ublExtensionPercepcion;

    @XmlTransient
    AdditionalPropertyType additionalPropertyType;

    @XmlTransient
    AdditionalMonetaryTotalType additionalMonetaryTotal;

    /**
     * A single extension for private use.
     * Gets the value of the ublExtension property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ublExtension property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUBLExtension().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link UBLExtensionType }
     */
    public List<UBLExtensionType> getUBLExtension() {
        if (ublExtension == null) {
            ublExtension = new ArrayList<UBLExtensionType>();
        }
        return this.ublExtension;
    }

    public void insertSunatOriginAddressType(AddressType addressType) {
        if(addressType==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();

        additionalInformationType.getSunatEmbededDespatchAdvice().setOriginAddress(addressType);
    }

    public void insertSunatTransactionType(IDType idType) {
        if(idType==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatTransactionType().setIdType(idType);
    }

    public void insertSunatDeliveryAddressType(AddressType addressType) {
        if(addressType==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatEmbededDespatchAdvice().setDeliveryAddress(addressType);
    }

    public void insertSunatRoadTransportType(RoadTransportType roadTransportType) {
        if(roadTransportType==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatEmbededDespatchAdvice().setSunatRoadTransport(roadTransportType);
    }
    public void insertSunatDriverPartyType(DriverPartyType value) {
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatEmbededDespatchAdvice().setDriverParty(value);
    }
//    public void insertSunatShipmentStageType(SunatShipmentStageType value) {
//        if(value==null) return;
//        AdditionalInformationType additionalInformationType = null;
//        UBLExtensionType current=getUblExtension("SUNAT");
//        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
//        additionalInformationType.getSunatEmbededDespatchAdvice().setSunatShipmentStageType(value);
//    }

    public void insertSunatCarrierPartyType(SunatCarrierPartyType value) {
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatEmbededDespatchAdvice().setSunatCarrierPartyType(value);
    }

    public void insertUBLExtensionType(AdditionalPropertyType additionalPropertyType) {
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getAdditionalProperty().add(additionalPropertyType);
    }

    public void insertUBLExtensionMoneratyType(AdditionalMonetaryTotalType additionalPropertyType) {
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getAdditionalMonetaryTotal().add(additionalPropertyType);
    }

    public void insertUBLExtensionTypeEbiz(AdditionalPropertyType additionalPropertyType) {
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("EBIZ");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getAdditionalProperty().add(additionalPropertyType);
    }

    public void insertUBLExtensionMoneratyTypeEbiz(AdditionalMonetaryTotalType additionalPropertyType) {
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("EBIZ");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getAdditionalMonetaryTotal().add(additionalPropertyType);
    }

    private UBLExtensionType buildUblExtension(String id) {
        AdditionalInformationType additionalInformationType = new AdditionalInformationType();
        ExtensionContentType extensionContentType = new ExtensionContentType();
        extensionContentType.setAny(additionalInformationType);
        UBLExtensionType ublExtensionType = new UBLExtensionType();
        IDType idType = new IDType();
        idType.setValue(id);
        ublExtensionType.setID(idType);
        ublExtensionType.setExtensionContent(extensionContentType);
        return ublExtensionType;
    }

    public UBLExtensionType getUblExtensionPercepcion() {
        return ublExtensionPercepcion;
    }

    public void setUblExtensionPercepcion(UBLExtensionType ublExtensionPercepcion) {
        this.ublExtensionPercepcion = ublExtensionPercepcion;
    }

    public AdditionalPropertyType getAdditionalPropertyType() {
        return additionalPropertyType;
    }

    public void setAdditionalPropertyType(AdditionalPropertyType additionalPropertyType) {
        this.additionalPropertyType = additionalPropertyType;
    }

    public AdditionalMonetaryTotalType getAdditionalMonetaryTotal() {
        return additionalMonetaryTotal;
    }

    public void setAdditionalMonetaryTotal(AdditionalMonetaryTotalType additionalMonetaryTotal) {
        this.additionalMonetaryTotal = additionalMonetaryTotal;
    }

    private UBLExtensionType getUblExtension(String id){
        List<UBLExtensionType> ubl = getUBLExtension();
        UBLExtensionType current=null;
        for (UBLExtensionType anUbl : ubl) {
            if (id.equals(anUbl.getID().getValue())) {
                current = anUbl;
                break;
            }
        }
        if (current==null) {
            current=buildUblExtension(id);
            getUBLExtension().add(current);
        }

        return current;
    }
    
    
    //HNA - 30OCt2015: RS185
    public void insertTransportModeCodeType(TransportModeCodeType value) {
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatEmbededDespatchAdvice().setTransportModeCodeType(value);
    }
    
    public void insertGrossWeightMeasureType(GrossWeightMeasureType value) {
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatEmbededDespatchAdvice().setGrossWeightMeasureType(value);
    }
    //TODO: si agregan constancia y marca adaptar insertRoadTransportType
    public void insertSunatCostsType(RoadTransportType value) {
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatCosts().setRoadTransport(value);
    }
    
    public void insertMaritimeTransportType(MaritimeTransportType value) {
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatAquaticResources().setMaritimeTransport(value);
    }
    
    public void insertDescriptionType(DescriptionType value) {
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatAquaticResources().setDescription(value);
    }
    
    public void insertStreetNameType(StreetNameType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatAquaticResources().setStreetName(value);
    }
    
    public void insertDeliveryDateType(DeliveryDateType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatAquaticResources().setDeliveryDate(value);
    }
    
    public void insertSunatReferenceValueAmount(AmountType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatReferenceValue().setAmount(value);
    }
    
    public void insertSunatValueTravelAmount(AmountType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatReferenceValue().getSunatValueTravel().setAmount(value);
    }
    
    public void insertReturnabilityIndicatorType(ReturnabilityIndicatorType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatReferenceValue().getSunatValueTravel().setReturnabilityIndicator(value);
    }
    
    public void insertSunatValueTravelOrigin(StreetNameType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatReferenceValue().getSunatValueTravel().getOriginAddress().setStreetName(value);
    }
    
    public void insertSunatValueTravelDelivery(StreetNameType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatReferenceValue().getSunatValueTravel().getDeliveryAddress().setStreetName(value);
    }
    
    public void insertSunatValueVehicleQuantity(QuantityType value) {
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatReferenceValue().getSunatValueTravel().getSunatValueVehicle().setQuantity(value);
    }
    
    public void insertSunatValueVehicleAmount(AmountType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatReferenceValue().getSunatValueTravel().getSunatValueVehicle().setAmount(value);
    }
    
    public void insertSunatTransportMeansTypeCode(TransportMeansTypeCodeType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatReferenceValue().
        getSunatValueTravel().getSunatValueVehicle().getSunatTransport().setTransportMeansTypeCode(value);
    }
    
    public void insertSunatTransportQuantity(QuantityType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatReferenceValue().
        getSunatValueTravel().getSunatValueVehicle().getSunatTransport().setQuantity(value);
    }
    
    public void insertSunatTransporAmount(AmountType value){
        if(value==null) return;
        AdditionalInformationType additionalInformationType = null;
        UBLExtensionType current=getUblExtension("SUNAT");
        additionalInformationType = (AdditionalInformationType) current.getExtensionContent().getAny();
        additionalInformationType.getSunatDeductions().getSunatReferenceValue().
        getSunatValueTravel().getSunatValueVehicle().getSunatTransport().setAmount(value);
    }
}