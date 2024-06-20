//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacioasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.n de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderoasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.n si se vuelve a compilar el esquema de origen. 
// Generado el: 2017.10.03 a las 12:14:45 PM COT 
//


package oasis.names.specification.ubl.schema.xsd.applicationresponse_2_1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the oasis.names.specification.ubl.schema.xsd.applicationresponse_2 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ApplicationResponse_QNAME = new QName("urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2", "ApplicationResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: oasis.names.specification.ubl.schema.xsd.applicationresponse_2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ApplicationResponseType }
     * 
     */
    public ApplicationResponseType createApplicationResponseType() {
        return new ApplicationResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApplicationResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2", name = "ApplicationResponse")
    public JAXBElement<ApplicationResponseType> createApplicationResponse(ApplicationResponseType value) {
        return new JAXBElement<ApplicationResponseType>(_ApplicationResponse_QNAME, ApplicationResponseType.class, null, value);
    }

}
