package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "EMISOR", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "Emisor.findAll", query = "SELECT t FROM Emisor t")})
public class Emisor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "RUC_EMISOR")
    private long rucEmisor;
    @Column(name = "USUARIOSOL")
    private String usuariosol;
    @Column(name = "CLAVESOL")
    private String clavesol;
    @Column(name = "ID_TOKEN_GRE")    
    private String idTokenGre;          
    @Column(name = "CLAVE_TOKEN_GRE")
    private String claveTokenGre;    
    @Column(name = "NOMBRE_CERTIFICADO")
    private String nombreCertificado;
    @Column(name = "KEY_PASSWORD")
    private String keyPassword;
    @Column(name = "KEY_STORE_PASSWORD")
    private String keyStorePassword;
    @Column(name = "PRIVATE_KEY_ALIAS")
    private String privateKeyAlias;   
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
    @Column(name = "NOMBRE_JASPER_GRREMIT")
    private String nombreJasperGrremit;
    @Column(name = "NOMBRE_JASPER_GRTRANS")
    private String nombreJasperGrtrans;
    @Column(name = "NOMBRE_IMAGEN_LOGO")
    private String nombreImagenLogo;   
    @Column(name = "NOMBRE_JASPER_BOLETA")
    private String nombreJasperBoleta;
    @Column(name = "NOMBRE_JASPER_FACTURA")
    private String nombreJasperFactura;
    @Column(name = "NOMBRE_JASPER_NOTA_CREDITO")
    private String nombreJasperNotaCredito;
    @Column(name = "NOMBRE_JASPER_NOTA_DEBITO")
    private String nombreJasperNotaDebito;
    @Column(name = "NOMBRE_JASPER_PERCEPCION")
    private String nombreJasperPercepcion;
    @Column(name = "NOMBRE_JASPER_RETENCION")
    private String nombreJasperRetencion;
    
//    @Column(name = "NOMBRE_JASPER_RESUMEN_BOLETA")
//    private String nombreJasperResumenBoleta;
//    @Column(name = "NOMBRE_JASPER_RESUMEN_ANULADO")
//    private String nombreJasperResumenAnulado;
//    @Column(name = "NOMBRE_JASPER_RESUMEN_REVERSION")
//    private String nombreJasperResumenReversion;
    
    @Column(name = "NOMBRE_JASPER_DAE_ROL_ADQUIRIENTE")
    private String nombreJasperDaeRolAdquiriente;
    @Column(name = "NOMBRE_JASPER_DAE_ADQUIRIENTE")
    private String nombreJasperDaeAdquiriente;
    @Column(name = "NOMBRE_JASPER_DAE_OPERADOR")
    private String nombreJasperDaeOperador;   
 
    @Column(name = "NOMBRE_COMERCIAL")
    private String nombreComercial;  
    @Column(name = "RAZON_SOCIAL")
    private String razonSocial;  
    @Column(name = "UBIGEO")
	private String ubigeo;
    @Column(name = "DIRECCION")
	private String direccion;
    @Column(name = "DEPARTAMENTO")
	private String departamento;
    @Column(name = "PROVINCIA")
	private String provincia; 	
    @Column(name = "DISTRITO")
	private String distrito; 
    @Column(name = "URBANIZACION")
	private String urbanizacion;    
    
    public Emisor() {
    }

	public long getRucEmisor() {
		return rucEmisor;
	}

	public void setRucEmisor(long rucEmisor) {
		this.rucEmisor = rucEmisor;
	}

	public String getUsuariosol() {
		return usuariosol;
	}

	public void setUsuariosol(String usuariosol) {
		this.usuariosol = usuariosol;
	}

	public String getClavesol() {
		return clavesol;
	}

	public void setClavesol(String clavesol) {
		this.clavesol = clavesol;
	}

	public String getIdTokenGre() {
		return idTokenGre;
	}

	public void setIdTokenGre(String idTokenGre) {
		this.idTokenGre = idTokenGre;
	}

	public String getClaveTokenGre() {
		return claveTokenGre;
	}

	public void setClaveTokenGre(String claveTokenGre) {
		this.claveTokenGre = claveTokenGre;
	}

	public String getNombreCertificado() {
		return nombreCertificado;
	}

	public void setNombreCertificado(String nombreCertificado) {
		this.nombreCertificado = nombreCertificado;
	}

	public String getKeyPassword() {
		return keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public String getPrivateKeyAlias() {
		return privateKeyAlias;
	}

	public void setPrivateKeyAlias(String privateKeyAlias) {
		this.privateKeyAlias = privateKeyAlias;
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

	public String getNombreJasperGrremit() {
		return nombreJasperGrremit;
	}

	public void setNombreJasperGrremit(String nombreJasperGrremit) {
		this.nombreJasperGrremit = nombreJasperGrremit;
	}

	public String getNombreJasperGrtrans() {
		return nombreJasperGrtrans;
	}

	public void setNombreJasperGrtrans(String nombreJasperGrtrans) {
		this.nombreJasperGrtrans = nombreJasperGrtrans;
	}

	public String getNombreImagenLogo() {
		return nombreImagenLogo;
	}

	public void setNombreImagenLogo(String nombreImagenLogo) {
		this.nombreImagenLogo = nombreImagenLogo;
	}

	public String getNombreJasperBoleta() {
		return nombreJasperBoleta;
	}

	public void setNombreJasperBoleta(String nombreJasperBoleta) {
		this.nombreJasperBoleta = nombreJasperBoleta;
	}

	public String getNombreJasperFactura() {
		return nombreJasperFactura;
	}

	public void setNombreJasperFactura(String nombreJasperFactura) {
		this.nombreJasperFactura = nombreJasperFactura;
	}

	public String getNombreJasperNotaCredito() {
		return nombreJasperNotaCredito;
	}

	public void setNombreJasperNotaCredito(String nombreJasperNotaCredito) {
		this.nombreJasperNotaCredito = nombreJasperNotaCredito;
	}

	public String getNombreJasperNotaDebito() {
		return nombreJasperNotaDebito;
	}

	public void setNombreJasperNotaDebito(String nombreJasperNotaDebito) {
		this.nombreJasperNotaDebito = nombreJasperNotaDebito;
	}

	public String getNombreJasperPercepcion() {
		return nombreJasperPercepcion;
	}

	public void setNombreJasperPercepcion(String nombreJasperPercepcion) {
		this.nombreJasperPercepcion = nombreJasperPercepcion;
	}

	public String getNombreJasperRetencion() {
		return nombreJasperRetencion;
	}

	public void setNombreJasperRetencion(String nombreJasperRetencion) {
		this.nombreJasperRetencion = nombreJasperRetencion;
	}

	public String getNombreJasperDaeRolAdquiriente() {
		return nombreJasperDaeRolAdquiriente;
	}

	public void setNombreJasperDaeRolAdquiriente(String nombreJasperDaeRolAdquiriente) {
		this.nombreJasperDaeRolAdquiriente = nombreJasperDaeRolAdquiriente;
	}

	public String getNombreJasperDaeAdquiriente() {
		return nombreJasperDaeAdquiriente;
	}

	public void setNombreJasperDaeAdquiriente(String nombreJasperDaeAdquiriente) {
		this.nombreJasperDaeAdquiriente = nombreJasperDaeAdquiriente;
	}

	public String getNombreJasperDaeOperador() {
		return nombreJasperDaeOperador;
	}

	public void setNombreJasperDaeOperador(String nombreJasperDaeOperador) {
		this.nombreJasperDaeOperador = nombreJasperDaeOperador;
	}

	public String getNombreComercial() {
		return nombreComercial;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getUbigeo() {
		return ubigeo;
	}

	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getUrbanizacion() {
		return urbanizacion;
	}

	public void setUrbanizacion(String urbanizacion) {
		this.urbanizacion = urbanizacion;
	}

}
