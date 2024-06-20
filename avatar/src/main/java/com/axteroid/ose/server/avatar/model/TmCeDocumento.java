package com.axteroid.ose.server.avatar.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TM_CE_DOCUMENTO database table.
 * 
 */
@Entity
@Table(name="TM_CE_DOCUMENTO")
@NamedQuery(name="TmCeDocumento.findAll", query="SELECT t FROM TmCeDocumento t")
public class TmCeDocumento implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TmCeDocumentoPK id;

	@Lob
	@Column(name="ADJUNTO_1")
	private byte[] adjunto1;

	@Lob
	@Column(name="ADJUNTO_2")
	private byte[] adjunto2;

	@Lob
	@Column(name="ADJUNTO_3")
	private byte[] adjunto3;

	@Lob
	@Column(name="ADJUNTO_4")
	private byte[] adjunto4;

	@Lob
	@Column(name="ADJUNTO_5")
	private byte[] adjunto5;

	@Lob
	private byte[] archivoenviado;

	@Lob
	private byte[] archivorespuesta;

	private String clavesol;

	private String codigobarrafuente;

	private String codigohash;

	private String correoadquiriente;

	private String correoemisor;

	private String cotipoemision;

	private String digestvalue;

	private long documentoelectronicoref;

	private String estado;

	private String estadodocumento;

	private String estadosunat;

	@Column(name="FECH_CREA")
	private Timestamp fechCrea;

	@Column(name="FECH_MODI")
	private Timestamp fechModi;

	private Timestamp fechadeclaracion;

	private Timestamp fechaemision;

	private Timestamp fechaenvioportal;

	private Timestamp fechaenvioreplicacion;

	private Timestamp fechaenviosunat;

	private Timestamp fechafirmaelectronica;

	private Timestamp fecharespuestareplicacion;

	private Timestamp fecharespuestasunat;

	private Timestamp fechaworker;

	private String firmaelectronica;

	@Column(name="FLAG_GENERACION_PDF")
	private int flagGeneracionPdf;

	@Column(name="FLAG_GENERACION_PDF2")
	private int flagGeneracionPdf2;

	private long idcomprobante;

	private String iddocumento;

	private String idregistro;

	private long idticket;

	private String inbloqueo;

	private String indeclarar;

	private String inpublico;

	private String inreplico;

	private String inreporto;

	@Column(name="INREPORTO_CDR")
	private long inreportoCdr;

	@Column(name="INREPORTO_UBL")
	private long inreportoUbl;

	private long intentos;

	private String mensaje;

	private String mensajesunat;

	@Column(name="NOMBREADJUNTO_1")
	private String nombreadjunto1;

	@Column(name="NOMBREADJUNTO_2")
	private String nombreadjunto2;

	@Column(name="NOMBREADJUNTO_3")
	private String nombreadjunto3;

	@Column(name="NOMBREADJUNTO_4")
	private String nombreadjunto4;

	@Column(name="NOMBREADJUNTO_5")
	private String nombreadjunto5;

	private String nombrearchivo;

	private String numerodocumentoadquiriente;

	private String numerodocumentoemisor;

	private String nupersonalizacion;

	private String nuubl;

	private String razonsocialadquiriente;

	private String razonsocialemisor;

	private String rutaarchivo;

	private String secuencialdocumento;

	private String seriedocumento;

	private String signaturevalue;

	private String ticketsunat;

	private String tipodocumento;

	private String tipodocumentoadquiriente;

	private String tipodocumentoemisor;

	private String tipomoneda;

	private BigDecimal totaligv;

	private BigDecimal totalventa;

	@Column(name="USUA_CREA")
	private String usuaCrea;

	@Column(name="USUA_MODI")
	private String usuaModi;

	private String usuariosol;

	private String worker;

	@Lob
	private String xmldata;

	public TmCeDocumento() {
	}

	public TmCeDocumentoPK getId() {
		return this.id;
	}

	public void setId(TmCeDocumentoPK id) {
		this.id = id;
	}

	public byte[] getAdjunto1() {
		return this.adjunto1;
	}

	public void setAdjunto1(byte[] adjunto1) {
		this.adjunto1 = adjunto1;
	}

	public byte[] getAdjunto2() {
		return this.adjunto2;
	}

	public void setAdjunto2(byte[] adjunto2) {
		this.adjunto2 = adjunto2;
	}

	public byte[] getAdjunto3() {
		return this.adjunto3;
	}

	public void setAdjunto3(byte[] adjunto3) {
		this.adjunto3 = adjunto3;
	}

	public byte[] getAdjunto4() {
		return this.adjunto4;
	}

	public void setAdjunto4(byte[] adjunto4) {
		this.adjunto4 = adjunto4;
	}

	public byte[] getAdjunto5() {
		return this.adjunto5;
	}

	public void setAdjunto5(byte[] adjunto5) {
		this.adjunto5 = adjunto5;
	}

	public byte[] getArchivoenviado() {
		return this.archivoenviado;
	}

	public void setArchivoenviado(byte[] archivoenviado) {
		this.archivoenviado = archivoenviado;
	}

	public byte[] getArchivorespuesta() {
		return this.archivorespuesta;
	}

	public void setArchivorespuesta(byte[] archivorespuesta) {
		this.archivorespuesta = archivorespuesta;
	}

	public String getClavesol() {
		return this.clavesol;
	}

	public void setClavesol(String clavesol) {
		this.clavesol = clavesol;
	}

	public String getCodigobarrafuente() {
		return this.codigobarrafuente;
	}

	public void setCodigobarrafuente(String codigobarrafuente) {
		this.codigobarrafuente = codigobarrafuente;
	}

	public String getCodigohash() {
		return this.codigohash;
	}

	public void setCodigohash(String codigohash) {
		this.codigohash = codigohash;
	}

	public String getCorreoadquiriente() {
		return this.correoadquiriente;
	}

	public void setCorreoadquiriente(String correoadquiriente) {
		this.correoadquiriente = correoadquiriente;
	}

	public String getCorreoemisor() {
		return this.correoemisor;
	}

	public void setCorreoemisor(String correoemisor) {
		this.correoemisor = correoemisor;
	}

	public String getCotipoemision() {
		return this.cotipoemision;
	}

	public void setCotipoemision(String cotipoemision) {
		this.cotipoemision = cotipoemision;
	}

	public String getDigestvalue() {
		return this.digestvalue;
	}

	public void setDigestvalue(String digestvalue) {
		this.digestvalue = digestvalue;
	}

	public long getDocumentoelectronicoref() {
		return this.documentoelectronicoref;
	}

	public void setDocumentoelectronicoref(long documentoelectronicoref) {
		this.documentoelectronicoref = documentoelectronicoref;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadodocumento() {
		return this.estadodocumento;
	}

	public void setEstadodocumento(String estadodocumento) {
		this.estadodocumento = estadodocumento;
	}

	public String getEstadosunat() {
		return this.estadosunat;
	}

	public void setEstadosunat(String estadosunat) {
		this.estadosunat = estadosunat;
	}

	public Timestamp getFechCrea() {
		return this.fechCrea;
	}

	public void setFechCrea(Timestamp fechCrea) {
		this.fechCrea = fechCrea;
	}

	public Timestamp getFechModi() {
		return this.fechModi;
	}

	public void setFechModi(Timestamp fechModi) {
		this.fechModi = fechModi;
	}

	public Timestamp getFechadeclaracion() {
		return this.fechadeclaracion;
	}

	public void setFechadeclaracion(Timestamp fechadeclaracion) {
		this.fechadeclaracion = fechadeclaracion;
	}

	public Timestamp getFechaemision() {
		return this.fechaemision;
	}

	public void setFechaemision(Timestamp fechaemision) {
		this.fechaemision = fechaemision;
	}

	public Timestamp getFechaenvioportal() {
		return this.fechaenvioportal;
	}

	public void setFechaenvioportal(Timestamp fechaenvioportal) {
		this.fechaenvioportal = fechaenvioportal;
	}

	public Timestamp getFechaenvioreplicacion() {
		return this.fechaenvioreplicacion;
	}

	public void setFechaenvioreplicacion(Timestamp fechaenvioreplicacion) {
		this.fechaenvioreplicacion = fechaenvioreplicacion;
	}

	public Timestamp getFechaenviosunat() {
		return this.fechaenviosunat;
	}

	public void setFechaenviosunat(Timestamp fechaenviosunat) {
		this.fechaenviosunat = fechaenviosunat;
	}

	public Timestamp getFechafirmaelectronica() {
		return this.fechafirmaelectronica;
	}

	public void setFechafirmaelectronica(Timestamp fechafirmaelectronica) {
		this.fechafirmaelectronica = fechafirmaelectronica;
	}

	public Timestamp getFecharespuestareplicacion() {
		return this.fecharespuestareplicacion;
	}

	public void setFecharespuestareplicacion(Timestamp fecharespuestareplicacion) {
		this.fecharespuestareplicacion = fecharespuestareplicacion;
	}

	public Timestamp getFecharespuestasunat() {
		return this.fecharespuestasunat;
	}

	public void setFecharespuestasunat(Timestamp fecharespuestasunat) {
		this.fecharespuestasunat = fecharespuestasunat;
	}

	public Timestamp getFechaworker() {
		return this.fechaworker;
	}

	public void setFechaworker(Timestamp fechaworker) {
		this.fechaworker = fechaworker;
	}

	public String getFirmaelectronica() {
		return this.firmaelectronica;
	}

	public void setFirmaelectronica(String firmaelectronica) {
		this.firmaelectronica = firmaelectronica;
	}

	public int getFlagGeneracionPdf() {
		return this.flagGeneracionPdf;
	}

	public void setFlagGeneracionPdf(int flagGeneracionPdf) {
		this.flagGeneracionPdf = flagGeneracionPdf;
	}

	public int getFlagGeneracionPdf2() {
		return this.flagGeneracionPdf2;
	}

	public void setFlagGeneracionPdf2(int flagGeneracionPdf2) {
		this.flagGeneracionPdf2 = flagGeneracionPdf2;
	}

	public long getIdcomprobante() {
		return this.idcomprobante;
	}

	public void setIdcomprobante(long idcomprobante) {
		this.idcomprobante = idcomprobante;
	}

	public String getIddocumento() {
		return this.iddocumento;
	}

	public void setIddocumento(String iddocumento) {
		this.iddocumento = iddocumento;
	}

	public String getIdregistro() {
		return this.idregistro;
	}

	public void setIdregistro(String idregistro) {
		this.idregistro = idregistro;
	}

	public long getIdticket() {
		return this.idticket;
	}

	public void setIdticket(long idticket) {
		this.idticket = idticket;
	}

	public String getInbloqueo() {
		return this.inbloqueo;
	}

	public void setInbloqueo(String inbloqueo) {
		this.inbloqueo = inbloqueo;
	}

	public String getIndeclarar() {
		return this.indeclarar;
	}

	public void setIndeclarar(String indeclarar) {
		this.indeclarar = indeclarar;
	}

	public String getInpublico() {
		return this.inpublico;
	}

	public void setInpublico(String inpublico) {
		this.inpublico = inpublico;
	}

	public String getInreplico() {
		return this.inreplico;
	}

	public void setInreplico(String inreplico) {
		this.inreplico = inreplico;
	}

	public String getInreporto() {
		return this.inreporto;
	}

	public void setInreporto(String inreporto) {
		this.inreporto = inreporto;
	}

	public long getInreportoCdr() {
		return this.inreportoCdr;
	}

	public void setInreportoCdr(long inreportoCdr) {
		this.inreportoCdr = inreportoCdr;
	}

	public long getInreportoUbl() {
		return this.inreportoUbl;
	}

	public void setInreportoUbl(long inreportoUbl) {
		this.inreportoUbl = inreportoUbl;
	}

	public long getIntentos() {
		return this.intentos;
	}

	public void setIntentos(long intentos) {
		this.intentos = intentos;
	}

	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensajesunat() {
		return this.mensajesunat;
	}

	public void setMensajesunat(String mensajesunat) {
		this.mensajesunat = mensajesunat;
	}

	public String getNombreadjunto1() {
		return this.nombreadjunto1;
	}

	public void setNombreadjunto1(String nombreadjunto1) {
		this.nombreadjunto1 = nombreadjunto1;
	}

	public String getNombreadjunto2() {
		return this.nombreadjunto2;
	}

	public void setNombreadjunto2(String nombreadjunto2) {
		this.nombreadjunto2 = nombreadjunto2;
	}

	public String getNombreadjunto3() {
		return this.nombreadjunto3;
	}

	public void setNombreadjunto3(String nombreadjunto3) {
		this.nombreadjunto3 = nombreadjunto3;
	}

	public String getNombreadjunto4() {
		return this.nombreadjunto4;
	}

	public void setNombreadjunto4(String nombreadjunto4) {
		this.nombreadjunto4 = nombreadjunto4;
	}

	public String getNombreadjunto5() {
		return this.nombreadjunto5;
	}

	public void setNombreadjunto5(String nombreadjunto5) {
		this.nombreadjunto5 = nombreadjunto5;
	}

	public String getNombrearchivo() {
		return this.nombrearchivo;
	}

	public void setNombrearchivo(String nombrearchivo) {
		this.nombrearchivo = nombrearchivo;
	}

	public String getNumerodocumentoadquiriente() {
		return this.numerodocumentoadquiriente;
	}

	public void setNumerodocumentoadquiriente(String numerodocumentoadquiriente) {
		this.numerodocumentoadquiriente = numerodocumentoadquiriente;
	}

	public String getNumerodocumentoemisor() {
		return this.numerodocumentoemisor;
	}

	public void setNumerodocumentoemisor(String numerodocumentoemisor) {
		this.numerodocumentoemisor = numerodocumentoemisor;
	}

	public String getNupersonalizacion() {
		return this.nupersonalizacion;
	}

	public void setNupersonalizacion(String nupersonalizacion) {
		this.nupersonalizacion = nupersonalizacion;
	}

	public String getNuubl() {
		return this.nuubl;
	}

	public void setNuubl(String nuubl) {
		this.nuubl = nuubl;
	}

	public String getRazonsocialadquiriente() {
		return this.razonsocialadquiriente;
	}

	public void setRazonsocialadquiriente(String razonsocialadquiriente) {
		this.razonsocialadquiriente = razonsocialadquiriente;
	}

	public String getRazonsocialemisor() {
		return this.razonsocialemisor;
	}

	public void setRazonsocialemisor(String razonsocialemisor) {
		this.razonsocialemisor = razonsocialemisor;
	}

	public String getRutaarchivo() {
		return this.rutaarchivo;
	}

	public void setRutaarchivo(String rutaarchivo) {
		this.rutaarchivo = rutaarchivo;
	}

	public String getSecuencialdocumento() {
		return this.secuencialdocumento;
	}

	public void setSecuencialdocumento(String secuencialdocumento) {
		this.secuencialdocumento = secuencialdocumento;
	}

	public String getSeriedocumento() {
		return this.seriedocumento;
	}

	public void setSeriedocumento(String seriedocumento) {
		this.seriedocumento = seriedocumento;
	}

	public String getSignaturevalue() {
		return this.signaturevalue;
	}

	public void setSignaturevalue(String signaturevalue) {
		this.signaturevalue = signaturevalue;
	}

	public String getTicketsunat() {
		return this.ticketsunat;
	}

	public void setTicketsunat(String ticketsunat) {
		this.ticketsunat = ticketsunat;
	}

	public String getTipodocumento() {
		return this.tipodocumento;
	}

	public void setTipodocumento(String tipodocumento) {
		this.tipodocumento = tipodocumento;
	}

	public String getTipodocumentoadquiriente() {
		return this.tipodocumentoadquiriente;
	}

	public void setTipodocumentoadquiriente(String tipodocumentoadquiriente) {
		this.tipodocumentoadquiriente = tipodocumentoadquiriente;
	}

	public String getTipodocumentoemisor() {
		return this.tipodocumentoemisor;
	}

	public void setTipodocumentoemisor(String tipodocumentoemisor) {
		this.tipodocumentoemisor = tipodocumentoemisor;
	}

	public String getTipomoneda() {
		return this.tipomoneda;
	}

	public void setTipomoneda(String tipomoneda) {
		this.tipomoneda = tipomoneda;
	}

	public BigDecimal getTotaligv() {
		return this.totaligv;
	}

	public void setTotaligv(BigDecimal totaligv) {
		this.totaligv = totaligv;
	}

	public BigDecimal getTotalventa() {
		return this.totalventa;
	}

	public void setTotalventa(BigDecimal totalventa) {
		this.totalventa = totalventa;
	}

	public String getUsuaCrea() {
		return this.usuaCrea;
	}

	public void setUsuaCrea(String usuaCrea) {
		this.usuaCrea = usuaCrea;
	}

	public String getUsuaModi() {
		return this.usuaModi;
	}

	public void setUsuaModi(String usuaModi) {
		this.usuaModi = usuaModi;
	}

	public String getUsuariosol() {
		return this.usuariosol;
	}

	public void setUsuariosol(String usuariosol) {
		this.usuariosol = usuariosol;
	}

	public String getWorker() {
		return this.worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getXmldata() {
		return this.xmldata;
	}

	public void setXmldata(String xmldata) {
		this.xmldata = xmldata;
	}

}