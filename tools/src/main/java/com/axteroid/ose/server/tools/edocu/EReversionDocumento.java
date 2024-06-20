package com.axteroid.ose.server.tools.edocu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.axteroid.ose.server.tools.validation.ValidateSerieRR;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class EReversionDocumento implements IDocumento,IEDocumento {

    /**
     * parametro
     * cac:AccountingSupplierParty/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName
     */
	public String razonSocialEmisor;
    /**
     * parametro
     * cac:AccountingSupplierParty/cbc:CustomerAssignedAccountID
     */	
	public String numeroRucEmisor;
    /**
     * parametro
     * cac:AccountingSupplierParty/cbc:AdditionalAccountID
     */	
	public String tipoDocumentoEmisor; //siempre 6
    /**
     * parametro
     * cbc:ReferenceDate
     */	
	public Date fechaDocumentoRevertido;
    /**
     * parametro
     * cbc:ID
     */		
	@ValidateSerieRR(message = "7285")
	public String serieNumeroReversion;
    /**
     * parametro
     * cbc:IssueDate
     */		
	public Date fechaGeneracionReversion;
    /**
     * parametro
     * cbc:UBLVersionID
     */	
	public String versionUBL = "2.0";
    /**
     * parametro
     * cbc:CustomizationID
     */	
	public String version = "1.0";

	public String reversionTipo;
	public String correoEmisor;
    public String correoAdquiriente;
    public Integer inHabilitado = 1;
    public String coTipoEmision;
    
    public String rucRazonSocialEmisor;
	
    private String hashCode;
    private String numIdCd;
    private Date dategetNotBefore;
    private Date dateNotAfter;
    
    @NotEmpty(message = "7013")
    @Valid
    List<EReversionDocumentoItem> items = new ArrayList<EReversionDocumentoItem>();
	
	public String getRazonSocialEmisor() {
		return razonSocialEmisor;
	}
	public void setRazonSocialEmisor(String razonSocialEmisor) {
		this.razonSocialEmisor = razonSocialEmisor;
	}
	public String getNumeroRucEmisor() {
		return numeroRucEmisor;
	}
	public void setNumeroRucEmisor(String numeroRucEmisor) {
		this.numeroRucEmisor = numeroRucEmisor;
	}
	public String getTipoDocumentoEmisor() {		
		return tipoDocumentoEmisor;
	}	
	public void setTipoDocumentoEmisor(String tipoDocumentoEmisor) {
		this.tipoDocumentoEmisor = tipoDocumentoEmisor;
	}
	public Date getFechaDocumentoRevertido() {
		return fechaDocumentoRevertido;
	}
	public void setFechaDocumentoRevertido(Date fechaDocumentoRevertido) {
		this.fechaDocumentoRevertido = fechaDocumentoRevertido;
	}
	public String getSerieNumeroReversion() {
		return serieNumeroReversion;
	}
	public void setSerieNumeroReversion(String serieNumeroReversion) {
		this.serieNumeroReversion = serieNumeroReversion;
	}
	public Date getFechaEmisionreversion() {
		return fechaGeneracionReversion;
	}
	public void setFechaEmisionreversion(Date fechaGeneracionReversion) {
		this.fechaGeneracionReversion = fechaGeneracionReversion;
	}
	public Date getFechaGeneracionReversion() {
		return fechaGeneracionReversion;
	}
	public void setFechaGeneracionReversion(Date fechaGeneracionReversion) {
		this.fechaGeneracionReversion = fechaGeneracionReversion;
	}
	public String getReversionTipo() {
		return reversionTipo;
	}
	public void setReversionTipo(String reversionTipo) {
		this.reversionTipo = reversionTipo;
	}
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
	public void populateItem() {
        if (StringUtils.isBlank(getReversionTipo())) {
            setReversionTipo("SN");
        }
        if (StringUtils.isBlank(getSerieNumeroReversion())) {
            setSerieNumeroReversion("SN" + new Date().getTime());
        }
	}	
	public List<EReversionDocumentoItem> getItems() {
		return items;
	}
	public void setItems(List<EReversionDocumentoItem> items) {
		this.items = items;
	}
	public String getNumeroDocumentoEmisor() {
		return this.getNumeroRucEmisor();
	}
	public String getIdDocumento() {
		return this.getSerieNumeroReversion();
	}
	public String getTipoDocumento() {
		return this.getReversionTipo();
	}
	public Date getFechaDocumento() {
		return this.getFechaGeneracionReversion();
	}

	public String getSerieDocumento() {
		return null;
	}
	public String getNumeroDocumento() {
		return null;
	}
	public String getEstado() {
		return null;
	}
/*	public String getHashCode() {
		return null;
	}*/
	public String getSignatureValue() {
		return null;
	}
	public String getTipoDocumentoAdquiriente() {
		return null;
	}
	public String getNumeroDocumentoAdquiriente() {
		return null;
	}
	public String getRazonSocialAdquiriente() {
		return null;
	}
	public boolean isExportacion() {
		return false;
	}
	public boolean isNotaDebitoPorPenalidad() {
		return false;
	}
    public String getRucRazonSocialEmisor() {
        return numeroRucEmisor + "|" + razonSocialEmisor;
    }
    public void setRucRazonSocialEmisor(String rucRazonSocialEmisor) {
        this.rucRazonSocialEmisor = rucRazonSocialEmisor;
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
