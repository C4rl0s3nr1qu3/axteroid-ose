package com.axteroid.ose.server.ubl20.bean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;

import java.io.Serializable;

public class NotificacionBean implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(NotificacionBean.class);
    private String numeroDocumentoOrigen;
    private String razonSocial;

    private String numeroDocumentoDestino;
    private Long idRolEmpresarialOrigen;
    private Long inHabilitado;
    private Long tipoNotificacion;
    private String tipoDocumento;
    private String coNotificacionDetalle;
    private String coOpcionTipoNotificacion;
    private String noOpcionTipoNotificacion;
    private String correos;

    public NotificacionBean() {


    }

    public NotificacionBean(String coOpcionTipoNotificacion, String coNotificacionDetalle,String correos) {
        this.coOpcionTipoNotificacion = coOpcionTipoNotificacion;
        this.coNotificacionDetalle = coNotificacionDetalle;
        this.correos= correos;
    }

    public String getNumeroDocumentoOrigen() {
        return numeroDocumentoOrigen;
    }

    public boolean esValida() {
        return Constantes.SUNAT_ESTADOCP_ACEPTADO.equals(String.valueOf(getInHabilitado()))
                && StringUtils.isNotBlank(getCorreos());
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setNumeroDocumentoOrigen(String numeroDocumentoOrigen) {
        this.numeroDocumentoOrigen = numeroDocumentoOrigen;
    }

    public String getNumeroDocumentoDestino() {
        return numeroDocumentoDestino;
    }

    public void setNumeroDocumentoDestino(String numeroDocumentoDestino) {
        this.numeroDocumentoDestino = numeroDocumentoDestino;
    }

    public Long getIdRolEmpresarialOrigen() {
        return idRolEmpresarialOrigen;
    }

    public void setIdRolEmpresarialOrigen(Long idRolEmpresarialOrigen) {
        this.idRolEmpresarialOrigen = idRolEmpresarialOrigen;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getCoNotificacionDetalle() {
        return coNotificacionDetalle;
    }

    public void setCoNotificacionDetalle(String coNotificacionDetalle) {
        this.coNotificacionDetalle = coNotificacionDetalle;
    }

    public String getCoOpcionTipoNotificacion() {
        return coOpcionTipoNotificacion;
    }

    public void setCoOpcionTipoNotificacion(String coOpcionTipoNotificacion) {
        this.coOpcionTipoNotificacion = coOpcionTipoNotificacion;
    }

    public String getNoOpcionTipoNotificacion() {
        return noOpcionTipoNotificacion;
    }

    public void setNoOpcionTipoNotificacion(String noOpcionTipoNotificacion) {
        this.noOpcionTipoNotificacion = noOpcionTipoNotificacion;
    }

    public String getCorreos() {
        return correos;
    }

    public void setCorreos(String correos) {
        this.correos = correos;
    }

    public Long getInHabilitado() {
        return inHabilitado;
    }

    public void setInHabilitado(Long inHabilitado) {
        this.inHabilitado = inHabilitado;
    }

    public Long getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(Long tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public boolean esProgramadoPorDia() {
        return !(StringUtils.isBlank(getCoNotificacionDetalle()) || esProgramadoPorHoras());
    }

    public boolean esEjecutable(Integer horaActual) {
        LOG.info("reporte " + getCoOpcionTipoNotificacion() + " <" + getNumeroDocumentoOrigen() + "> " + getCoNotificacionDetalle() + " para la hora actual <" + horaActual + ">");
        if (!NumberUtils.isNumber(getCoNotificacionDetalle())) {
            LOG.debug("reporte " + getCoOpcionTipoNotificacion() + " no tiene hora configurada. Nùmero documento origen " + getNumeroDocumentoOrigen()+ " Razon Social " + getRazonSocial() );
            return false;
        }
        Integer configurado = new Integer(getCoNotificacionDetalle());
        int diferencia = (horaActual - configurado);
        LOG.debug("diferencia <" + diferencia + ">");
        return diferencia >= 0 && diferencia < 10;
    }

    @Override
    public String toString() {
        return "NotificacionBean{" +
                "numeroDocumentoOrigen='" + numeroDocumentoOrigen + '\'' +
                ", numeroDocumentoDestino='" + numeroDocumentoDestino + '\'' +
                ", idRolEmpresarialOrigen=" + idRolEmpresarialOrigen +
                ", inHabilitado=" + inHabilitado +
                ", tipoNotificacion=" + tipoNotificacion +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", coNotificacionDetalle='" + coNotificacionDetalle + '\'' +
                ", coOpcionTipoNotificacion='" + coOpcionTipoNotificacion + '\'' +
                ", noOpcionTipoNotificacion='" + noOpcionTipoNotificacion + '\'' +
                ", correos='" + correos + '\'' +
                '}';
    }

    public boolean esProgramadoPorHoras() {
        return StringUtils.isNotBlank(getCoNotificacionDetalle()) && getCoNotificacionDetalle().endsWith("H");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificacionBean that = (NotificacionBean) o;

        if (!coOpcionTipoNotificacion.equals(that.coOpcionTipoNotificacion)) return false;
        if (!idRolEmpresarialOrigen.equals(that.idRolEmpresarialOrigen)) return false;
        if (!tipoDocumento.equals(that.tipoDocumento)) return false;
        if (!tipoNotificacion.equals(that.tipoNotificacion)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idRolEmpresarialOrigen.hashCode();
        result = 31 * result + tipoNotificacion.hashCode();
        result = 31 * result + tipoDocumento.hashCode();
        result = 31 * result + coOpcionTipoNotificacion.hashCode();
        return result;
    }

    public int obtenerPlazoDeHoras() {
        if (StringUtils.isBlank(getCoNotificacionDetalle())) return 0;

        if (getCoNotificacionDetalle().endsWith("H")) {
            String cadena = getCoNotificacionDetalle().substring(0, getCoNotificacionDetalle().length() - 1);
            return NumberUtils.isNumber(cadena) ? Integer.parseInt(cadena) : 0;
        }
        return 0;
    }
}
