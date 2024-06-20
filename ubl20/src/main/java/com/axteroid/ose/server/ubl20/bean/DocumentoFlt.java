package com.axteroid.ose.server.ubl20.bean;

import org.apache.commons.lang.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: RAC
 * Date: 03/04/12
 */
public class DocumentoFlt {
    private String idEmisor;
    private String idOrigen;
    private String idDocumento;
    private String tipoDocumento;
    private String tipoDocumentoAdquiriente;
    private String idAdquiriente;
    private String fechaFirmaInicio;
    private String fechaFirmaFin;
    private String serieGrupoDocumento;
    private String estadoDocumento;
    private String numeroCorrelativoInicio;
    private String numeroCorrelativoFin;
    private String inReplico;
    private String inBloqueo;
    private String declararSunat ;
    private String estadoSunat;
    private String worker;
    private boolean soloNuevos=true;
    private List<String> estadoSunatList = new ArrayList<String>();

    private Long idTicket;
    private String tipoDocumentoEmisor;
//    private String emisorId;
    private Timestamp fechaInicioNotificacion;
    private Timestamp fechaFinNotificacion;

    private List<String> documentoStr;

    public DocumentoFlt() {
    }

    public DocumentoFlt(String idEmisor, String tipoDocumento, String idDocumento) {
        this.idEmisor = idEmisor;
        this.tipoDocumento = tipoDocumento;
        this.idDocumento = idDocumento;
    }

    public DocumentoFlt(String idEmisor, Long idTicket, String idDocumento) {
        this(idEmisor, idTicket);
        setIdDocumento(idDocumento);
    }

    public DocumentoFlt(String idEmisor, Long idTicket) {
        setIdEmisor(idEmisor);
        setIdTicket(idTicket);
    }

    public String getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(String idEmisor) {
        this.idEmisor = idEmisor;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getTipoDocumentoEmisor() {
        return tipoDocumentoEmisor;
    }

    public void setTipoDocumentoEmisor(String tipoDocumentoEmisor) {
        this.tipoDocumentoEmisor = tipoDocumentoEmisor;
    }

    @Override
    public String toString() {
        return "DocumentoFlt{" +
                "idEmisor='" + idEmisor + '\'' +
                ", idOrigen='" + idOrigen + '\'' +
                ", idDocumento='" + idDocumento + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", tipoDocumentoAdquiriente='" + tipoDocumentoAdquiriente + '\'' +
                ", idAdquiriente='" + idAdquiriente + '\'' +
                ", fechaFirmaInicio='" + fechaFirmaInicio + '\'' +
                ", fechaFirmaFin='" + fechaFirmaFin + '\'' +
                ", serieGrupoDocumento='" + serieGrupoDocumento + '\'' +
                ", estadoDocumento='" + estadoDocumento + '\'' +
                ", numeroCorrelativoInicio='" + numeroCorrelativoInicio + '\'' +
                ", numeroCorrelativoFin='" + numeroCorrelativoFin + '\'' +
                ", inReplico='" + inReplico + '\'' +
                ", inBloqueo='" + inBloqueo + '\'' +
                ", declararSunat='" + declararSunat + '\'' +
                ", estadoSunat='" + estadoSunat + '\'' +
                ", worker='" + worker + '\'' +
                ", soloNuevos=" + soloNuevos +
                ", estadoSunatList=" + estadoSunatList +
                ", idTicket=" + idTicket +
                ", tipoDocumentoEmisor='" + tipoDocumentoEmisor + '\'' +
                ", fechaInicioNotificacion=" + fechaInicioNotificacion +
                ", fechaFinNotificacion=" + fechaFinNotificacion +
                ", documentoStr=" + documentoStr +
                '}';
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }


    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<String> getDocumentoStr() {
        return documentoStr;
    }

    public void setDocumentoStr(List<String> documentoStr) {
        this.documentoStr = documentoStr;
    }

    public String getInReplico() {
        return inReplico;
    }

    public void setInReplico(String inReplico) {
        this.inReplico = inReplico;
    }

    public String getEstadoSunat() {
        return estadoSunat;
    }

    public void setEstadoSunat(String estadoSunat) {
        this.estadoSunat = estadoSunat;
    }

    public Timestamp getFechaInicioNotificacion() {
        return fechaInicioNotificacion;
    }

    public void setFechaInicioNotificacion(Timestamp fechaInicioNotificacion) {
        this.fechaInicioNotificacion = fechaInicioNotificacion;
    }

    public Timestamp getFechaFinNotificacion() {
        return fechaFinNotificacion;
    }

    public void setFechaFinNotificacion(Timestamp fechaFinNotificacion) {
        this.fechaFinNotificacion = fechaFinNotificacion;
    }

    public static DocumentoFlt build(NotificacionBean notificacionBean) throws ParseException {
        DocumentoFlt documentoFlt = new DocumentoFlt();
        documentoFlt.setIdEmisor(notificacionBean.getNumeroDocumentoOrigen());
        documentoFlt.setTipoDocumento(notificacionBean.getTipoDocumento());
        documentoFlt.asignarRangoFechas(notificacionBean.getCoNotificacionDetalle());
        return documentoFlt;
    }

    private void asignarRangoFechas(String horaMinuto) throws ParseException {
        String hora = horaMinuto.substring(0, 2);
        String minuto = horaMinuto.substring(2, 4);
        Date date = new Date();
        SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStr = sdfDia.format(date);
        SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date hoy = sdfFull.parse(fechaStr + " " + hora + ":" + minuto);
        setFechaInicioNotificacion(new Timestamp(DateUtils.addDays(hoy, -1).getTime()));
        setFechaFinNotificacion(new Timestamp(DateUtils.addMinutes(hoy, -1).getTime()));


    }

    public static void main(String[] args) throws ParseException {
        String horaMinuto = "1200";

        String hora = horaMinuto.substring(0, 2);
        String minuto = horaMinuto.substring(2, 4);
        Date date = new Date();
        SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStr = sdfDia.format(date);
        SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date hoy = sdfFull.parse(fechaStr + " " + hora + ":" + minuto);
        System.out.println(DateUtils.addDays(hoy, -1));
        System.out.println(DateUtils.addMinutes(hoy, -1));
    }

    public List<String> getEstadoSunatList() {
        return estadoSunatList;
    }

    public void setEstadoSunatList(List<String> estadoSunatList) {
        this.estadoSunatList = estadoSunatList;
    }

    public String getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(String idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getDeclararSunat() {
        return declararSunat;
    }

    public void setDeclararSunat(String declararSunat) {
        this.declararSunat = declararSunat;
    }

    public String getSerieGrupoDocumento() {
        return serieGrupoDocumento;
    }

    public void setSerieGrupoDocumento(String serieGrupoDocumento) {
        this.serieGrupoDocumento = serieGrupoDocumento;
    }

    public String getNumeroCorrelativoInicio() {
        return numeroCorrelativoInicio;
    }

    public void setNumeroCorrelativoInicio(String numeroCorrelativoInicio) {
        this.numeroCorrelativoInicio = numeroCorrelativoInicio;
    }

    public String getNumeroCorrelativoFin() {
        return numeroCorrelativoFin;
    }

    public void setNumeroCorrelativoFin(String numeroCorrelativoFin) {
        this.numeroCorrelativoFin = numeroCorrelativoFin;
    }

    public String getIdAdquiriente() {
        return idAdquiriente;
    }

    public void setIdAdquiriente(String idAdquiriente) {
        this.idAdquiriente = idAdquiriente;
    }

    public String getFechaFirmaInicio() {
        return fechaFirmaInicio;
    }

    public void setFechaFirmaInicio(String fechaFirmaInicio) {
        this.fechaFirmaInicio = fechaFirmaInicio;
    }

    public String getFechaFirmaFin() {
        return fechaFirmaFin;
    }

    public void setFechaFirmaFin(String fechaFirmaFin) {
        this.fechaFirmaFin = fechaFirmaFin;
    }

    public String getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(String estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public String getTipoDocumentoAdquiriente() {
        return tipoDocumentoAdquiriente;
    }

    public void setTipoDocumentoAdquiriente(String tipoDocumentoAdquiriente) {
        this.tipoDocumentoAdquiriente = tipoDocumentoAdquiriente;
    }

    public String getInBloqueo() {
        return inBloqueo;
    }

    public void setInBloqueo(String inBloqueo) {
        this.inBloqueo = inBloqueo;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public boolean isSoloNuevos() {
        return soloNuevos;
    }

    public void setSoloNuevos(boolean soloNuevos) {
        this.soloNuevos = soloNuevos;
    }
}
