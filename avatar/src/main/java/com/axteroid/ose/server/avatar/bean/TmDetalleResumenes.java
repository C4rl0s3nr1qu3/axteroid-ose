package com.axteroid.ose.server.avatar.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_DETALLE_RESUMENES", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "TmDetalleResumenes.findAll", query = "SELECT t FROM TmDetalleResumenes t")})

public class TmDetalleResumenes implements Serializable{

	private static final long serialVersionUID = -6519766389587806866L;
    @Basic(optional = false)
    @Column(name = "ID_COMPROBANTE_RC")
    private String idComprobanteRc;
	@Basic(optional = false)
    @Column(name = "NUM_RUC")
    private long numRuc;
    @Basic(optional = false)
    @Column(name = "COD_CPE")
    private String codCpe;
    @Basic(optional = false)
    @Column(name = "NUM_SERIE_CPE")
    private String numSerieCpe;
    @Basic(optional = false)
    @Column(name = "NUM_CPE")
    private String numCpe;
    @Column(name = "IND_ESTADO_CPE")
    private Short indEstadoCpe;
    @Column(name = "FEC_EMISION_CPE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEmisionCpe;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MTO_IMPORTE_CPE")
    private BigDecimal mtoImporteCpe;
    @Column(name = "COD_MONEDA_CPE")
    private String codMonedaCpe;
    @Column(name = "COD_MOT_TRASLADO")
    private Short codMotTraslado;
    @Column(name = "COD_MOD_TRASLADO")
    private Short codModTraslado;
    @Column(name = "IND_TRANSBORDO")
    private Short indTransbordo;
    @Column(name = "FEC_INI_TRASLADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecIniTraslado;
    @Basic(optional = false)
    @Column(name = "USER_CREA")
    private String userCrea;
    @Basic(optional = false)
    @Column(name = "FECHA_CREA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCrea;
    @Column(name = "USER_MODI")
    private String userModi;
    @Column(name = "FECHA_MODI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModi;   
    @Basic(optional = false)
    @Column(name = "ERROR_COMPROBANTE")
    private Character errorComprobante;
    
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
	public long getNumRuc() {
		return numRuc;
	}
	public void setNumRuc(long numRuc) {
		this.numRuc = numRuc;
	}
	public String getCodCpe() {
		return codCpe;
	}
	public void setCodCpe(String codCpe) {
		this.codCpe = codCpe;
	}
	public String getNumSerieCpe() {
		return numSerieCpe;
	}
	public void setNumSerieCpe(String numSerieCpe) {
		this.numSerieCpe = numSerieCpe;
	}
	public String getNumCpe() {
		return numCpe;
	}
	public void setNumCpe(String numCpe) {
		this.numCpe = numCpe;
	}
	public Short getIndEstadoCpe() {
		return indEstadoCpe;
	}
	public void setIndEstadoCpe(Short indEstadoCpe) {
		this.indEstadoCpe = indEstadoCpe;
	}
	public Date getFecEmisionCpe() {
		return fecEmisionCpe;
	}
	public void setFecEmisionCpe(Date fecEmisionCpe) {
		this.fecEmisionCpe = fecEmisionCpe;
	}
	public BigDecimal getMtoImporteCpe() {
		return mtoImporteCpe;
	}
	public void setMtoImporteCpe(BigDecimal mtoImporteCpe) {
		this.mtoImporteCpe = mtoImporteCpe;
	}
	public String getCodMonedaCpe() {
		return codMonedaCpe;
	}
	public void setCodMonedaCpe(String codMonedaCpe) {
		this.codMonedaCpe = codMonedaCpe;
	}
	public Short getCodMotTraslado() {
		return codMotTraslado;
	}
	public void setCodMotTraslado(Short codMotTraslado) {
		this.codMotTraslado = codMotTraslado;
	}
	public Short getCodModTraslado() {
		return codModTraslado;
	}
	public void setCodModTraslado(Short codModTraslado) {
		this.codModTraslado = codModTraslado;
	}
	public Short getIndTransbordo() {
		return indTransbordo;
	}
	public void setIndTransbordo(Short indTransbordo) {
		this.indTransbordo = indTransbordo;
	}
	public Date getFecIniTraslado() {
		return fecIniTraslado;
	}
	public void setFecIniTraslado(Date fecIniTraslado) {
		this.fecIniTraslado = fecIniTraslado;
	}
	public String getUserCrea() {
		return userCrea;
	}
	public void setUserCrea(String userCrea) {
		this.userCrea = userCrea;
	}
	public Date getFechaCrea() {
		return fechaCrea;
	}
	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}
	public String getUserModi() {
		return userModi;
	}
	public void setUserModi(String userModi) {
		this.userModi = userModi;
	}
	public Date getFechaModi() {
		return fechaModi;
	}
	public void setFechaModi(Date fechaModi) {
		this.fechaModi = fechaModi;
	}
	public String getIdComprobanteRc() {
		return idComprobanteRc;
	}
	public void setIdComprobanteRc(String idComprobanteRc) {
		this.idComprobanteRc = idComprobanteRc;
	}
	public Character getErrorComprobante() {
		return errorComprobante;
	}
	public void setErrorComprobante(Character errorComprobante) {
		this.errorComprobante = errorComprobante;
	}
}
