package com.axteroid.ose.server.tools.edocu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.ubltype.TypeParty;
import com.axteroid.ose.server.tools.util.NumberUtil;
import com.axteroid.ose.server.tools.util.StringUtil;
import com.axteroid.ose.server.tools.validation.TipoDocumentoIdentidadValidatorTypeRetencion;
import com.axteroid.ose.server.tools.validation.TipoDocumentoRetencionValidate;
import com.axteroid.ose.server.tools.validation.TipoMonedaValidatorType;
import com.axteroid.ose.server.tools.validation.TipoRegimenRetencionValidatorType;
import com.axteroid.ose.server.tools.validation.ValidateFormatNumber;
import com.axteroid.ose.server.tools.validation.ValidateFormatNumberMayorCero;
import com.axteroid.ose.server.tools.validation.ValidateSerieRetention;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ERetencionDocumento implements IDocumento, IEDocumento {
	
	//RETENCION ELECTRONICA /Retention
    /**
     * parametro
     * cbc:UBLVersionID
     */
	public String versionUBL = Constantes.SUNAT_UBL_20;
	
    /**
     * parametro
     * cbc:CustomizationID
     */
    public String version = Constantes.SUNAT_CUSTOMIZA_10;
    
    @NotBlank(message = "7010")
    @Length(max = 100, message = "7069")
    public String correoEmisor;
    
    @NotBlank(message = "7021")
    @Length(max = 100, message = "7071")
    public String correoAdquiriente;
    
    public Integer inHabilitado = 1;
    
    public String coTipoEmision;    
	
    /**
     * parametro
     * cbc:ID
     */
    @NotBlank(message = "7003")
    @ValidateSerieRetention(message = "7225", groups = DocumentRetencion.class)
    public String serieNumeroRetencion;
    
    /**
     * parametro
     * cbc:IssueDate
     */
    @NotNull(message = "7037")
    public Date fechaEmision;
    
    /**
     * parametro
     * TODO - no existe en la guia de elaboracion - considerar validaciones
     */

    @NotBlank(message="7277")
    @TipoDocumentoRetencionValidate(message="7278")
    public String tipoDocumento;    
    
    public String rucRazonSocialEmisor;
    
    /**
     * parametro
     * cac:AgentParty/cac::PartyIdentification/cbc:ID
     */
    @NotBlank(message = "7001")
    @Length(max = 11, message = "7063")
    public String numeroDocumentoEmisor;
    
    /**
     * parametro
     * cac:AgentParty/cac::PartyIdentification/cbc:ID@schemeID
     */
    @NotBlank(message = "7000")
    @TipoDocumentoIdentidadValidatorTypeRetencion(message = "7227")
    public String tipoDocumentoEmisor;
    
    /**
     * parametro
     * cac:AgentParty/cac:PartyName/cbc:Name
     */
    @Length(max = 100, message = "7059")
    public String nombreComercialEmisor;
    
    /**
     * parametro
     * cac:AgentParty/cac:PostalAddress/cbc:ID
     */
//    @NotBlank(message = "7170",groups = DocumentNote.class) // RAC 20/05/2015
    @Length(max = 6, message = "7056")
    public String ubigeoEmisor;

    /**
     * parametro
     * cac:AgentParty/cac:PostalAddress/cbc:StreetName
     */
//    @NotBlank(message = "7005",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 100, message = "7057")
    public String direccionEmisor;

    /**
     * parametro
     * cac:AgentParty/cac:PostalAddress/cbc:CitySubdivisionName
     */
//    @NotBlank(message = "7171",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 30, message = "7065")
    public String urbanizacionEmisor;

    /**
     * parametro
     * cac:AgentParty/cac:PostalAddress/cbc:CityName
     */
//    @NotBlank(message = "7006",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 30, message = "7068")
    public String provinciaEmisor;

    /**
     * parametro
     * cac:AgentParty/cac:PostalAddress/cbc:CountrySubentity
     */
//    @NotBlank(message = "7007",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 30, message = "7066")
    public String departamentoEmisor;

    /**
     * parametro
     * cac:AgentParty/cac:PostalAddress/cbc:District
     */
//    @NotBlank(message = "7008",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 30, message = "7067")
    public String distritoEmisor;

    /**
     * parametro
     * cac:AgentParty/cac:PostalAddress/cac:Country/cbc:IdentificationCode
     */

//    @NotBlank(message = "7009",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 2, message = "7055")
    public String codigoPaisEmisor;
        
    /**
     * parametro
     * cac:AgentParty/cac:PartyLegalEntity/cbc:RegistrationName
     */
    @NotBlank(message = "7011")
    @Length(max = 100, message = "7062")
    public String razonSocialEmisor;
    
    /**
     * parametro
     * cac:ReceiverParty/cac:PartyIdentification/cbc:ID
     */
    @NotBlank(message = "7019", groups = {DocumentInvoice.class, DocumentNote.class, DocumentSummary.class, DocumentVoid.class})
    @Length(max = 15, message = "7064")
    public String numeroDocumentoProveedor;

    /**
     * parametro
     * cac:ReceiverParty/cac:PartyIdentification/cbc:ID@schemeID
     * obs: la valdiacion debe ser mas especifica
     */
   // @TipoDocumentoIdentidadValidatorType(message = "7016")
    @NotBlank(message = "7016", groups = {DocumentInvoice.class, DocumentNote.class, DocumentSummary.class, DocumentVoid.class})
    public String tipoDocumentoProveedor;
    
    /**
     * parametro
     * cac:ReceiverParty/cac:PartyName/cbc:Name
     */
    @Length(max = 100, message = "7059")
    public String nombreComercialProveedor;
    
    /**
     * parametro
     * cac:ReceiverParty/cac:PostalAddress/cbc:ID
     */
//    @NotBlank(message = "7170",groups = DocumentNote.class) // RAC 20/05/2015
    @Length(max = 6, message = "7056")
    public String ubigeoProveedor;

    /**
     * parametro
     * cac:ReceiverParty/cac:PostalAddress/cbc:StreetName
     */
//    @NotBlank(message = "7005",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 100, message = "7057")
    public String direccionProveedor;

    /**
     * parametro
     * cac:ReceiverParty/cac:PostalAddress/cbc:CitySubdivisionName
     */
//    @NotBlank(message = "7171",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 30, message = "7065")
    public String urbanizacionProveedor;

    /**
     * parametro
     * cac:ReceiverParty/cac:PostalAddress/cbc:CityName
     */
//    @NotBlank(message = "7006",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 30, message = "7068")
    public String provinciaProveedor;

    /**
     * parametro
     * cac:ReceiverParty/cac:PostalAddress/cbc:CountrySubentity
     */
//    @NotBlank(message = "7007",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 30, message = "7066")
    public String departamentoProveedor;

    /**
     * parametro
     * cac:ReceiverParty/cac:PostalAddress/cbc:District
     */
//    @NotBlank(message = "7008",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 30, message = "7067")
    public String distritoProveedor;

    /**
     * parametro
     * cac:ReceiverParty/cac:PostalAddress/cac:Country/cbc:IdentificationCode
     */

//    @NotBlank(message = "7009",groups = DocumentNote.class)// RAC 20/05/2015
    @Length(max = 2, message = "7055")
    public String codigoPaisProveedor;
        
    /**
     * parametro
     * cac:ReceiverParty/cac:PartyLegalEntity/cbc:RegistrationName
     */
    @NotBlank(message = "7011")
    @Length(max = 100, message = "7062")
    public String razonSocialProveedor;
    
    /**
     * parametro
     * sac:SUNATRetentionSystemCode
     */
    @NotBlank(message = "7228")
    @TipoRegimenRetencionValidatorType(message = "7229")
    public String regimenRetencion;
    
    /**
     * parametro
     * sac:SUNATRetentionPercent
     */
    @NotNull(message = "7230")
    public BigDecimal tasaRetencion;
    
    /**
     * parametro
     * cbc:Note
     * Dato: campo condicional
     */
	public String observaciones;
	
    /**
     * parametro
     * cbc:TotalInvoiceAmount
     */	
	@NotNull(message = "7231")
	@ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
	public BigDecimal importeTotalRetenido;
	
    /**
     * parametro
     * cbc:TotalInvoiceAmount@currencyID
     */		
	@NotBlank(message = "7232")
	@TipoMonedaValidatorType(message="7233")
	public String tipoMonedaTotalRetenido;
	
    /**
     * parametro
     * cbc:TotalPaid
     */		
	@NotNull(message = "7234")
	@ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
	@ValidateFormatNumberMayorCero(message = "7402")
	public BigDecimal importeTotalPagado;
	
    /**
     * parametro
     * cbc:TotalPaid@currencyID
     */		
	@NotBlank(message = "7232")
	@TipoMonedaValidatorType(message="7233")
	public String tipoMonedaTotalPagado;
	
    @Length(max = 4, message = "7145|codigoAuxiliar100")
    private String codigoAuxiliar100_1;
    @Length(max = 100, message = "7155|textoAuxiliar100")
    private String textoAuxiliar100_1;
    @Length(max = 4, message = "7145|codigoAuxiliar100")
    private String codigoAuxiliar100_2;
    @Length(max = 100, message = "7155|textoAuxiliar100")
    private String textoAuxiliar100_2;
    @Length(max = 4, message = "7145|codigoAuxiliar100")
    private String codigoAuxiliar100_3;
    @Length(max = 100, message = "7155|textoAuxiliar100")
    private String textoAuxiliar100_3;
    @Length(max = 4, message = "7145|codigoAuxiliar100")
    private String codigoAuxiliar100_4;
    @Length(max = 100, message = "7155|textoAuxiliar100")
    private String textoAuxiliar100_4;
    @Length(max = 4, message = "7145|codigoAuxiliar100")
    private String codigoAuxiliar100_5;
    @Length(max = 100, message = "7155|textoAuxiliar100")
    private String textoAuxiliar100_5;
    @Length(max = 4, message = "7145|codigoAuxiliar100")
    private String codigoAuxiliar100_6;
    @Length(max = 100, message = "7155|textoAuxiliar100")
    private String textoAuxiliar100_6;
    @Length(max = 4, message = "7145|codigoAuxiliar100")
    private String codigoAuxiliar100_7;
    @Length(max = 100, message = "7155|textoAuxiliar100")
    private String textoAuxiliar100_7;
    @Length(max = 4, message = "7145|codigoAuxiliar100")
    private String codigoAuxiliar100_8;
    @Length(max = 100, message = "7155|textoAuxiliar100")
    private String textoAuxiliar100_8;
    @Length(max = 4, message = "7145|codigoAuxiliar100")
    private String codigoAuxiliar100_9;
    @Length(max = 100, message = "7155|textoAuxiliar100")
    private String textoAuxiliar100_9;
    @Length(max = 4, message = "7145|codigoAuxiliar100")
    private String codigoAuxiliar100_10;
    @Length(max = 100, message = "7155|textoAuxiliar100")
    private String textoAuxiliar100_10;
    
    @Length(max = 4, message = "7145|codigoAuxiliar40")
    private String codigoAuxiliar40_1;
    @Length(max = 40, message = "7155|textoAuxiliar40")
    private String textoAuxiliar40_1;
    @Length(max = 4, message = "7145|codigoAuxiliar40")
    private String codigoAuxiliar40_2;
    @Length(max = 40, message = "7155|textoAuxiliar40")
    private String textoAuxiliar40_2;
    @Length(max = 4, message = "7145|codigoAuxiliar40")
    private String codigoAuxiliar40_3;
    @Length(max = 40, message = "7155|textoAuxiliar40")
    private String textoAuxiliar40_3;
    @Length(max = 4, message = "7145|codigoAuxiliar40")
    private String codigoAuxiliar40_4;
    @Length(max = 40, message = "7155|textoAuxiliar40")
    private String textoAuxiliar40_4;
    @Length(max = 4, message = "7145|codigoAuxiliar40")
    private String codigoAuxiliar40_5;
    @Length(max = 40, message = "7155|textoAuxiliar40")
    private String textoAuxiliar40_5;
    @Length(max = 4, message = "7145|codigoAuxiliar40")
    private String codigoAuxiliar40_6;
    @Length(max = 40, message = "7155|textoAuxiliar40")
    private String textoAuxiliar40_6;
    @Length(max = 4, message = "7145|codigoAuxiliar40")
    private String codigoAuxiliar40_7;
    @Length(max = 40, message = "7155|textoAuxiliar40")
    private String textoAuxiliar40_7;
    @Length(max = 4, message = "7145|codigoAuxiliar40")
    private String codigoAuxiliar40_8;
    @Length(max = 40, message = "7155|textoAuxiliar40")
    private String textoAuxiliar40_8;
    @Length(max = 4, message = "7145|codigoAuxiliar40")
    private String codigoAuxiliar40_9;
    @Length(max = 40, message = "7155|textoAuxiliar40")
    private String textoAuxiliar40_9;
    @Length(max = 4, message = "7145|codigoAuxiliar40")
    private String codigoAuxiliar40_10;
    @Length(max = 40, message = "7155|textoAuxiliar40")
    private String textoAuxiliar40_10;
    
    @Length(max = 4, message = "7145|codigoAuxiliar500")
    private String codigoAuxiliar500_1;
    @Length(max = 500, message = "7155|textoAuxiliar500")
    private String textoAuxiliar500_1;
    @Length(max = 4, message = "7145|codigoAuxiliar500")
    private String codigoAuxiliar500_2;
    @Length(max = 500, message = "7155|textoAuxiliar500")
    private String textoAuxiliar500_2;
    @Length(max = 4, message = "7145|codigoAuxiliar500")
    private String codigoAuxiliar500_3;
    @Length(max = 500, message = "7155|textoAuxiliar500")
    private String textoAuxiliar500_3;
    @Length(max = 4, message = "7145|codigoAuxiliar500")
    private String codigoAuxiliar500_4;
    @Length(max = 500, message = "7155|textoAuxiliar500")
    private String textoAuxiliar500_4;
    @Length(max = 4, message = "7145|codigoAuxiliar500")
    private String codigoAuxiliar500_5;
    @Length(max = 500, message = "7155|textoAuxiliar500")
    private String textoAuxiliar500_5;    
    @Length(max = 4, message = "7145|codigoAuxiliar500")
    private String codigoAuxiliar500_6;
    @Length(max = 500, message = "7155|textoAuxiliar500")
    private String textoAuxiliar500_6;
    @Length(max = 4, message = "7145|codigoAuxiliar500")
    private String codigoAuxiliar500_7;
    @Length(max = 500, message = "7155|textoAuxiliar500")
    private String textoAuxiliar500_7;
    @Length(max = 4, message = "7145|codigoAuxiliar500")
    private String codigoAuxiliar500_8;
    @Length(max = 500, message = "7155|textoAuxiliar500")
    private String textoAuxiliar500_8;
    @Length(max = 4, message = "7145|codigoAuxiliar500")
    private String codigoAuxiliar500_9;
    @Length(max = 500, message = "7155|textoAuxiliar500")
    private String textoAuxiliar500_9;
    @Length(max = 4, message = "7145|codigoAuxiliar500")
    private String codigoAuxiliar500_10;
    @Length(max = 500, message = "7155|textoAuxiliar500")
    private String textoAuxiliar500_10;
    
    @Length(max = 4, message = "7145|codigoAuxiliar250")
    private String codigoAuxiliar250_1;
    @Length(max = 250, message = "7155|textoAuxiliar250")
    private String textoAuxiliar250_1;
    @Length(max = 4, message = "7145|codigoAuxiliar250")
    private String codigoAuxiliar250_2;
    @Length(max = 250, message = "7155|textoAuxiliar250")
    private String textoAuxiliar250_2;
    @Length(max = 4, message = "7145|codigoAuxiliar250")
    private String codigoAuxiliar250_3;
    @Length(max = 250, message = "7155|textoAuxiliar250")
    private String textoAuxiliar250_3;
    @Length(max = 4, message = "7145|codigoAuxiliar250")
    private String codigoAuxiliar250_4;
    @Length(max = 250, message = "7155|textoAuxiliar250")
    private String textoAuxiliar250_4;
    @Length(max = 4, message = "7145|codigoAuxiliar250")
    private String codigoAuxiliar250_5;
    @Length(max = 250, message = "7155|textoAuxiliar250")
    private String textoAuxiliar250_5;
    @Length(max = 4, message = "7145|codigoAuxiliar250")
    private String codigoAuxiliar250_6;
    @Length(max = 250, message = "7155|textoAuxiliar250")
    private String textoAuxiliar250_6;
    @Length(max = 4, message = "7145|codigoAuxiliar250")
    private String codigoAuxiliar250_7;
    @Length(max = 250, message = "7155|textoAuxiliar250")
    private String textoAuxiliar250_7;
    @Length(max = 4, message = "7145|codigoAuxiliar250")
    private String codigoAuxiliar250_8;
    @Length(max = 250, message = "7155|textoAuxiliar250")
    private String textoAuxiliar250_8;
    @Length(max = 4, message = "7145|codigoAuxiliar250")
    private String codigoAuxiliar250_9;
    @Length(max = 250, message = "7155|textoAuxiliar250")
    private String textoAuxiliar250_9;
    @Length(max = 4, message = "7145|codigoAuxiliar250")
    private String codigoAuxiliar250_10;
    @Length(max = 250, message = "7155|textoAuxiliar250")
    private String textoAuxiliar250_10;    
	
    //ITEMS
    @NotEmpty(message = "7013")
    @Valid
    public List<ERetencionDocumentoItem> items = new ArrayList<ERetencionDocumentoItem>();
    
    protected TypeParty agentParty;
    protected TypeParty receiverParty; 
    private String hashCode;
    private String numIdCd;
    private Date dategetNotBefore;
    private Date dateNotAfter;
    
	public String getVersionUBL() {
		return versionUBL;
	}

	public void setVersionUBL(String versionUBL) {
		this.versionUBL = versionUBL;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCorreoEmisor() {
		return correoEmisor;
	}

	public void setCorreoEmisor(String correoEmisor) {
		this.correoEmisor = correoEmisor;
	}

	public String getCorreoAdquiriente() {
		return correoAdquiriente;
	}

	public void setCorreoAdquiriente(String correoAdquiriente) {
		this.correoAdquiriente = correoAdquiriente;
	}

	public Integer getInHabilitado() {
		return inHabilitado;
	}

	public void setInHabilitado(Integer inHabilitado) {
		this.inHabilitado = inHabilitado;
	}

	public String getCoTipoEmision() {
		return coTipoEmision;
	}

	public void setCoTipoEmision(String coTipoEmision) {
		this.coTipoEmision = coTipoEmision;
	}

	public String getSerieNumeroRetencion() {
		return serieNumeroRetencion;
	}

	public void setSerieNumeroRetencion(String serieNumeroRetencion) {
		this.serieNumeroRetencion = serieNumeroRetencion;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getNumeroDocumentoEmisor() {
		return numeroDocumentoEmisor;
	}

	public void setNumeroDocumentoEmisor(String numeroDocumentoEmisor) {
		this.numeroDocumentoEmisor = numeroDocumentoEmisor;
	}

	public String getTipoDocumentoEmisor() {
		return tipoDocumentoEmisor;
	}

	public String getRucRazonSocialEmisor() {
		return numeroDocumentoEmisor + "|" + razonSocialEmisor;
	}
	
	public void setTipoDocumentoEmisor(String tipoDocumentoEmisor) {
		this.tipoDocumentoEmisor = tipoDocumentoEmisor;
	}

	public String getNombreComercialEmisor() {
		return nombreComercialEmisor;
	}

	public void setNombreComercialEmisor(String nombreComercialEmisor) {
		this.nombreComercialEmisor = nombreComercialEmisor;
	}

	public String getUbigeoEmisor() {
		return ubigeoEmisor;
	}

	public void setUbigeoEmisor(String ubigeoEmisor) {
		this.ubigeoEmisor = ubigeoEmisor;
	}

	public String getDireccionEmisor() {
		return direccionEmisor;
	}

	public void setDireccionEmisor(String direccionEmisor) {
		this.direccionEmisor = direccionEmisor;
	}

	public String getUrbanizacionEmisor() {
		return urbanizacionEmisor;
	}

	public void setUrbanizacionEmisor(String urbanizacionEmisor) {
		this.urbanizacionEmisor = urbanizacionEmisor;
	}

	public String getProvinciaEmisor() {
		return provinciaEmisor;
	}

	public void setProvinciaEmisor(String provinciaEmisor) {
		this.provinciaEmisor = provinciaEmisor;
	}

	public String getDepartamentoEmisor() {
		return departamentoEmisor;
	}

	public void setDepartamentoEmisor(String departamentoEmisor) {
		this.departamentoEmisor = departamentoEmisor;
	}

	public String getDistritoEmisor() {
		return distritoEmisor;
	}

	public void setDistritoEmisor(String distritoEmisor) {
		this.distritoEmisor = distritoEmisor;
	}

	public String getCodigoPaisEmisor() {
		return codigoPaisEmisor;
	}

	public void setCodigoPaisEmisor(String codigoPaisEmisor) {
		this.codigoPaisEmisor = codigoPaisEmisor;
	}

	public String getRazonSocialEmisor() {
		return razonSocialEmisor;
	}

	public void setRazonSocialEmisor(String razonSocialEmisor) {
		this.razonSocialEmisor = razonSocialEmisor;
	}

	public String getNumeroDocumentoProveedor() {
		return numeroDocumentoProveedor;
	}

	public void setNumeroDocumentoProveedor(String numeroDocumentoProveedor) {
		this.numeroDocumentoProveedor = numeroDocumentoProveedor;
	}

	public String getTipoDocumentoProveedor() {
		return tipoDocumentoProveedor;
	}

	public void setTipoDocumentoProveedor(String tipoDocumentoProveedor) {
		this.tipoDocumentoProveedor = tipoDocumentoProveedor;
	}

	public String getNombreComercialProveedor() {
		return nombreComercialProveedor;
	}

	public void setNombreComercialProveedor(String nombreComercialProveedor) {
		this.nombreComercialProveedor = nombreComercialProveedor;
	}

	public String getUbigeoProveedor() {
		return ubigeoProveedor;
	}

	public void setUbigeoProveedor(String ubigeoProveedor) {
		this.ubigeoProveedor = ubigeoProveedor;
	}

	public String getDireccionProveedor() {
		return direccionProveedor;
	}

	public void setDireccionProveedor(String direccionProveedor) {
		this.direccionProveedor = direccionProveedor;
	}

	public String getUrbanizacionProveedor() {
		return urbanizacionProveedor;
	}

	public void setUrbanizacionProveedor(String urbanizacionProveedor) {
		this.urbanizacionProveedor = urbanizacionProveedor;
	}

	public String getProvinciaProveedor() {
		return provinciaProveedor;
	}

	public void setProvinciaProveedor(String provinciaProveedor) {
		this.provinciaProveedor = provinciaProveedor;
	}

	public String getDepartamentoProveedor() {
		return departamentoProveedor;
	}

	public void setDepartamentoProveedor(String departamentoProveedor) {
		this.departamentoProveedor = departamentoProveedor;
	}

	public String getDistritoProveedor() {
		return distritoProveedor;
	}

	public void setDistritoProveedor(String distritoProveedor) {
		this.distritoProveedor = distritoProveedor;
	}

	public String getCodigoPaisProveedor() {
		return codigoPaisProveedor;
	}

	public void setCodigoPaisProveedor(String codigoPaisProveedor) {
		this.codigoPaisProveedor = codigoPaisProveedor;
	}

	public String getRazonSocialProveedor() {
		return razonSocialProveedor;
	}

	public void setRazonSocialProveedor(String razonSocialProveedor) {
		this.razonSocialProveedor = razonSocialProveedor;
	}

	public String getRegimenRetencion() {
		return regimenRetencion;
	}

	public void setRegimenRetencion(String regimenRetencion) {
		this.regimenRetencion = regimenRetencion;
	}

	public BigDecimal getTasaRetencion() {
		return tasaRetencion;
	}

	public void setTasaRetencion(BigDecimal tasaRetencion) {
		this.tasaRetencion = tasaRetencion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public BigDecimal getImporteTotalRetenido() {
		return importeTotalRetenido;
	}

	public void setImporteTotalRetenido(BigDecimal importeTotalRetenido) {
		this.importeTotalRetenido = importeTotalRetenido;
	}

	public String getTipoMonedaTotalRetenido() {
		return tipoMonedaTotalRetenido;
	}

	public void setTipoMonedaTotalRetenido(String tipoMonedaTotalRetenido) {
		this.tipoMonedaTotalRetenido = tipoMonedaTotalRetenido;
	}

	public BigDecimal getImporteTotalPagado() {
		return importeTotalPagado;
	}

	public void setImporteTotalPagado(BigDecimal importeTotalPagado) {
		this.importeTotalPagado = importeTotalPagado;
	}

	public String getTipoMonedaTotalPagado() {
		return tipoMonedaTotalPagado;
	}

	public void setTipoMonedaTotalPagado(String tipoMonedaTotalPagado) {
		this.tipoMonedaTotalPagado = tipoMonedaTotalPagado;
	}

	public List<ERetencionDocumentoItem> getItems() {
		return items;
	}

	public void setItems(List<ERetencionDocumentoItem> items) {
		this.items = items;
	}

	public void populateItem() {
        if (StringUtils.isBlank(getTipoDocumento())) {
            setTipoDocumento("SN");
        }
        if (StringUtils.isBlank(getSerieNumeroRetencion())) {
            setSerieNumeroRetencion("SN" + new Date().getTime());
        }
	}

	public String getIdDocumento() {
		return getSerieNumeroRetencion();
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}
	
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

	public String getSerieDocumento() {
		return getIdDocumento().substring(0, getIdDocumento().lastIndexOf('-'));
	}

	public Date getFechaDocumento() {
		return getFechaEmision();
	}

	public String getNumeroDocumento() {
		return getIdDocumento().substring(getIdDocumento().lastIndexOf('-') + 1, getIdDocumento().length());
	}

	public String getEstado() {
		return null;  //Todo implementar
	}

/*	public String getHashCode() {
		return null;
	}*/

	public String getSignatureValue() {
		return null;
	}

	public String getTipoDocumentoAdquiriente() {
		return getTipoDocumentoProveedor();
	}

	public String getNumeroDocumentoAdquiriente() {
		return getNumeroDocumentoProveedor();
	}

	public String getRazonSocialAdquiriente() {
		return getRazonSocialProveedor();
	}

	public boolean isExportacion() {
		return false;
	}

	public boolean isNotaDebitoPorPenalidad() {
		return false;
	}
	
    public String buildEmisor() {
        if (StringUtils.isBlank(numeroDocumentoEmisor) ||
                StringUtils.isBlank(tipoDocumentoEmisor)) {
            return null;
        }
        return StringUtil.blank(numeroDocumentoEmisor) + "|" + StringUtil.blank(tipoDocumentoEmisor);
    }
    
    public String buildProveedor() {
        if (StringUtils.isBlank(numeroDocumentoProveedor) ||
                StringUtils.isBlank(tipoDocumentoProveedor)) {
            return null;
        }
        return StringUtil.blank(numeroDocumentoProveedor) + "|" + StringUtil.blank(tipoDocumentoProveedor);
    }
    
    public String buildTotalRetenido(){
        if (StringUtils.isBlank(tipoMonedaTotalRetenido) ||
        		importeTotalRetenido == null) return null;
        return tipoMonedaTotalRetenido + "|" + NumberUtil.toFormat(importeTotalRetenido);
    }
    
    public String buildTotalPagado(){
        if (StringUtils.isBlank(tipoMonedaTotalPagado) ||
        		importeTotalPagado == null) return null;
        return tipoMonedaTotalPagado + "|" + NumberUtil.toFormat(importeTotalPagado);
    }

	public String getCodigoAuxiliar100_1() {
		return codigoAuxiliar100_1;
	}

	public void setCodigoAuxiliar100_1(String codigoAuxiliar100_1) {
		this.codigoAuxiliar100_1 = codigoAuxiliar100_1;
	}

	public String getTextoAuxiliar100_1() {
		return textoAuxiliar100_1;
	}

	public void setTextoAuxiliar100_1(String textoAuxiliar100_1) {
		this.textoAuxiliar100_1 = textoAuxiliar100_1;
	}
    
    public String getCodigoAuxiliar100_2() {
		return codigoAuxiliar100_2;
	}

	public void setCodigoAuxiliar100_2(String codigoAuxiliar100_2) {
		this.codigoAuxiliar100_2 = codigoAuxiliar100_2;
	}

	public String getTextoAuxiliar100_2() {
		return textoAuxiliar100_2;
	}

	public void setTextoAuxiliar100_2(String textoAuxiliar100_2) {
		this.textoAuxiliar100_2 = textoAuxiliar100_2;
	}

	public String getCodigoAuxiliar100_3() {
		return codigoAuxiliar100_3;
	}

	public void setCodigoAuxiliar100_3(String codigoAuxiliar100_3) {
		this.codigoAuxiliar100_3 = codigoAuxiliar100_3;
	}

	public String getTextoAuxiliar100_3() {
		return textoAuxiliar100_3;
	}

	public void setTextoAuxiliar100_3(String textoAuxiliar100_3) {
		this.textoAuxiliar100_3 = textoAuxiliar100_3;
	}

	public String getCodigoAuxiliar100_4() {
		return codigoAuxiliar100_4;
	}

	public void setCodigoAuxiliar100_4(String codigoAuxiliar100_4) {
		this.codigoAuxiliar100_4 = codigoAuxiliar100_4;
	}

	public String getTextoAuxiliar100_4() {
		return textoAuxiliar100_4;
	}

	public void setTextoAuxiliar100_4(String textoAuxiliar100_4) {
		this.textoAuxiliar100_4 = textoAuxiliar100_4;
	}

	public String getCodigoAuxiliar100_5() {
		return codigoAuxiliar100_5;
	}

	public void setCodigoAuxiliar100_5(String codigoAuxiliar100_5) {
		this.codigoAuxiliar100_5 = codigoAuxiliar100_5;
	}

	public String getTextoAuxiliar100_5() {
		return textoAuxiliar100_5;
	}

	public void setTextoAuxiliar100_5(String textoAuxiliar100_5) {
		this.textoAuxiliar100_5 = textoAuxiliar100_5;
	}

	public String getCodigoAuxiliar100_6() {
		return codigoAuxiliar100_6;
	}

	public void setCodigoAuxiliar100_6(String codigoAuxiliar100_6) {
		this.codigoAuxiliar100_6 = codigoAuxiliar100_6;
	}

	public String getTextoAuxiliar100_6() {
		return textoAuxiliar100_6;
	}

	public void setTextoAuxiliar100_6(String textoAuxiliar100_6) {
		this.textoAuxiliar100_6 = textoAuxiliar100_6;
	}

	public String getCodigoAuxiliar100_7() {
		return codigoAuxiliar100_7;
	}

	public void setCodigoAuxiliar100_7(String codigoAuxiliar100_7) {
		this.codigoAuxiliar100_7 = codigoAuxiliar100_7;
	}

	public String getTextoAuxiliar100_7() {
		return textoAuxiliar100_7;
	}

	public void setTextoAuxiliar100_7(String textoAuxiliar100_7) {
		this.textoAuxiliar100_7 = textoAuxiliar100_7;
	}

	public String getCodigoAuxiliar100_8() {
		return codigoAuxiliar100_8;
	}

	public void setCodigoAuxiliar100_8(String codigoAuxiliar100_8) {
		this.codigoAuxiliar100_8 = codigoAuxiliar100_8;
	}

	public String getTextoAuxiliar100_8() {
		return textoAuxiliar100_8;
	}

	public void setTextoAuxiliar100_8(String textoAuxiliar100_8) {
		this.textoAuxiliar100_8 = textoAuxiliar100_8;
	}

	public String getCodigoAuxiliar100_9() {
		return codigoAuxiliar100_9;
	}

	public void setCodigoAuxiliar100_9(String codigoAuxiliar100_9) {
		this.codigoAuxiliar100_9 = codigoAuxiliar100_9;
	}

	public String getTextoAuxiliar100_9() {
		return textoAuxiliar100_9;
	}

	public void setTextoAuxiliar100_9(String textoAuxiliar100_9) {
		this.textoAuxiliar100_9 = textoAuxiliar100_9;
	}

	public String getCodigoAuxiliar100_10() {
		return codigoAuxiliar100_10;
	}

	public void setCodigoAuxiliar100_10(String codigoAuxiliar100_10) {
		this.codigoAuxiliar100_10 = codigoAuxiliar100_10;
	}

	public String getTextoAuxiliar100_10() {
		return textoAuxiliar100_10;
	}

	public void setTextoAuxiliar100_10(String textoAuxiliar100_10) {
		this.textoAuxiliar100_10 = textoAuxiliar100_10;
	}

	public String getCodigoAuxiliar40_1() {
		return codigoAuxiliar40_1;
	}

	public void setCodigoAuxiliar40_1(String codigoAuxiliar40_1) {
		this.codigoAuxiliar40_1 = codigoAuxiliar40_1;
	}

	public String getTextoAuxiliar40_1() {
		return textoAuxiliar40_1;
	}

	public void setTextoAuxiliar40_1(String textoAuxiliar40_1) {
		this.textoAuxiliar40_1 = textoAuxiliar40_1;
	}

	public String getCodigoAuxiliar40_2() {
		return codigoAuxiliar40_2;
	}

	public void setCodigoAuxiliar40_2(String codigoAuxiliar40_2) {
		this.codigoAuxiliar40_2 = codigoAuxiliar40_2;
	}

	public String getTextoAuxiliar40_2() {
		return textoAuxiliar40_2;
	}

	public void setTextoAuxiliar40_2(String textoAuxiliar40_2) {
		this.textoAuxiliar40_2 = textoAuxiliar40_2;
	}

	public String getCodigoAuxiliar40_3() {
		return codigoAuxiliar40_3;
	}

	public void setCodigoAuxiliar40_3(String codigoAuxiliar40_3) {
		this.codigoAuxiliar40_3 = codigoAuxiliar40_3;
	}

	public String getTextoAuxiliar40_3() {
		return textoAuxiliar40_3;
	}

	public void setTextoAuxiliar40_3(String textoAuxiliar40_3) {
		this.textoAuxiliar40_3 = textoAuxiliar40_3;
	}

	public String getCodigoAuxiliar40_4() {
		return codigoAuxiliar40_4;
	}

	public void setCodigoAuxiliar40_4(String codigoAuxiliar40_4) {
		this.codigoAuxiliar40_4 = codigoAuxiliar40_4;
	}

	public String getTextoAuxiliar40_4() {
		return textoAuxiliar40_4;
	}

	public void setTextoAuxiliar40_4(String textoAuxiliar40_4) {
		this.textoAuxiliar40_4 = textoAuxiliar40_4;
	}

	public String getCodigoAuxiliar40_5() {
		return codigoAuxiliar40_5;
	}

	public void setCodigoAuxiliar40_5(String codigoAuxiliar40_5) {
		this.codigoAuxiliar40_5 = codigoAuxiliar40_5;
	}

	public String getTextoAuxiliar40_5() {
		return textoAuxiliar40_5;
	}

	public void setTextoAuxiliar40_5(String textoAuxiliar40_5) {
		this.textoAuxiliar40_5 = textoAuxiliar40_5;
	}

	public String getCodigoAuxiliar40_6() {
		return codigoAuxiliar40_6;
	}

	public void setCodigoAuxiliar40_6(String codigoAuxiliar40_6) {
		this.codigoAuxiliar40_6 = codigoAuxiliar40_6;
	}

	public String getTextoAuxiliar40_6() {
		return textoAuxiliar40_6;
	}

	public void setTextoAuxiliar40_6(String textoAuxiliar40_6) {
		this.textoAuxiliar40_6 = textoAuxiliar40_6;
	}

	public String getCodigoAuxiliar40_7() {
		return codigoAuxiliar40_7;
	}

	public void setCodigoAuxiliar40_7(String codigoAuxiliar40_7) {
		this.codigoAuxiliar40_7 = codigoAuxiliar40_7;
	}

	public String getTextoAuxiliar40_7() {
		return textoAuxiliar40_7;
	}

	public void setTextoAuxiliar40_7(String textoAuxiliar40_7) {
		this.textoAuxiliar40_7 = textoAuxiliar40_7;
	}

	public String getCodigoAuxiliar40_8() {
		return codigoAuxiliar40_8;
	}

	public void setCodigoAuxiliar40_8(String codigoAuxiliar40_8) {
		this.codigoAuxiliar40_8 = codigoAuxiliar40_8;
	}

	public String getTextoAuxiliar40_8() {
		return textoAuxiliar40_8;
	}

	public void setTextoAuxiliar40_8(String textoAuxiliar40_8) {
		this.textoAuxiliar40_8 = textoAuxiliar40_8;
	}

	public String getCodigoAuxiliar40_9() {
		return codigoAuxiliar40_9;
	}

	public void setCodigoAuxiliar40_9(String codigoAuxiliar40_9) {
		this.codigoAuxiliar40_9 = codigoAuxiliar40_9;
	}

	public String getTextoAuxiliar40_9() {
		return textoAuxiliar40_9;
	}

	public void setTextoAuxiliar40_9(String textoAuxiliar40_9) {
		this.textoAuxiliar40_9 = textoAuxiliar40_9;
	}

	public String getCodigoAuxiliar40_10() {
		return codigoAuxiliar40_10;
	}

	public void setCodigoAuxiliar40_10(String codigoAuxiliar40_10) {
		this.codigoAuxiliar40_10 = codigoAuxiliar40_10;
	}

	public String getTextoAuxiliar40_10() {
		return textoAuxiliar40_10;
	}

	public void setTextoAuxiliar40_10(String textoAuxiliar40_10) {
		this.textoAuxiliar40_10 = textoAuxiliar40_10;
	}

	public String getCodigoAuxiliar500_1() {
		return codigoAuxiliar500_1;
	}

	public void setCodigoAuxiliar500_1(String codigoAuxiliar500_1) {
		this.codigoAuxiliar500_1 = codigoAuxiliar500_1;
	}

	public String getTextoAuxiliar500_1() {
		return textoAuxiliar500_1;
	}

	public void setTextoAuxiliar500_1(String textoAuxiliar500_1) {
		this.textoAuxiliar500_1 = textoAuxiliar500_1;
	}

	public String getCodigoAuxiliar500_2() {
		return codigoAuxiliar500_2;
	}

	public void setCodigoAuxiliar500_2(String codigoAuxiliar500_2) {
		this.codigoAuxiliar500_2 = codigoAuxiliar500_2;
	}

	public String getTextoAuxiliar500_2() {
		return textoAuxiliar500_2;
	}

	public void setTextoAuxiliar500_2(String textoAuxiliar500_2) {
		this.textoAuxiliar500_2 = textoAuxiliar500_2;
	}

	public String getCodigoAuxiliar500_3() {
		return codigoAuxiliar500_3;
	}

	public void setCodigoAuxiliar500_3(String codigoAuxiliar500_3) {
		this.codigoAuxiliar500_3 = codigoAuxiliar500_3;
	}

	public String getTextoAuxiliar500_3() {
		return textoAuxiliar500_3;
	}

	public void setTextoAuxiliar500_3(String textoAuxiliar500_3) {
		this.textoAuxiliar500_3 = textoAuxiliar500_3;
	}

	public String getCodigoAuxiliar500_4() {
		return codigoAuxiliar500_4;
	}

	public void setCodigoAuxiliar500_4(String codigoAuxiliar500_4) {
		this.codigoAuxiliar500_4 = codigoAuxiliar500_4;
	}

	public String getTextoAuxiliar500_4() {
		return textoAuxiliar500_4;
	}

	public void setTextoAuxiliar500_4(String textoAuxiliar500_4) {
		this.textoAuxiliar500_4 = textoAuxiliar500_4;
	}

	public String getCodigoAuxiliar500_5() {
		return codigoAuxiliar500_5;
	}

	public void setCodigoAuxiliar500_5(String codigoAuxiliar500_5) {
		this.codigoAuxiliar500_5 = codigoAuxiliar500_5;
	}

	public String getTextoAuxiliar500_5() {
		return textoAuxiliar500_5;
	}

	public void setTextoAuxiliar500_5(String textoAuxiliar500_5) {
		this.textoAuxiliar500_5 = textoAuxiliar500_5;
	}

	public String getCodigoAuxiliar500_6() {
		return codigoAuxiliar500_6;
	}

	public void setCodigoAuxiliar500_6(String codigoAuxiliar500_6) {
		this.codigoAuxiliar500_6 = codigoAuxiliar500_6;
	}

	public String getTextoAuxiliar500_6() {
		return textoAuxiliar500_6;
	}

	public void setTextoAuxiliar500_6(String textoAuxiliar500_6) {
		this.textoAuxiliar500_6 = textoAuxiliar500_6;
	}

	public String getCodigoAuxiliar500_7() {
		return codigoAuxiliar500_7;
	}

	public void setCodigoAuxiliar500_7(String codigoAuxiliar500_7) {
		this.codigoAuxiliar500_7 = codigoAuxiliar500_7;
	}

	public String getTextoAuxiliar500_7() {
		return textoAuxiliar500_7;
	}

	public void setTextoAuxiliar500_7(String textoAuxiliar500_7) {
		this.textoAuxiliar500_7 = textoAuxiliar500_7;
	}

	public String getCodigoAuxiliar500_8() {
		return codigoAuxiliar500_8;
	}

	public void setCodigoAuxiliar500_8(String codigoAuxiliar500_8) {
		this.codigoAuxiliar500_8 = codigoAuxiliar500_8;
	}

	public String getTextoAuxiliar500_8() {
		return textoAuxiliar500_8;
	}

	public void setTextoAuxiliar500_8(String textoAuxiliar500_8) {
		this.textoAuxiliar500_8 = textoAuxiliar500_8;
	}

	public String getCodigoAuxiliar500_9() {
		return codigoAuxiliar500_9;
	}

	public void setCodigoAuxiliar500_9(String codigoAuxiliar500_9) {
		this.codigoAuxiliar500_9 = codigoAuxiliar500_9;
	}

	public String getTextoAuxiliar500_9() {
		return textoAuxiliar500_9;
	}

	public void setTextoAuxiliar500_9(String textoAuxiliar500_9) {
		this.textoAuxiliar500_9 = textoAuxiliar500_9;
	}

	public String getCodigoAuxiliar500_10() {
		return codigoAuxiliar500_10;
	}

	public void setCodigoAuxiliar500_10(String codigoAuxiliar500_10) {
		this.codigoAuxiliar500_10 = codigoAuxiliar500_10;
	}

	public String getTextoAuxiliar500_10() {
		return textoAuxiliar500_10;
	}

	public void setTextoAuxiliar500_10(String textoAuxiliar500_10) {
		this.textoAuxiliar500_10 = textoAuxiliar500_10;
	}

	public String getCodigoAuxiliar250_1() {
		return codigoAuxiliar250_1;
	}

	public void setCodigoAuxiliar250_1(String codigoAuxiliar250_1) {
		this.codigoAuxiliar250_1 = codigoAuxiliar250_1;
	}

	public String getTextoAuxiliar250_1() {
		return textoAuxiliar250_1;
	}

	public void setTextoAuxiliar250_1(String textoAuxiliar250_1) {
		this.textoAuxiliar250_1 = textoAuxiliar250_1;
	}

	public String getCodigoAuxiliar250_2() {
		return codigoAuxiliar250_2;
	}

	public void setCodigoAuxiliar250_2(String codigoAuxiliar250_2) {
		this.codigoAuxiliar250_2 = codigoAuxiliar250_2;
	}

	public String getTextoAuxiliar250_2() {
		return textoAuxiliar250_2;
	}

	public void setTextoAuxiliar250_2(String textoAuxiliar250_2) {
		this.textoAuxiliar250_2 = textoAuxiliar250_2;
	}

	public String getCodigoAuxiliar250_3() {
		return codigoAuxiliar250_3;
	}

	public void setCodigoAuxiliar250_3(String codigoAuxiliar250_3) {
		this.codigoAuxiliar250_3 = codigoAuxiliar250_3;
	}

	public String getTextoAuxiliar250_3() {
		return textoAuxiliar250_3;
	}

	public void setTextoAuxiliar250_3(String textoAuxiliar250_3) {
		this.textoAuxiliar250_3 = textoAuxiliar250_3;
	}

	public String getCodigoAuxiliar250_4() {
		return codigoAuxiliar250_4;
	}

	public void setCodigoAuxiliar250_4(String codigoAuxiliar250_4) {
		this.codigoAuxiliar250_4 = codigoAuxiliar250_4;
	}

	public String getTextoAuxiliar250_4() {
		return textoAuxiliar250_4;
	}

	public void setTextoAuxiliar250_4(String textoAuxiliar250_4) {
		this.textoAuxiliar250_4 = textoAuxiliar250_4;
	}

	public String getCodigoAuxiliar250_5() {
		return codigoAuxiliar250_5;
	}

	public void setCodigoAuxiliar250_5(String codigoAuxiliar250_5) {
		this.codigoAuxiliar250_5 = codigoAuxiliar250_5;
	}

	public String getTextoAuxiliar250_5() {
		return textoAuxiliar250_5;
	}

	public void setTextoAuxiliar250_5(String textoAuxiliar250_5) {
		this.textoAuxiliar250_5 = textoAuxiliar250_5;
	}

	public String getCodigoAuxiliar250_6() {
		return codigoAuxiliar250_6;
	}

	public void setCodigoAuxiliar250_6(String codigoAuxiliar250_6) {
		this.codigoAuxiliar250_6 = codigoAuxiliar250_6;
	}

	public String getTextoAuxiliar250_6() {
		return textoAuxiliar250_6;
	}

	public void setTextoAuxiliar250_6(String textoAuxiliar250_6) {
		this.textoAuxiliar250_6 = textoAuxiliar250_6;
	}

	public String getCodigoAuxiliar250_7() {
		return codigoAuxiliar250_7;
	}

	public void setCodigoAuxiliar250_7(String codigoAuxiliar250_7) {
		this.codigoAuxiliar250_7 = codigoAuxiliar250_7;
	}

	public String getTextoAuxiliar250_7() {
		return textoAuxiliar250_7;
	}

	public void setTextoAuxiliar250_7(String textoAuxiliar250_7) {
		this.textoAuxiliar250_7 = textoAuxiliar250_7;
	}

	public String getCodigoAuxiliar250_8() {
		return codigoAuxiliar250_8;
	}

	public void setCodigoAuxiliar250_8(String codigoAuxiliar250_8) {
		this.codigoAuxiliar250_8 = codigoAuxiliar250_8;
	}

	public String getTextoAuxiliar250_8() {
		return textoAuxiliar250_8;
	}

	public void setTextoAuxiliar250_8(String textoAuxiliar250_8) {
		this.textoAuxiliar250_8 = textoAuxiliar250_8;
	}

	public String getCodigoAuxiliar250_9() {
		return codigoAuxiliar250_9;
	}

	public void setCodigoAuxiliar250_9(String codigoAuxiliar250_9) {
		this.codigoAuxiliar250_9 = codigoAuxiliar250_9;
	}

	public String getTextoAuxiliar250_9() {
		return textoAuxiliar250_9;
	}

	public void setTextoAuxiliar250_9(String textoAuxiliar250_9) {
		this.textoAuxiliar250_9 = textoAuxiliar250_9;
	}

	public String getCodigoAuxiliar250_10() {
		return codigoAuxiliar250_10;
	}

	public void setCodigoAuxiliar250_10(String codigoAuxiliar250_10) {
		this.codigoAuxiliar250_10 = codigoAuxiliar250_10;
	}

	public String getTextoAuxiliar250_10() {
		return textoAuxiliar250_10;
	}

	public void setTextoAuxiliar250_10(String textoAuxiliar250_10) {
		this.textoAuxiliar250_10 = textoAuxiliar250_10;
	}

	public String getAuxiliar100_1() {
        if (StringUtils.isBlank(codigoAuxiliar100_1) || StringUtils.isBlank(textoAuxiliar100_1)) return null;
        return codigoAuxiliar100_1 + "|" + textoAuxiliar100_1;
    }
	
    public String getAuxiliar100_2() {
        if (StringUtils.isBlank(codigoAuxiliar100_2) || StringUtils.isBlank(textoAuxiliar100_2)) return null;
        return codigoAuxiliar100_2 + "|" + textoAuxiliar100_2;
    }

    public String getAuxiliar100_3() {
        if (StringUtils.isBlank(codigoAuxiliar100_3) || StringUtils.isBlank(textoAuxiliar100_3)) return null;
        return codigoAuxiliar100_3 + "|" + textoAuxiliar100_3;
    }

    public String getAuxiliar100_4() {
        if (StringUtils.isBlank(codigoAuxiliar100_4) || StringUtils.isBlank(textoAuxiliar100_4)) return null;
        return codigoAuxiliar100_4 + "|" + textoAuxiliar100_4;
    }

    public String getAuxiliar100_5() {
        if (StringUtils.isBlank(codigoAuxiliar100_5) || StringUtils.isBlank(textoAuxiliar100_5)) return null;
        return codigoAuxiliar100_5 + "|" + textoAuxiliar100_5;
    }

    public String getAuxiliar100_6() {
        if (StringUtils.isBlank(codigoAuxiliar100_6) || StringUtils.isBlank(textoAuxiliar100_6)) return null;
        return codigoAuxiliar100_6 + "|" + textoAuxiliar100_6;
    }

    public String getAuxiliar100_7() {
        if (StringUtils.isBlank(codigoAuxiliar100_7) || StringUtils.isBlank(textoAuxiliar100_7)) return null;
        return codigoAuxiliar100_7 + "|" + textoAuxiliar100_7;
    }

    public String getAuxiliar100_8() {
        if (StringUtils.isBlank(codigoAuxiliar100_8) || StringUtils.isBlank(textoAuxiliar100_8)) return null;
        return codigoAuxiliar100_8 + "|" + textoAuxiliar100_8;
    }

    public String getAuxiliar100_9() {
        if (StringUtils.isBlank(codigoAuxiliar100_9) || StringUtils.isBlank(textoAuxiliar100_9)) return null;
        return codigoAuxiliar100_9 + "|" + textoAuxiliar100_9;
    }

    public String getAuxiliar100_10() {
        if (StringUtils.isBlank(codigoAuxiliar100_10) || StringUtils.isBlank(textoAuxiliar100_10)) return null;
        return codigoAuxiliar100_10 + "|" + textoAuxiliar100_10;
    }
    
    public String getAuxiliar40_1() {
        if (StringUtils.isBlank(codigoAuxiliar40_1) || StringUtils.isBlank(textoAuxiliar40_1)) return null;
        return codigoAuxiliar40_1 + "|" + textoAuxiliar40_1;
    }

    public String getAuxiliar40_2() {
        if (StringUtils.isBlank(codigoAuxiliar40_2) || StringUtils.isBlank(textoAuxiliar40_2)) return null;
        return codigoAuxiliar40_2 + "|" + textoAuxiliar40_2;
    }

    public String getAuxiliar40_3() {
        if (StringUtils.isBlank(codigoAuxiliar40_3) || StringUtils.isBlank(textoAuxiliar40_3)) return null;
        return codigoAuxiliar40_3 + "|" + textoAuxiliar40_3;
    }

    public String getAuxiliar40_4() {
        if (StringUtils.isBlank(codigoAuxiliar40_4) || StringUtils.isBlank(textoAuxiliar40_4)) return null;
        return codigoAuxiliar40_4 + "|" + textoAuxiliar40_4;
    }

    public String getAuxiliar40_5() {
        if (StringUtils.isBlank(codigoAuxiliar40_5) || StringUtils.isBlank(textoAuxiliar40_5)) return null;
        return codigoAuxiliar40_5 + "|" + textoAuxiliar40_5;
    }

    public String getAuxiliar40_6() {
        if (StringUtils.isBlank(codigoAuxiliar40_6) || StringUtils.isBlank(textoAuxiliar40_6)) return null;
        return codigoAuxiliar40_6 + "|" + textoAuxiliar40_6;
    }

    public String getAuxiliar40_7() {
        if (StringUtils.isBlank(codigoAuxiliar40_7) || StringUtils.isBlank(textoAuxiliar40_7)) return null;
        return codigoAuxiliar40_7 + "|" + textoAuxiliar40_7;
    }

    public String getAuxiliar40_8() {
        if (StringUtils.isBlank(codigoAuxiliar40_8) || StringUtils.isBlank(textoAuxiliar40_8)) return null;
        return codigoAuxiliar40_8 + "|" + textoAuxiliar40_8;
    }

    public String getAuxiliar40_9() {
        if (StringUtils.isBlank(codigoAuxiliar40_9) || StringUtils.isBlank(textoAuxiliar40_9)) return null;
        return codigoAuxiliar40_9 + "|" + textoAuxiliar40_9;
    }

    public String getAuxiliar40_10() {
        if (StringUtils.isBlank(codigoAuxiliar40_10) || StringUtils.isBlank(textoAuxiliar40_10)) return null;
        return codigoAuxiliar40_10 + "|" + textoAuxiliar40_10;
    }
    
    public String getAuxiliar500_1() {
        if (StringUtils.isBlank(codigoAuxiliar500_1) || StringUtils.isBlank(textoAuxiliar500_1)) return null;
        return codigoAuxiliar500_1 + "|" + textoAuxiliar500_1;
    }

    public String getAuxiliar500_2() {
        if (StringUtils.isBlank(codigoAuxiliar500_2) || StringUtils.isBlank(textoAuxiliar500_2)) return null;
        return codigoAuxiliar500_2 + "|" + textoAuxiliar500_2;
    }

    public String getAuxiliar500_3() {
        if (StringUtils.isBlank(codigoAuxiliar500_3) || StringUtils.isBlank(textoAuxiliar500_3)) return null;
        return codigoAuxiliar500_3 + "|" + textoAuxiliar500_3;
    }

    public String getAuxiliar500_4() {
        if (StringUtils.isBlank(codigoAuxiliar500_4) || StringUtils.isBlank(textoAuxiliar500_4)) return null;
        return codigoAuxiliar500_4 + "|" + textoAuxiliar500_4;
    }

    public String getAuxiliar500_5() {
        if (StringUtils.isBlank(codigoAuxiliar500_5) || StringUtils.isBlank(textoAuxiliar500_5)) return null;
        return codigoAuxiliar500_5 + "|" + textoAuxiliar500_5;
    }
    
    public String getAuxiliar500_6() {
        if (StringUtils.isBlank(codigoAuxiliar500_6) || StringUtils.isBlank(textoAuxiliar500_6)) return null;
        return codigoAuxiliar500_6 + "|" + textoAuxiliar500_6;
    }

    public String getAuxiliar500_7() {
        if (StringUtils.isBlank(codigoAuxiliar500_7) || StringUtils.isBlank(textoAuxiliar500_7)) return null;
        return codigoAuxiliar500_7 + "|" + textoAuxiliar500_7;
    }

    public String getAuxiliar500_8() {
        if (StringUtils.isBlank(codigoAuxiliar500_8) || StringUtils.isBlank(textoAuxiliar500_8)) return null;
        return codigoAuxiliar500_8 + "|" + textoAuxiliar500_8;
    }

    public String getAuxiliar500_9() {
        if (StringUtils.isBlank(codigoAuxiliar500_9) || StringUtils.isBlank(textoAuxiliar500_9)) return null;
        return codigoAuxiliar500_9 + "|" + textoAuxiliar500_9;
    }

    public String getAuxiliar500_10() {
        if (StringUtils.isBlank(codigoAuxiliar500_10) || StringUtils.isBlank(textoAuxiliar500_10)) return null;
        return codigoAuxiliar500_10 + "|" + textoAuxiliar500_10;
    }
    
    public String getAuxiliar250_1() {
        if (StringUtils.isBlank(codigoAuxiliar250_1) || StringUtils.isBlank(textoAuxiliar250_1)) return null;
        return codigoAuxiliar250_1 + "|" + textoAuxiliar250_1;
    }

    public String getAuxiliar250_2() {
        if (StringUtils.isBlank(codigoAuxiliar250_2) || StringUtils.isBlank(textoAuxiliar250_2)) return null;
        return codigoAuxiliar250_2 + "|" + textoAuxiliar250_2;
    }

    public String getAuxiliar250_3() {
        if (StringUtils.isBlank(codigoAuxiliar250_3) || StringUtils.isBlank(textoAuxiliar250_3)) return null;
        return codigoAuxiliar250_3 + "|" + textoAuxiliar250_3;
    }

    public String getAuxiliar250_4() {
        if (StringUtils.isBlank(codigoAuxiliar250_4) || StringUtils.isBlank(textoAuxiliar250_4)) return null;
        return codigoAuxiliar250_4 + "|" + textoAuxiliar250_4;
    }

    public String getAuxiliar250_5() {
        if (StringUtils.isBlank(codigoAuxiliar250_5) || StringUtils.isBlank(textoAuxiliar250_5)) return null;
        return codigoAuxiliar250_5 + "|" + textoAuxiliar250_5;
    }

    public String getAuxiliar250_6() {
        if (StringUtils.isBlank(codigoAuxiliar250_6) || StringUtils.isBlank(textoAuxiliar250_6)) return null;
        return codigoAuxiliar250_6 + "|" + textoAuxiliar250_6;
    }

    public String getAuxiliar250_7() {
        if (StringUtils.isBlank(codigoAuxiliar250_7) || StringUtils.isBlank(textoAuxiliar250_7)) return null;
        return codigoAuxiliar250_7 + "|" + textoAuxiliar250_7;
    }

    public String getAuxiliar250_8() {
        if (StringUtils.isBlank(codigoAuxiliar250_8) || StringUtils.isBlank(textoAuxiliar250_8)) return null;
        return codigoAuxiliar250_8 + "|" + textoAuxiliar250_8;
    }

    public String getAuxiliar250_9() {
        if (StringUtils.isBlank(codigoAuxiliar250_9) || StringUtils.isBlank(textoAuxiliar250_9)) return null;
        return codigoAuxiliar250_9 + "|" + textoAuxiliar250_9;
    }

    public String getAuxiliar250_10() {
        if (StringUtils.isBlank(codigoAuxiliar250_10) || StringUtils.isBlank(textoAuxiliar250_10)) return null;
        return codigoAuxiliar250_10 + "|" + textoAuxiliar250_10;
    }

	public TypeParty getAgentParty() {
		return agentParty;
	}

	public void setAgentParty(TypeParty agentParty) {
		this.agentParty = agentParty;
	}

	public TypeParty getReceiverParty() {
		return receiverParty;
	}

	public void setReceiverParty(TypeParty receiverParty) {
		this.receiverParty = receiverParty;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public String getNumIdCd() {
		return numIdCd;
	}

	public void setNumIdCd(String numIdCd) {
		this.numIdCd = numIdCd;
	}

	public Date getDategetNotBefore() {
		return dategetNotBefore;
	}

	public void setDategetNotBefore(Date dategetNotBefore) {
		this.dategetNotBefore = dategetNotBefore;
	}

	public Date getDateNotAfter() {
		return dateNotAfter;
	}

	public void setDateNotAfter(Date dateNotAfter) {
		this.dateNotAfter = dateNotAfter;
	}
}
