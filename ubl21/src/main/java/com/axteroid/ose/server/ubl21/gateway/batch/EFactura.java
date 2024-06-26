//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-793 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.08.10 at 04:51:51 PM COT 
//


package com.axteroid.ose.server.ubl21.gateway.batch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Factura", propOrder = {
	"versionUBL",
	"perfil",
    "correoEmisor",
    "correoAdquiriente",
    "codigoPais",
    "prefijo",
    "rangoInicial",
    "rangoFinal",
    "fechaVigenciaDesde",
    "fechaVigenciaHasta",
    "cufe",
    "fechaEmision",
    "horaEmision",
    "tipoDocumento",
    "nota",
    "tipoMoneda",
    "numeroCodigoTributarioEmisor",
    "razonSocialEmisor",
    "codigoCortoTipoTributarioEmisor",
    "tipoCodigoTributarioEmisor",
    "nombreTipoCodigoTributarioEmisor",
    "dunsEmisor",
    "departamentoEmisor",
    "nombreCiudadEmisor",
    "direccionEmisor",
    "numeroCodigoTributarioCliente",
    "razonSocialCliente",
    "codigoCortoTipoTributarioCliente",
    "tipoCodigoTributarioCliente",
    "nombreTipoCodigoTributarioCliente",
    "dunsCliente",
    "departamentoCliente",
    "nombreCiudadCliente",
    "direccionCliente",
    "montoImpuestoIVA",
    "montoSaldo",
    "montoSubtotal",
    "montoTotal",
    "montoTotaldescuento",
    "montoTotalDescuentoItems",
    "inHabilitado",
    "facturaItem"
})
@XmlRootElement
public class EFactura {
	
	protected String versionUBL = "UBL 2.0";
	protected String perfil = "DIAN 1.0";
	
	protected String correoEmisor;
	protected String correoAdquiriente;
	protected String codigoPais;
	protected String prefijo;
	protected String rangoInicial;
	protected String rangoFinal;
	protected String fechaVigenciaDesde;
	protected String fechaVigenciaHasta;
	protected String cufe;
	protected String fechaEmision;
	
	@XmlElement(name = "HoraEmision")
	protected String horaEmision;
	
	protected String tipoDocumento;
	protected String nota;
	protected String tipoMoneda;
	protected String numeroCodigoTributarioEmisor;
	protected String razonSocialEmisor;
	protected String codigoCortoTipoTributarioEmisor;
	protected String tipoCodigoTributarioEmisor;
	protected String nombreTipoCodigoTributarioEmisor;
	protected String dunsEmisor;
	protected String departamentoEmisor;
	protected String nombreCiudadEmisor;
	protected String direccionEmisor;
	protected String numeroCodigoTributarioCliente;
	protected String razonSocialCliente;
	protected String codigoCortoTipoTributarioCliente;
	protected String tipoCodigoTributarioCliente;
	protected String nombreTipoCodigoTributarioCliente;
	protected String dunsCliente;
	protected String departamentoCliente;
	protected String nombreCiudadCliente;
	protected String direccionCliente;
	protected BigDecimal montoImpuestoIVA;
	protected BigDecimal montoSaldo;
	protected BigDecimal montoSubtotal;
	protected BigDecimal montoTotal;
	protected BigDecimal montoTotaldescuento;
	protected BigDecimal montoTotalDescuentoItems;
	protected String inHabilitado;
	protected List<EFacturaItem> facturaItem;

	public String getVersionUBL() {
		return versionUBL;
	}

	public void setVersionUBL(String versionUBL) {
		this.versionUBL = versionUBL;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
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

	public String getCodigoPais() {
		return codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getPrefijo() {
		return prefijo;
	}

	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}

	public String getRangoInicial() {
		return rangoInicial;
	}

	public void setRangoInicial(String rangoInicial) {
		this.rangoInicial = rangoInicial;
	}

	public String getRangoFinal() {
		return rangoFinal;
	}

	public void setRangoFinal(String rangoFinal) {
		this.rangoFinal = rangoFinal;
	}

	public String getFechaVigenciaDesde() {
		return fechaVigenciaDesde;
	}

	public void setFechaVigenciaDesde(String fechaVigenciaDesde) {
		this.fechaVigenciaDesde = fechaVigenciaDesde;
	}

	public String getFechaVigenciaHasta() {
		return fechaVigenciaHasta;
	}

	public void setFechaVigenciaHasta(String fechaVigenciaHasta) {
		this.fechaVigenciaHasta = fechaVigenciaHasta;
	}

	public String getCufe() {
		return cufe;
	}

	public void setCufe(String cufe) {
		this.cufe = cufe;
	}

	public String getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getHoraEmision() {
		return horaEmision;
	}

	public void setHoraEmision(String horaEmision) {
		this.horaEmision = horaEmision;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	public String getNumeroCodigoTributarioEmisor() {
		return numeroCodigoTributarioEmisor;
	}

	public void setNumeroCodigoTributarioEmisor(String numeroCodigoTributarioEmisor) {
		this.numeroCodigoTributarioEmisor = numeroCodigoTributarioEmisor;
	}

	public String getRazonSocialEmisor() {
		return razonSocialEmisor;
	}

	public void setRazonSocialEmisor(String razonSocialEmisor) {
		this.razonSocialEmisor = razonSocialEmisor;
	}

	public String getCodigoCortoTipoTributarioEmisor() {
		return codigoCortoTipoTributarioEmisor;
	}

	public void setCodigoCortoTipoTributarioEmisor(String codigoCortoTipoTributarioEmisor) {
		this.codigoCortoTipoTributarioEmisor = codigoCortoTipoTributarioEmisor;
	}

	public String getTipoCodigoTributarioEmisor() {
		return tipoCodigoTributarioEmisor;
	}

	public void setTipoCodigoTributarioEmisor(String tipoCodigoTributarioEmisor) {
		this.tipoCodigoTributarioEmisor = tipoCodigoTributarioEmisor;
	}

	public String getNombreTipoCodigoTributarioEmisor() {
		return nombreTipoCodigoTributarioEmisor;
	}

	public void setNombreTipoCodigoTributarioEmisor(String nombreTipoCodigoTributarioEmisor) {
		this.nombreTipoCodigoTributarioEmisor = nombreTipoCodigoTributarioEmisor;
	}

	public String getDunsEmisor() {
		return dunsEmisor;
	}

	public void setDunsEmisor(String dunsEmisor) {
		this.dunsEmisor = dunsEmisor;
	}

	public String getDepartamentoEmisor() {
		return departamentoEmisor;
	}

	public void setDepartamentoEmisor(String departamentoEmisor) {
		this.departamentoEmisor = departamentoEmisor;
	}

	public String getNombreCiudadEmisor() {
		return nombreCiudadEmisor;
	}

	public void setNombreCiudadEmisor(String nombreCiudadEmisor) {
		this.nombreCiudadEmisor = nombreCiudadEmisor;
	}

	public String getDireccionEmisor() {
		return direccionEmisor;
	}

	public void setDireccionEmisor(String direccionEmisor) {
		this.direccionEmisor = direccionEmisor;
	}

	public String getNumeroCodigoTributarioCliente() {
		return numeroCodigoTributarioCliente;
	}

	public void setNumeroCodigoTributarioCliente(String numeroCodigoTributarioCliente) {
		this.numeroCodigoTributarioCliente = numeroCodigoTributarioCliente;
	}

	public String getRazonSocialCliente() {
		return razonSocialCliente;
	}

	public void setRazonSocialCliente(String razonSocialCliente) {
		this.razonSocialCliente = razonSocialCliente;
	}

	public String getCodigoCortoTipoTributarioCliente() {
		return codigoCortoTipoTributarioCliente;
	}

	public void setCodigoCortoTipoTributarioCliente(String codigoCortoTipoTributarioCliente) {
		this.codigoCortoTipoTributarioCliente = codigoCortoTipoTributarioCliente;
	}

	public String getTipoCodigoTributarioCliente() {
		return tipoCodigoTributarioCliente;
	}

	public void setTipoCodigoTributarioCliente(String tipoCodigoTributarioCliente) {
		this.tipoCodigoTributarioCliente = tipoCodigoTributarioCliente;
	}

	public String getNombreTipoCodigoTributarioCliente() {
		return nombreTipoCodigoTributarioCliente;
	}

	public void setNombreTipoCodigoTributarioCliente(String nombreTipoCodigoTributarioCliente) {
		this.nombreTipoCodigoTributarioCliente = nombreTipoCodigoTributarioCliente;
	}

	public String getDunsCliente() {
		return dunsCliente;
	}

	public void setDunsCliente(String dunsCliente) {
		this.dunsCliente = dunsCliente;
	}

	public String getDepartamentoCliente() {
		return departamentoCliente;
	}

	public void setDepartamentoCliente(String departamentoCliente) {
		this.departamentoCliente = departamentoCliente;
	}

	public String getNombreCiudadCliente() {
		return nombreCiudadCliente;
	}

	public void setNombreCiudadCliente(String nombreCiudadCliente) {
		this.nombreCiudadCliente = nombreCiudadCliente;
	}

	public String getDireccionCliente() {
		return direccionCliente;
	}

	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}

	public BigDecimal getMontoImpuestoIVA() {
		return montoImpuestoIVA;
	}

	public void setMontoImpuestoIVA(BigDecimal montoImpuestoIVA) {
		this.montoImpuestoIVA = montoImpuestoIVA;
	}

	public BigDecimal getMontoSaldo() {
		return montoSaldo;
	}

	public void setMontoSaldo(BigDecimal montoSaldo) {
		this.montoSaldo = montoSaldo;
	}

	public BigDecimal getMontoSubtotal() {
		return montoSubtotal;
	}

	public void setMontoSubtotal(BigDecimal montoSubtotal) {
		this.montoSubtotal = montoSubtotal;
	}

	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	public BigDecimal getMontoTotaldescuento() {
		return montoTotaldescuento;
	}

	public void setMontoTotaldescuento(BigDecimal montoTotaldescuento) {
		this.montoTotaldescuento = montoTotaldescuento;
	}

	public BigDecimal getMontoTotalDescuentoItems() {
		return montoTotalDescuentoItems;
	}

	public void setMontoTotalDescuentoItems(BigDecimal montoTotalDescuentoItems) {
		this.montoTotalDescuentoItems = montoTotalDescuentoItems;
	}

	public String getInHabilitado() {
		return inHabilitado;
	}

	public void setInHabilitado(String inHabilitado) {
		this.inHabilitado = inHabilitado;
	}

	public List<EFacturaItem> getFacturaItem() {
		if (facturaItem == null) {
			facturaItem = new ArrayList<EFacturaItem>();
		}
		return this.facturaItem;
	}

}
