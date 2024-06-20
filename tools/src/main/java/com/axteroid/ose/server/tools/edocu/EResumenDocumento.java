package com.axteroid.ose.server.tools.edocu;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.axteroid.ose.server.tools.validation.ValidateSerieRA;
import com.axteroid.ose.server.tools.validation.ValidateSerieRC;
import com.axteroid.ose.server.tools.validation.ValidateTypeRAVoid;
import com.axteroid.ose.server.tools.validation.ValidateTypeRCTicket;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: RAC
 * Date: 14/02/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class EResumenDocumento implements IDocumento,IEDocumento {

    /**
     * parametro
     */
    @NotBlank(message = "7001")
    public String numeroDocumentoEmisor;


    public String coTipoEmision;
    public String idEmisor;

    public String version = "1.0";
    public String versionUBL = "2.0";

    /**
     * parametro
     */
    @NotBlank(message = "7000")
    public String tipoDocumentoEmisor;

    /**
     * parametro
     * cbc:ID
     */
    @NotBlank(message = "7003")
    @ValidateSerieRC(message = "7090", groups = DocumentSummary.class)
    @ValidateSerieRA(message = "7091", groups = DocumentVoid.class)
    private String resumenId;

    /**
     * parametro
     * cbc:ReferenceDate
     */
    @NotNull(message = "7037")
    private Date fechaEmisionComprobante;

    /**
     * parametro
     * IssueDate
     */
    @NotNull(message = "7038")
    private Date fechaGeneracionResumen;

    /**
     * parametro
     * cac:AccountingSupplierParty/cac:Party/cac:PartyLegalEntity/cbc:RegistrationName
     */
    @NotBlank(message = "7011")
    public String razonSocialEmisor;

    @NotBlank(message = "7010")
    public String correoEmisor;


    public Integer inHabilitado;

    @ValidateTypeRCTicket(groups = DocumentSummary.class, message = "7039")
    @ValidateTypeRAVoid(groups = DocumentVoid.class, message = "7040")
    public String resumenTipo;

    public String rucRazonSocialEmisor;

    @NotEmpty(message = "7013")
    @Valid
    List<EResumenDocumentoItem> items = new ArrayList<EResumenDocumentoItem>();
    private String hashCode;
    private String numIdCd;
    private Date dategetNotBefore;
    private Date dateNotAfter;
    
    public String getRucRazonSocialEmisor() {
        return numeroDocumentoEmisor + "|" + razonSocialEmisor;
    }

    public void setRucRazonSocialEmisor(String rucRazonSocialEmisor) {
        this.rucRazonSocialEmisor = rucRazonSocialEmisor;
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

    public void setTipoDocumentoEmisor(String tipoDocumentoEmisor) {
        this.tipoDocumentoEmisor = tipoDocumentoEmisor;
    }

    public String getResumenId() {
        return resumenId;
    }

    public void setResumenId(String resumenId) {
        this.resumenId = resumenId;
    }

    public Date getFechaEmisionComprobante() {
        return fechaEmisionComprobante;
    }

    public void setFechaEmisionComprobante(Date fechaEmisionComprobante) {
        this.fechaEmisionComprobante = fechaEmisionComprobante;
    }

    public Date getFechaGeneracionResumen() {
        return fechaGeneracionResumen;
    }

    public void setFechaGeneracionResumen(Date fechaGeneracionResumen) {
        this.fechaGeneracionResumen = fechaGeneracionResumen;
    }

    public String getRazonSocialEmisor() {
        return razonSocialEmisor;
    }

    public void setRazonSocialEmisor(String razonSocialEmisor) {
        this.razonSocialEmisor = razonSocialEmisor;
    }

    public String getCorreoEmisor() {
        return correoEmisor;
    }

    public void setCorreoEmisor(String correoEmisor) {
        this.correoEmisor = correoEmisor;
    }

    public String getResumenTipo() {
        return resumenTipo;
    }

    public void setResumenTipo(String resumenTipo) {
        this.resumenTipo = resumenTipo;
    }


    public String getIdDocumento() {
        return getResumenId();
    }

    public String getTipoDocumento() {
        return getResumenTipo();
    }

    public List<EResumenDocumentoItem> getItems() {
        return items;
    }

    public void setItems(List<EResumenDocumentoItem> items) {
        this.items = items;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionUBL() {
        return versionUBL;
    }

    public void setVersionUBL(String versionUBL) {
        this.versionUBL = versionUBL;
    }

    public String getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(String idEmisor) {
        this.idEmisor = idEmisor;
    }

    public String getSerieDocumento() {
//        return null;// todo RAC descomentar
        throw new IllegalArgumentException("Metodo invalido");
    }

    public String getNumeroDocumento() {
//        return null;// todo RAC descomentar
        throw new IllegalArgumentException("Metodo invalido");
    }

    public String getEstado() {
        return null;  //Todo implementar
    }

    public Date getFechaDocumento() {
        return getFechaEmisionComprobante();
    }

    public String getTipoDocumentoAdquiriente() {
        return null;  //Todo implementar
    }

    public String getNumeroDocumentoAdquiriente() {
        return null;  //Todo implementar
    }

    public String getRazonSocialAdquiriente() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCorreoAdquiriente() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    public void populateItem() {
        if (StringUtils.isBlank(getResumenTipo())) {
            setResumenTipo("SN");
        }
        if (StringUtils.isBlank(getResumenId())) {
            setResumenId("SN" + new Date().getTime());
        }
    }

    public Integer getInHabilitado() {
        return inHabilitado;
    }

    public void setInHabilitado(Integer inHabilitado) {
        this.inHabilitado = inHabilitado;
    }

    @Override
    public String toString() {
        return "EResumenDocumento{" +
                "numeroDocumentoEmisor='" + numeroDocumentoEmisor + '\'' +
                ", idEmisor='" + idEmisor + '\'' +
                ", version='" + version + '\'' +
                ", versionUBL='" + versionUBL + '\'' +
                ", tipoDocumentoEmisor='" + tipoDocumentoEmisor + '\'' +
                ", resumenId='" + resumenId + '\'' +
                ", fechaEmisionComprobante=" + fechaEmisionComprobante +
                ", fechaGeneracionResumen=" + fechaGeneracionResumen +
                ", razonSocialEmisor='" + razonSocialEmisor + '\'' +
                ", correoEmisor='" + correoEmisor + '\'' +
                ", inHabilitado=" + inHabilitado +
                ", resumenTipo='" + resumenTipo + '\'' +
                ", rucRazonSocialEmisor='" + rucRazonSocialEmisor + '\'' +
                ", items=" + items +
                '}';
    }

    public boolean isExportacion() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

/*    public String getHashCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }*/

    public String getSignatureValue() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCoTipoEmision() {
        return coTipoEmision;
    }

    public void setCoTipoEmision(String coTipoEmision) {
        this.coTipoEmision = coTipoEmision;
    }


    public void setCorreoAdquiriente(String value) {

    }

    public boolean isNotaDebitoPorPenalidad() {
        return false;
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
