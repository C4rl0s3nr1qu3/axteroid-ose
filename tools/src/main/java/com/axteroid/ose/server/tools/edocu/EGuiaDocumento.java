package com.axteroid.ose.server.tools.edocu;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.exception.GeneralException;
import com.axteroid.ose.server.tools.ubltype.TypeOrderReference;
import com.axteroid.ose.server.tools.util.FileUtil;
import com.axteroid.ose.server.tools.util.NumberUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class EGuiaDocumento implements IDocumento,IEDocumento {

	
	//GUIA ELECTRONICA /DespatchAdvice
	public String versionUBL = "2.1";
    public String version = Constantes.SUNAT_CUSTOMIZA_10;
    public String serieNumeroGuia;
    public Date fechaEmisionGuia;
    public String tipoDocumentoGuia;  
	public String observaciones;
	public String serieGuiaBaja;
	public String codigoGuiaBaja;    
	public String tipoGuiaBaja;
	public String numeroDocumentoRelacionado;
	public String codigoDocumentoRelacionado;
    public String rucRazonSocialRemitente;
    public String numeroDocumentoRemitente;
    public String tipoDocumentoRemitente;
    public String razonSocialRemitente;
    public String numeroDocumentoDestinatario;
    public String tipoDocumentoDestinatario;
    public String razonSocialDestinatario;	  
    public String numeroDocumentoEstablecimiento;  
    public String tipoDocumentoEstablecimiento;   
    public String razonSocialEstablecimiento;	 
    public String IDShipment = "IDShipment";
    public String motivoTraslado;
    public String descripcionMotivoTraslado;
    public boolean indTransbordoProgramado;
    public BigDecimal pesoBrutoTotalBienes;
    public String unidadMedidaPesoBruto;
    public BigDecimal numeroBultos;
    public String modalidadTraslado;
    public Date fechaInicioTraslado;
    public String numeroRucTransportista;
    public String tipoDocumentoTransportista;
    public String razonSocialTransportista;  
    public String numeroPlacaVehiculo; 
    public String numeroDocumentoConductor;   
    public String tipoDocumentoConductor;   
    public String ubigeoPtoLLegada; 
    public String direccionPtoLLegada;
    public String numeroContenedor;
    public String ubigeoPtoPartida;
    public String direccionPtoPartida;
    public String codigoPuerto;

    //ITEMS
    public List<EGuiaDocumentoItem> items = new ArrayList<EGuiaDocumentoItem>();
    public String correoEmisor;
    public String correoAdquiriente;    
    public Integer inHabilitado = 1;    
    private String rutaAdjunto_1;
    private String rutaAdjunto_2;
    private String rutaAdjunto_3;
    private String rutaAdjunto_4;
    private String rutaAdjunto_5;
    private byte[] adjunto_1;
    private byte[] adjunto_2;
    private byte[] adjunto_3;
    private byte[] adjunto_4;
    private byte[] adjunto_5;
    private String nombreAdjunto_1;
    private String nombreAdjunto_2;
    private String nombreAdjunto_3;
    private String nombreAdjunto_4;
    private String nombreAdjunto_5;    
    public String coTipoEmision;    
    private String hashCode;
    private String numIdCd;   
    private TypeOrderReference typeOrderReference;
    private String issueTime;    
    private Date dategetNotBefore;
    private Date dateNotAfter;
//    @Length(max = 4, message = "7145|codigoAuxiliar100")
//    private String codigoAuxiliar100_1;
//    @Length(max = 100, message = "7155|textoAuxiliar100")
//    private String textoAuxiliar100_1;
//    @Length(max = 4, message = "7145|codigoAuxiliar100")
//    private String codigoAuxiliar100_2;
//    @Length(max = 100, message = "7155|textoAuxiliar100")
//    private String textoAuxiliar100_2;
//    @Length(max = 4, message = "7145|codigoAuxiliar100")
//    private String codigoAuxiliar100_3;
//    @Length(max = 100, message = "7155|textoAuxiliar100")
//    private String textoAuxiliar100_3;
//    @Length(max = 4, message = "7145|codigoAuxiliar100")
//    private String codigoAuxiliar100_4;
//    @Length(max = 100, message = "7155|textoAuxiliar100")
//    private String textoAuxiliar100_4;
//    @Length(max = 4, message = "7145|codigoAuxiliar100")
//    private String codigoAuxiliar100_5;
//    @Length(max = 100, message = "7155|textoAuxiliar100")
//    private String textoAuxiliar100_5;
//    @Length(max = 4, message = "7145|codigoAuxiliar100")
//    private String codigoAuxiliar100_6;
//    @Length(max = 100, message = "7155|textoAuxiliar100")
//    private String textoAuxiliar100_6;
//    @Length(max = 4, message = "7145|codigoAuxiliar100")
//    private String codigoAuxiliar100_7;
//    @Length(max = 100, message = "7155|textoAuxiliar100")
//    private String textoAuxiliar100_7;
//    @Length(max = 4, message = "7145|codigoAuxiliar100")
//    private String codigoAuxiliar100_8;
//    @Length(max = 100, message = "7155|textoAuxiliar100")
//    private String textoAuxiliar100_8;
//    @Length(max = 4, message = "7145|codigoAuxiliar100")
//    private String codigoAuxiliar100_9;
//    @Length(max = 100, message = "7155|textoAuxiliar100")
//    private String textoAuxiliar100_9;
//    @Length(max = 4, message = "7145|codigoAuxiliar100")
//    private String codigoAuxiliar100_10;
//    @Length(max = 100, message = "7155|textoAuxiliar100")
//    private String textoAuxiliar100_10;
//    
//    @Length(max = 4, message = "7145|codigoAuxiliar40")
//    private String codigoAuxiliar40_1;
//    @Length(max = 40, message = "7155|textoAuxiliar40")
//    private String textoAuxiliar40_1;
//    @Length(max = 4, message = "7145|codigoAuxiliar40")
//    private String codigoAuxiliar40_2;
//    @Length(max = 40, message = "7155|textoAuxiliar40")
//    private String textoAuxiliar40_2;
//    @Length(max = 4, message = "7145|codigoAuxiliar40")
//    private String codigoAuxiliar40_3;
//    @Length(max = 40, message = "7155|textoAuxiliar40")
//    private String textoAuxiliar40_3;
//    @Length(max = 4, message = "7145|codigoAuxiliar40")
//    private String codigoAuxiliar40_4;
//    @Length(max = 40, message = "7155|textoAuxiliar40")
//    private String textoAuxiliar40_4;
//    @Length(max = 4, message = "7145|codigoAuxiliar40")
//    private String codigoAuxiliar40_5;
//    @Length(max = 40, message = "7155|textoAuxiliar40")
//    private String textoAuxiliar40_5;
//    @Length(max = 4, message = "7145|codigoAuxiliar40")
//    private String codigoAuxiliar40_6;
//    @Length(max = 40, message = "7155|textoAuxiliar40")
//    private String textoAuxiliar40_6;
//    @Length(max = 4, message = "7145|codigoAuxiliar40")
//    private String codigoAuxiliar40_7;
//    @Length(max = 40, message = "7155|textoAuxiliar40")
//    private String textoAuxiliar40_7;
//    @Length(max = 4, message = "7145|codigoAuxiliar40")
//    private String codigoAuxiliar40_8;
//    @Length(max = 40, message = "7155|textoAuxiliar40")
//    private String textoAuxiliar40_8;
//    @Length(max = 4, message = "7145|codigoAuxiliar40")
//    private String codigoAuxiliar40_9;
//    @Length(max = 40, message = "7155|textoAuxiliar40")
//    private String textoAuxiliar40_9;
//    @Length(max = 4, message = "7145|codigoAuxiliar40")
//    private String codigoAuxiliar40_10;
//    @Length(max = 40, message = "7155|textoAuxiliar40")
//    private String textoAuxiliar40_10;
//    
//    @Length(max = 4, message = "7145|codigoAuxiliar500")
//    private String codigoAuxiliar500_1;
//    @Length(max = 500, message = "7155|textoAuxiliar500")
//    private String textoAuxiliar500_1;
//    @Length(max = 4, message = "7145|codigoAuxiliar500")
//    private String codigoAuxiliar500_2;
//    @Length(max = 500, message = "7155|textoAuxiliar500")
//    private String textoAuxiliar500_2;
//    @Length(max = 4, message = "7145|codigoAuxiliar500")
//    private String codigoAuxiliar500_3;
//    @Length(max = 500, message = "7155|textoAuxiliar500")
//    private String textoAuxiliar500_3;
//    @Length(max = 4, message = "7145|codigoAuxiliar500")
//    private String codigoAuxiliar500_4;
//    @Length(max = 500, message = "7155|textoAuxiliar500")
//    private String textoAuxiliar500_4;
//    @Length(max = 4, message = "7145|codigoAuxiliar500")
//    private String codigoAuxiliar500_5;
//    @Length(max = 500, message = "7155|textoAuxiliar500")
//    private String textoAuxiliar500_5;    
//    @Length(max = 4, message = "7145|codigoAuxiliar500")
//    private String codigoAuxiliar500_6;
//    @Length(max = 500, message = "7155|textoAuxiliar500")
//    private String textoAuxiliar500_6;
//    @Length(max = 4, message = "7145|codigoAuxiliar500")
//    private String codigoAuxiliar500_7;
//    @Length(max = 500, message = "7155|textoAuxiliar500")
//    private String textoAuxiliar500_7;
//    @Length(max = 4, message = "7145|codigoAuxiliar500")
//    private String codigoAuxiliar500_8;
//    @Length(max = 500, message = "7155|textoAuxiliar500")
//    private String textoAuxiliar500_8;
//    @Length(max = 4, message = "7145|codigoAuxiliar500")
//    private String codigoAuxiliar500_9;
//    @Length(max = 500, message = "7155|textoAuxiliar500")
//    private String textoAuxiliar500_9;
//    @Length(max = 4, message = "7145|codigoAuxiliar500")
//    private String codigoAuxiliar500_10;
//    @Length(max = 500, message = "7155|textoAuxiliar500")
//    private String textoAuxiliar500_10;
//    
//    @Length(max = 4, message = "7145|codigoAuxiliar250")
//    private String codigoAuxiliar250_1;
//    @Length(max = 250, message = "7155|textoAuxiliar250")
//    private String textoAuxiliar250_1;
//    @Length(max = 4, message = "7145|codigoAuxiliar250")
//    private String codigoAuxiliar250_2;
//    @Length(max = 250, message = "7155|textoAuxiliar250")
//    private String textoAuxiliar250_2;
//    @Length(max = 4, message = "7145|codigoAuxiliar250")
//    private String codigoAuxiliar250_3;
//    @Length(max = 250, message = "7155|textoAuxiliar250")
//    private String textoAuxiliar250_3;
//    @Length(max = 4, message = "7145|codigoAuxiliar250")
//    private String codigoAuxiliar250_4;
//    @Length(max = 250, message = "7155|textoAuxiliar250")
//    private String textoAuxiliar250_4;
//    @Length(max = 4, message = "7145|codigoAuxiliar250")
//    private String codigoAuxiliar250_5;
//    @Length(max = 250, message = "7155|textoAuxiliar250")
//    private String textoAuxiliar250_5;
//    @Length(max = 4, message = "7145|codigoAuxiliar250")
//    private String codigoAuxiliar250_6;
//    @Length(max = 250, message = "7155|textoAuxiliar250")
//    private String textoAuxiliar250_6;
//    @Length(max = 4, message = "7145|codigoAuxiliar250")
//    private String codigoAuxiliar250_7;
//    @Length(max = 250, message = "7155|textoAuxiliar250")
//    private String textoAuxiliar250_7;
//    @Length(max = 4, message = "7145|codigoAuxiliar250")
//    private String codigoAuxiliar250_8;
//    @Length(max = 250, message = "7155|textoAuxiliar250")
//    private String textoAuxiliar250_8;
//    @Length(max = 4, message = "7145|codigoAuxiliar250")
//    private String codigoAuxiliar250_9;
//    @Length(max = 250, message = "7155|textoAuxiliar250")
//    private String textoAuxiliar250_9;
//    @Length(max = 4, message = "7145|codigoAuxiliar250")
//    private String codigoAuxiliar250_10;
//    @Length(max = 250, message = "7155|textoAuxiliar250")
//    private String textoAuxiliar250_10;      
    
    public String getRutaAdjunto_1() {
        return rutaAdjunto_1;
    }

    public void setRutaAdjunto_1(String rutaAdjunto_1) {
        this.rutaAdjunto_1 = rutaAdjunto_1;
    }

    public String getRutaAdjunto_2() {
        return rutaAdjunto_2;
    }

    public void setRutaAdjunto_2(String rutaAdjunto_2) {
        this.rutaAdjunto_2 = rutaAdjunto_2;
    }

    public String getRutaAdjunto_3() {
        return rutaAdjunto_3;
    }

    public void setRutaAdjunto_3(String rutaAdjunto_3) {
        this.rutaAdjunto_3 = rutaAdjunto_3;
    }

    public String getRutaAdjunto_4() {
        return rutaAdjunto_4;
    }

    public void setRutaAdjunto_4(String rutaAdjunto_4) {
        this.rutaAdjunto_4 = rutaAdjunto_4;
    }

    public String getRutaAdjunto_5() {
        return rutaAdjunto_5;
    }

    public void setRutaAdjunto_5(String rutaAdjunto_5) {
        this.rutaAdjunto_5 = rutaAdjunto_5;
    }
    
    public List<File> obtenerListaAdjuntosEnLan(String rutaParcial) {
        List<File> result = new ArrayList<File>();
        if (StringUtils.isNotBlank(getRutaAdjunto_1())) {
            result.add(new File(rutaParcial + getRutaAdjunto_1()));
        }
        if (StringUtils.isNotBlank(getRutaAdjunto_2())) {
            result.add(new File(rutaParcial + getRutaAdjunto_2()));
        }
        if (StringUtils.isNotBlank(getRutaAdjunto_3())) {
            result.add(new File(rutaParcial + getRutaAdjunto_3()));
        }
        if (StringUtils.isNotBlank(getRutaAdjunto_4())) {
            result.add(new File(rutaParcial + getRutaAdjunto_4()));
        }
        if (StringUtils.isNotBlank(getRutaAdjunto_5())) {
            result.add(new File(rutaParcial + getRutaAdjunto_5()));
        }
        return result;
    }
    
    public List<File> obtenerListaAdjuntosEnServer(EDocumento documento) {
        List<File> result = new ArrayList<File>();

        try {
            if (getAdjunto_1() != null) {
                result.add(FileUtil.writeToFile(getNombreAdjunto_1(), getAdjunto_1()));
            }
            if (getAdjunto_2() != null) {
                result.add(FileUtil.writeToFile(getNombreAdjunto_2(), getAdjunto_2()));
            }
            if (getAdjunto_3() != null) {
                result.add(FileUtil.writeToFile(getNombreAdjunto_3(), getAdjunto_3()));
            }
            if (getAdjunto_4() != null) {
                result.add(FileUtil.writeToFile(getNombreAdjunto_4(), getAdjunto_4()));
            }
            if (getAdjunto_5() != null) {
                result.add(FileUtil.writeToFile(getNombreAdjunto_5(), getAdjunto_5()));
            }
        } catch (Exception e) {
            throw new GeneralException("7182", "No se pudo convertir los adjuntos a archivos");

        }
        return result;
    }
    
    public void validarNombreArchivoAdjunto(EDocumento documento) {
        List<String> result = new ArrayList<String>();


        if (getNombreAdjunto_1() != null && getNombreAdjunto_1().length() <= 20) {
            result.add(getNombreAdjunto_1());
        }
        if (getNombreAdjunto_2() != null && getNombreAdjunto_2().length() <= 20) {
            result.add(getNombreAdjunto_2());
        }
        if (getNombreAdjunto_3() != null && getNombreAdjunto_3().length() <= 20) {
            result.add(getNombreAdjunto_3());
        }
        if (getNombreAdjunto_4() != null && getNombreAdjunto_4().length() <= 20) {
            result.add(getNombreAdjunto_4());
        }
        if (getNombreAdjunto_5() != null && getNombreAdjunto_5().length() <= 20) {
            result.add(getNombreAdjunto_5());
        }

        if (result.size() > 0) {
            //throw new GeneralException("7183", "El nombre de los archivos adjuntos supera el límite permitido de 20 caracteres: " + ListUtil.getRepeatList(new HashSet<String>(result), result));
            throw new GeneralException("7183", "El nombre de los archivos adjuntos supera el límite permitido de 20 caracteres: ");

        
        }

        return;
    }
    
    public byte[] getAdjunto_1() {
        return adjunto_1;
    }

    public void setAdjunto_1(byte[] adjunto_1) {
        this.adjunto_1 = adjunto_1;
    }

    public byte[] getAdjunto_2() {
        return adjunto_2;
    }

    public void setAdjunto_2(byte[] adjunto_2) {
        this.adjunto_2 = adjunto_2;
    }

    public byte[] getAdjunto_3() {
        return adjunto_3;
    }

    public void setAdjunto_3(byte[] adjunto_3) {
        this.adjunto_3 = adjunto_3;
    }

    public byte[] getAdjunto_4() {
        return adjunto_4;
    }

    public void setAdjunto_4(byte[] adjunto_4) {
        this.adjunto_4 = adjunto_4;
    }

    public byte[] getAdjunto_5() {
        return adjunto_5;
    }

    public void setAdjunto_5(byte[] adjunto_5) {
        this.adjunto_5 = adjunto_5;
    }

    public String getNombreAdjunto_1() {
        return nombreAdjunto_1;
    }

    public void setNombreAdjunto_1(String nombreAdjunto_1) {
        this.nombreAdjunto_1 = nombreAdjunto_1;
    }

    public String getNombreAdjunto_2() {
        return nombreAdjunto_2;
    }

    public void setNombreAdjunto_2(String nombreAdjunto_2) {
        this.nombreAdjunto_2 = nombreAdjunto_2;
    }

    public String getNombreAdjunto_3() {
        return nombreAdjunto_3;
    }

    public void setNombreAdjunto_3(String nombreAdjunto_3) {
        this.nombreAdjunto_3 = nombreAdjunto_3;
    }

    public String getNombreAdjunto_4() {
        return nombreAdjunto_4;
    }

    public void setNombreAdjunto_4(String nombreAdjunto_4) {
        this.nombreAdjunto_4 = nombreAdjunto_4;
    }

    public String getNombreAdjunto_5() {
        return nombreAdjunto_5;
    }

    public void setNombreAdjunto_5(String nombreAdjunto_5) {
        this.nombreAdjunto_5 = nombreAdjunto_5;
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

	public String getSerieNumeroGuia() {
//	public String getIdDocumento() {
		return serieNumeroGuia;
	}

	public void setSerieNumeroGuia(String serieNumeroGuia) {
//	public void setIdDocumento(String serieNumeroGuia) {
		this.serieNumeroGuia = serieNumeroGuia;
	}

	public Date getFechaEmisionGuia() {
//	public Date getFechaDocumento() {
		return fechaEmisionGuia;
	}

	public void setFechaEmisionGuia(Date fechaEmisionGuia) {
//	public void setFechaDocumento(Date fechaEmisionGuia) {
		this.fechaEmisionGuia = fechaEmisionGuia;
	}

	public String getTipoDocumentoGuia() {
//	public String getTipoDocumento() {
		return tipoDocumentoGuia;
	}

	public void setTipoDocumentoGuia(String tipoDocumentoGuia) {
//	public void setTipoDocumento(String tipoDocumentoGuia) {
		this.tipoDocumentoGuia = tipoDocumentoGuia;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getSerieGuiaBaja() {
		return serieGuiaBaja;
	}

	public void setSerieGuiaBaja(String serieGuiaBaja) {
		this.serieGuiaBaja = serieGuiaBaja;
	}

	public String getCodigoGuiaBaja() {
		return codigoGuiaBaja;
	}

	public void setCodigoGuiaBaja(String codigoGuiaBaja) {
		this.codigoGuiaBaja = codigoGuiaBaja;
	}

	public String getTipoGuiaBaja() {
		return tipoGuiaBaja;
	}

	public void setTipoGuiaBaja(String tipoGuiaBaja) {
		this.tipoGuiaBaja = tipoGuiaBaja;
	}

	public String getNumeroDocumentoRelacionado() {
		return numeroDocumentoRelacionado;
	}

	public void setNumeroDocumentoRelacionado(String numeroDocumentoRelacionado) {
		this.numeroDocumentoRelacionado = numeroDocumentoRelacionado;
	}

	public String getCodigoDocumentoRelacionado() {
		return codigoDocumentoRelacionado;
	}

	public void setCodigoDocumentoRelacionado(String codigoDocumentoRelacionado) {
		this.codigoDocumentoRelacionado = codigoDocumentoRelacionado;
	}

	public String getRucRazonSocialRemitente() {
		return numeroDocumentoRemitente + "|" + razonSocialRemitente;
	}

	public void setRucRazonSocialRemitente(String rucRazonSocialRemitente) {
		this.rucRazonSocialRemitente = rucRazonSocialRemitente;
	}

	public String getNumeroDocumentoRemitente() {
		return numeroDocumentoRemitente;
	}

	public void setNumeroDocumentoRemitente(String numeroDocumentoRemitente) {
		this.numeroDocumentoRemitente = numeroDocumentoRemitente;
	}

	public String getTipoDocumentoRemitente() {
		return tipoDocumentoRemitente;
	}

	public void setTipoDocumentoRemitente(String tipoDocumentoRemitente) {
		this.tipoDocumentoRemitente = tipoDocumentoRemitente;
	}

	public String getRazonSocialRemitente() {
		return razonSocialRemitente;
	}

	public void setRazonSocialRemitente(String razonSocialRemitente) {
		this.razonSocialRemitente = razonSocialRemitente;
	}

	public String getNumeroDocumentoDestinatario() {
		return numeroDocumentoDestinatario;
	}

	public void setNumeroDocumentoDestinatario(String numeroDocumentoDestinatario) {
		this.numeroDocumentoDestinatario = numeroDocumentoDestinatario;
	}

	public String getTipoDocumentoDestinatario() {
		return tipoDocumentoDestinatario;
	}

	public void setTipoDocumentoDestinatario(String tipoDocumentoDestinatario) {
		this.tipoDocumentoDestinatario = tipoDocumentoDestinatario;
	}

	public String getRazonSocialDestinatario() {
		return razonSocialDestinatario;
	}

	public void setRazonSocialDestinatario(String razonSocialDestinatario) {
		this.razonSocialDestinatario = razonSocialDestinatario;
	}

	public String getNumeroDocumentoEstablecimiento() {
		return numeroDocumentoEstablecimiento;
	}

	public void setNumeroDocumentoEstablecimiento(
			String numeroDocumentoEstablecimiento) {
		this.numeroDocumentoEstablecimiento = numeroDocumentoEstablecimiento;
	}

	public String getTipoDocumentoEstablecimiento() {
		return tipoDocumentoEstablecimiento;
	}

	public void setTipoDocumentoEstablecimiento(String tipoDocumentoEstablecimiento) {
		this.tipoDocumentoEstablecimiento = tipoDocumentoEstablecimiento;
	}

	public String getRazonSocialEstablecimiento() {
		return razonSocialEstablecimiento;
	}

	public void setRazonSocialEstablecimiento(String razonSocialEstablecimiento) {
		this.razonSocialEstablecimiento = razonSocialEstablecimiento;
	}

	public String getMotivoTraslado() {
		return motivoTraslado;
	}

	public void setMotivoTraslado(String motivoTraslado) {
		this.motivoTraslado = motivoTraslado;
	}

	public String getdescripcionMotivoTraslado() {
		return descripcionMotivoTraslado;
	}

	public void setdescripcionMotivoTraslado(String descripcionMotivoTraslado) {
		this.descripcionMotivoTraslado = descripcionMotivoTraslado;
	}

	public boolean isIndTransbordoProgramado() {
		return indTransbordoProgramado;
	}

	public void setIndTransbordoProgramado(boolean indTransbordoProgramado) {
		this.indTransbordoProgramado = indTransbordoProgramado;
	}

	public BigDecimal getPesoBrutoTotalBienes() {
		return pesoBrutoTotalBienes;
	}

	public void setPesoBrutoTotalBienes(BigDecimal pesoBrutoTotalBienes) {
		this.pesoBrutoTotalBienes = pesoBrutoTotalBienes;
	}

	public String getUnidadMedidaPesoBruto() {
		return unidadMedidaPesoBruto;
	}

	public void setUnidadMedidaPesoBruto(String unidadMedidaPesoBruto) {
		this.unidadMedidaPesoBruto = unidadMedidaPesoBruto;
	}

	public String getDescripcionMotivoTraslado() {
		return descripcionMotivoTraslado;
	}

	public void setDescripcionMotivoTraslado(String descripcionMotivoTraslado) {
		this.descripcionMotivoTraslado = descripcionMotivoTraslado;
	}

	public BigDecimal getNumeroBultos() {
		return numeroBultos;
	}

	public void setNumeroBultos(BigDecimal numeroBultos) {
		numeroBultos.setScale(0);
		this.numeroBultos = numeroBultos;
	}

	public String getModalidadTraslado() {
		return modalidadTraslado;
	}

	public void setModalidadTraslado(String modalidadTraslado) {
		this.modalidadTraslado = modalidadTraslado;
	}

	public Date getFechaInicioTraslado() {
		return fechaInicioTraslado;
	}

	public void setFechaInicioTraslado(Date fechaInicioTraslado) {
		this.fechaInicioTraslado = fechaInicioTraslado;
	}

	public String getNumeroRucTransportista() {
		return numeroRucTransportista;
	}

	public void setNumeroRucTransportista(String numeroRucTransportista) {
		this.numeroRucTransportista = numeroRucTransportista;
	}

	public String getTipoDocumentoTransportista() {
		return tipoDocumentoTransportista;
	}

	public void setTipoDocumentoTransportista(String tipoDocumentoTransportista) {
		this.tipoDocumentoTransportista = tipoDocumentoTransportista;
	}

	public String getRazonSocialTransportista() {
		return razonSocialTransportista;
	}

	public void setRazonSocialTransportista(String razonSocialTransportista) {
		this.razonSocialTransportista = razonSocialTransportista;
	}

	public String getNumeroPlacaVehiculo() {
		return numeroPlacaVehiculo;
	}

	public void setNumeroPlacaVehiculo(String numeroPlacaVehiculo) {
		this.numeroPlacaVehiculo = numeroPlacaVehiculo;
	}

	public String getNumeroDocumentoConductor() {
		return numeroDocumentoConductor;
	}

	public void setNumeroDocumentoConductor(String numeroDocumentoConductor) {
		this.numeroDocumentoConductor = numeroDocumentoConductor;
	}

	public String getTipoDocumentoConductor() {
		return tipoDocumentoConductor;
	}

	public void setTipoDocumentoConductor(String tipoDocumentoConductor) {
		this.tipoDocumentoConductor = tipoDocumentoConductor;
	}

	public String getUbigeoPtoLLegada() {
		return ubigeoPtoLLegada;
	}

	public void setUbigeoPtoLLegada(String ubigeoPtoLLegada) {
		this.ubigeoPtoLLegada = ubigeoPtoLLegada;
	}

	public String getDireccionPtoLLegada() {
		return direccionPtoLLegada;
	}

	public void setDireccionPtoLLegada(String direccionPtoLLegada) {
		this.direccionPtoLLegada = direccionPtoLLegada;
	}

	public String getNumeroContenedor() {
		return numeroContenedor;
	}

	public void setNumeroContenedor(String numeroContenedor) {
		this.numeroContenedor = numeroContenedor;
	}

	public String getUbigeoPtoPartida() {
		return ubigeoPtoPartida;
	}

	public void setUbigeoPtoPartida(String ubigeoPtoPartida) {
		this.ubigeoPtoPartida = ubigeoPtoPartida;
	}

	public String getDireccionPtoPartida() {
		return direccionPtoPartida;
	}

	public void setDireccionPtoPartida(String direccionPtoPartida) {
		this.direccionPtoPartida = direccionPtoPartida;
	}

	public String getCodigoPuerto() {
		return codigoPuerto;
	}

	public void setCodigoPuerto(String codigoPuerto) {
		this.codigoPuerto = codigoPuerto;
	}

	public List<EGuiaDocumentoItem> getItems() {
		return items;
	}

	public void setItems(List<EGuiaDocumentoItem> items) {
		this.items = items;
	}

	public void populateItem() {
        if (StringUtils.isBlank(getTipoDocumentoGuia())) {
            setTipoDocumentoGuia("SN");
        }
        if (StringUtils.isBlank(getSerieNumeroGuia())) {
            setSerieNumeroGuia("SN" + new Date().getTime());
        }
	}

	public void setCorreoEmisor(String correoEmisor) {
		this.correoEmisor = correoEmisor;
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
	
	public String getSerieDocumento() {
		return getIdDocumento().substring(0, getIdDocumento().lastIndexOf('-'));
	}
	
	public String getNumeroDocumento() {
		return getIdDocumento().substring(getIdDocumento().lastIndexOf('-') + 1, getIdDocumento().length());
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

	public String getCorreoAdquiriente() {
		return correoAdquiriente;
	}

	public String getCorreoEmisor() {
		return correoEmisor;
	}

	public String getCoTipoEmision() {
		return coTipoEmision;
	}

	public void setCoTipoEmision(String coTipoEmision) {
		this.coTipoEmision = coTipoEmision;
	}

	public boolean isExportacion() {
		return false;
	}

	public boolean isNotaDebitoPorPenalidad() {
		return false;
	}

	public String getNumeroDocumentoEmisor() {
		return getNumeroDocumentoRemitente();
	}

	public String getTipoDocumentoEmisor() {
		return getTipoDocumentoRemitente();
	}

	public String getIdDocumento() {
		return getSerieNumeroGuia();
	}

	public String getTipoDocumento() {
		return getTipoDocumentoGuia();
	}

	public Date getFechaDocumento() {
		return getFechaEmisionGuia();
	}

	public String getTipoDocumentoAdquiriente() {
		return getTipoDocumentoDestinatario();
	}

	public String getNumeroDocumentoAdquiriente() {
		return getNumeroDocumentoDestinatario();
	}

	public String getRazonSocialAdquiriente() {
		return getRazonSocialDestinatario();
	}

	public String getRazonSocialEmisor() {
		return getRazonSocialRemitente();
	}
	
	public String buildCodigoTipoGuiaBaja() {
		return codigoGuiaBaja + "|" + tipoGuiaBaja;
	}
	
	public String buildNumeroDocumentoRemitente() {
		if(StringUtils.isBlank(numeroDocumentoRemitente)
				|| StringUtils.isBlank(tipoDocumentoRemitente)) {
			return null;
		}
		return StringUtil.blank(numeroDocumentoRemitente) + "|" + StringUtil.blank(tipoDocumentoRemitente);		
	}
	
	public String buildNumeroDocumentoDestinatario() {
		if(StringUtils.isBlank(numeroDocumentoDestinatario)
				|| StringUtils.isBlank(tipoDocumentoDestinatario)) {
			return null;
		}
		return StringUtil.blank(numeroDocumentoDestinatario) + "|" + StringUtil.blank(tipoDocumentoDestinatario);		
	}
	
	public String buildNumeroDocumentoEstablecimiento() {
		if(StringUtils.isBlank(numeroDocumentoEstablecimiento)
				|| StringUtils.isBlank(tipoDocumentoEstablecimiento)) {
			return null;
		}
		return StringUtil.blank(numeroDocumentoEstablecimiento) + "|" + StringUtil.blank(tipoDocumentoEstablecimiento);		
	}	
	
	public String buildPesoBrutoTotalBienes() {
	        if (StringUtils.isBlank(unidadMedidaPesoBruto) 
	        		|| pesoBrutoTotalBienes == null){
	        	return null;
	        }
	        return StringUtil.blank(unidadMedidaPesoBruto) + "|" + NumberUtil.toFormat(pesoBrutoTotalBienes);
	}
	
    public String buildTransportista() {
        if (StringUtils.isBlank(numeroRucTransportista) ||
                StringUtils.isBlank(tipoDocumentoTransportista)) {
            return null;
        }
        return StringUtil.blank(numeroRucTransportista) + "|" + StringUtil.blank(tipoDocumentoTransportista);
    }
    
    public String buildConductor(){
    	if(StringUtils.isBlank(numeroDocumentoConductor) || 
    			StringUtils.isBlank(tipoDocumentoConductor)) {
    		return null;
    	}
    	return StringUtil.blank(numeroDocumentoConductor) + "|" + StringUtil.blank(tipoDocumentoConductor);
    }
    
    public String buildDireccionPtoLlegada() {
        if (StringUtils.isBlank(ubigeoPtoLLegada) ||
                StringUtils.isBlank(direccionPtoLLegada)) {
            return null;
        }

        return StringUtil.blank(ubigeoPtoLLegada) + "|" +
                StringUtil.blank(direccionPtoLLegada);
    }
    
    public String buildDireccionPtoPartida() {
        if (StringUtils.isBlank(ubigeoPtoPartida) ||
                StringUtils.isBlank(direccionPtoPartida)) {
            return null;
        }

        return StringUtil.blank(ubigeoPtoPartida) + "|" +
                StringUtil.blank(direccionPtoPartida);
    }

	public String getIDShipment() {
		return IDShipment;
	}

	public void setIDShipment(String iDShipment) {
		IDShipment = iDShipment;
	}
	
//	public String getCodigoAuxiliar100_1() {
//		return codigoAuxiliar100_1;
//	}
//
//	public void setCodigoAuxiliar100_1(String codigoAuxiliar100_1) {
//		this.codigoAuxiliar100_1 = codigoAuxiliar100_1;
//	}
//
//	public String getTextoAuxiliar100_1() {
//		return textoAuxiliar100_1;
//	}
//
//	public void setTextoAuxiliar100_1(String textoAuxiliar100_1) {
//		this.textoAuxiliar100_1 = textoAuxiliar100_1;
//	}
//
//	public String getCodigoAuxiliar100_2() {
//		return codigoAuxiliar100_2;
//	}
//
//	public void setCodigoAuxiliar100_2(String codigoAuxiliar100_2) {
//		this.codigoAuxiliar100_2 = codigoAuxiliar100_2;
//	}
//
//	public String getTextoAuxiliar100_2() {
//		return textoAuxiliar100_2;
//	}
//
//	public void setTextoAuxiliar100_2(String textoAuxiliar100_2) {
//		this.textoAuxiliar100_2 = textoAuxiliar100_2;
//	}
//
//	public String getCodigoAuxiliar100_3() {
//		return codigoAuxiliar100_3;
//	}
//
//	public void setCodigoAuxiliar100_3(String codigoAuxiliar100_3) {
//		this.codigoAuxiliar100_3 = codigoAuxiliar100_3;
//	}
//
//	public String getTextoAuxiliar100_3() {
//		return textoAuxiliar100_3;
//	}
//
//	public void setTextoAuxiliar100_3(String textoAuxiliar100_3) {
//		this.textoAuxiliar100_3 = textoAuxiliar100_3;
//	}
//
//	public String getCodigoAuxiliar100_4() {
//		return codigoAuxiliar100_4;
//	}
//
//	public void setCodigoAuxiliar100_4(String codigoAuxiliar100_4) {
//		this.codigoAuxiliar100_4 = codigoAuxiliar100_4;
//	}
//
//	public String getTextoAuxiliar100_4() {
//		return textoAuxiliar100_4;
//	}
//
//	public void setTextoAuxiliar100_4(String textoAuxiliar100_4) {
//		this.textoAuxiliar100_4 = textoAuxiliar100_4;
//	}
//
//	public String getCodigoAuxiliar100_5() {
//		return codigoAuxiliar100_5;
//	}
//
//	public void setCodigoAuxiliar100_5(String codigoAuxiliar100_5) {
//		this.codigoAuxiliar100_5 = codigoAuxiliar100_5;
//	}
//
//	public String getTextoAuxiliar100_5() {
//		return textoAuxiliar100_5;
//	}
//
//	public void setTextoAuxiliar100_5(String textoAuxiliar100_5) {
//		this.textoAuxiliar100_5 = textoAuxiliar100_5;
//	}
//
//	public String getCodigoAuxiliar100_6() {
//		return codigoAuxiliar100_6;
//	}
//
//	public void setCodigoAuxiliar100_6(String codigoAuxiliar100_6) {
//		this.codigoAuxiliar100_6 = codigoAuxiliar100_6;
//	}
//
//	public String getTextoAuxiliar100_6() {
//		return textoAuxiliar100_6;
//	}
//
//	public void setTextoAuxiliar100_6(String textoAuxiliar100_6) {
//		this.textoAuxiliar100_6 = textoAuxiliar100_6;
//	}
//
//	public String getCodigoAuxiliar100_7() {
//		return codigoAuxiliar100_7;
//	}
//
//	public void setCodigoAuxiliar100_7(String codigoAuxiliar100_7) {
//		this.codigoAuxiliar100_7 = codigoAuxiliar100_7;
//	}
//
//	public String getTextoAuxiliar100_7() {
//		return textoAuxiliar100_7;
//	}
//
//	public void setTextoAuxiliar100_7(String textoAuxiliar100_7) {
//		this.textoAuxiliar100_7 = textoAuxiliar100_7;
//	}
//
//	public String getCodigoAuxiliar100_8() {
//		return codigoAuxiliar100_8;
//	}
//
//	public void setCodigoAuxiliar100_8(String codigoAuxiliar100_8) {
//		this.codigoAuxiliar100_8 = codigoAuxiliar100_8;
//	}
//
//	public String getTextoAuxiliar100_8() {
//		return textoAuxiliar100_8;
//	}
//
//	public void setTextoAuxiliar100_8(String textoAuxiliar100_8) {
//		this.textoAuxiliar100_8 = textoAuxiliar100_8;
//	}
//
//	public String getCodigoAuxiliar100_9() {
//		return codigoAuxiliar100_9;
//	}
//
//	public void setCodigoAuxiliar100_9(String codigoAuxiliar100_9) {
//		this.codigoAuxiliar100_9 = codigoAuxiliar100_9;
//	}
//
//	public String getTextoAuxiliar100_9() {
//		return textoAuxiliar100_9;
//	}
//
//	public void setTextoAuxiliar100_9(String textoAuxiliar100_9) {
//		this.textoAuxiliar100_9 = textoAuxiliar100_9;
//	}
//
//	public String getCodigoAuxiliar100_10() {
//		return codigoAuxiliar100_10;
//	}
//
//	public void setCodigoAuxiliar100_10(String codigoAuxiliar100_10) {
//		this.codigoAuxiliar100_10 = codigoAuxiliar100_10;
//	}
//
//	public String getTextoAuxiliar100_10() {
//		return textoAuxiliar100_10;
//	}
//
//	public void setTextoAuxiliar100_10(String textoAuxiliar100_10) {
//		this.textoAuxiliar100_10 = textoAuxiliar100_10;
//	}
//
//	public String getCodigoAuxiliar40_1() {
//		return codigoAuxiliar40_1;
//	}
//
//	public void setCodigoAuxiliar40_1(String codigoAuxiliar40_1) {
//		this.codigoAuxiliar40_1 = codigoAuxiliar40_1;
//	}
//
//	public String getTextoAuxiliar40_1() {
//		return textoAuxiliar40_1;
//	}
//
//	public void setTextoAuxiliar40_1(String textoAuxiliar40_1) {
//		this.textoAuxiliar40_1 = textoAuxiliar40_1;
//	}
//
//	public String getCodigoAuxiliar40_2() {
//		return codigoAuxiliar40_2;
//	}
//
//	public void setCodigoAuxiliar40_2(String codigoAuxiliar40_2) {
//		this.codigoAuxiliar40_2 = codigoAuxiliar40_2;
//	}
//
//	public String getTextoAuxiliar40_2() {
//		return textoAuxiliar40_2;
//	}
//
//	public void setTextoAuxiliar40_2(String textoAuxiliar40_2) {
//		this.textoAuxiliar40_2 = textoAuxiliar40_2;
//	}
//
//	public String getCodigoAuxiliar40_3() {
//		return codigoAuxiliar40_3;
//	}
//
//	public void setCodigoAuxiliar40_3(String codigoAuxiliar40_3) {
//		this.codigoAuxiliar40_3 = codigoAuxiliar40_3;
//	}
//
//	public String getTextoAuxiliar40_3() {
//		return textoAuxiliar40_3;
//	}
//
//	public void setTextoAuxiliar40_3(String textoAuxiliar40_3) {
//		this.textoAuxiliar40_3 = textoAuxiliar40_3;
//	}
//
//	public String getCodigoAuxiliar40_4() {
//		return codigoAuxiliar40_4;
//	}
//
//	public void setCodigoAuxiliar40_4(String codigoAuxiliar40_4) {
//		this.codigoAuxiliar40_4 = codigoAuxiliar40_4;
//	}
//
//	public String getTextoAuxiliar40_4() {
//		return textoAuxiliar40_4;
//	}
//
//	public void setTextoAuxiliar40_4(String textoAuxiliar40_4) {
//		this.textoAuxiliar40_4 = textoAuxiliar40_4;
//	}
//
//	public String getCodigoAuxiliar40_5() {
//		return codigoAuxiliar40_5;
//	}
//
//	public void setCodigoAuxiliar40_5(String codigoAuxiliar40_5) {
//		this.codigoAuxiliar40_5 = codigoAuxiliar40_5;
//	}
//
//	public String getTextoAuxiliar40_5() {
//		return textoAuxiliar40_5;
//	}
//
//	public void setTextoAuxiliar40_5(String textoAuxiliar40_5) {
//		this.textoAuxiliar40_5 = textoAuxiliar40_5;
//	}
//
//	public String getCodigoAuxiliar40_6() {
//		return codigoAuxiliar40_6;
//	}
//
//	public void setCodigoAuxiliar40_6(String codigoAuxiliar40_6) {
//		this.codigoAuxiliar40_6 = codigoAuxiliar40_6;
//	}
//
//	public String getTextoAuxiliar40_6() {
//		return textoAuxiliar40_6;
//	}
//
//	public void setTextoAuxiliar40_6(String textoAuxiliar40_6) {
//		this.textoAuxiliar40_6 = textoAuxiliar40_6;
//	}
//
//	public String getCodigoAuxiliar40_7() {
//		return codigoAuxiliar40_7;
//	}
//
//	public void setCodigoAuxiliar40_7(String codigoAuxiliar40_7) {
//		this.codigoAuxiliar40_7 = codigoAuxiliar40_7;
//	}
//
//	public String getTextoAuxiliar40_7() {
//		return textoAuxiliar40_7;
//	}
//
//	public void setTextoAuxiliar40_7(String textoAuxiliar40_7) {
//		this.textoAuxiliar40_7 = textoAuxiliar40_7;
//	}
//
//	public String getCodigoAuxiliar40_8() {
//		return codigoAuxiliar40_8;
//	}
//
//	public void setCodigoAuxiliar40_8(String codigoAuxiliar40_8) {
//		this.codigoAuxiliar40_8 = codigoAuxiliar40_8;
//	}
//
//	public String getTextoAuxiliar40_8() {
//		return textoAuxiliar40_8;
//	}
//
//	public void setTextoAuxiliar40_8(String textoAuxiliar40_8) {
//		this.textoAuxiliar40_8 = textoAuxiliar40_8;
//	}
//
//	public String getCodigoAuxiliar40_9() {
//		return codigoAuxiliar40_9;
//	}
//
//	public void setCodigoAuxiliar40_9(String codigoAuxiliar40_9) {
//		this.codigoAuxiliar40_9 = codigoAuxiliar40_9;
//	}
//
//	public String getTextoAuxiliar40_9() {
//		return textoAuxiliar40_9;
//	}
//
//	public void setTextoAuxiliar40_9(String textoAuxiliar40_9) {
//		this.textoAuxiliar40_9 = textoAuxiliar40_9;
//	}
//
//	public String getCodigoAuxiliar40_10() {
//		return codigoAuxiliar40_10;
//	}
//
//	public void setCodigoAuxiliar40_10(String codigoAuxiliar40_10) {
//		this.codigoAuxiliar40_10 = codigoAuxiliar40_10;
//	}
//
//	public String getTextoAuxiliar40_10() {
//		return textoAuxiliar40_10;
//	}
//
//	public void setTextoAuxiliar40_10(String textoAuxiliar40_10) {
//		this.textoAuxiliar40_10 = textoAuxiliar40_10;
//	}
//
//	public String getCodigoAuxiliar500_1() {
//		return codigoAuxiliar500_1;
//	}
//
//	public void setCodigoAuxiliar500_1(String codigoAuxiliar500_1) {
//		this.codigoAuxiliar500_1 = codigoAuxiliar500_1;
//	}
//
//	public String getTextoAuxiliar500_1() {
//		return textoAuxiliar500_1;
//	}
//
//	public void setTextoAuxiliar500_1(String textoAuxiliar500_1) {
//		this.textoAuxiliar500_1 = textoAuxiliar500_1;
//	}
//
//	public String getCodigoAuxiliar500_2() {
//		return codigoAuxiliar500_2;
//	}
//
//	public void setCodigoAuxiliar500_2(String codigoAuxiliar500_2) {
//		this.codigoAuxiliar500_2 = codigoAuxiliar500_2;
//	}
//
//	public String getTextoAuxiliar500_2() {
//		return textoAuxiliar500_2;
//	}
//
//	public void setTextoAuxiliar500_2(String textoAuxiliar500_2) {
//		this.textoAuxiliar500_2 = textoAuxiliar500_2;
//	}
//
//	public String getCodigoAuxiliar500_3() {
//		return codigoAuxiliar500_3;
//	}
//
//	public void setCodigoAuxiliar500_3(String codigoAuxiliar500_3) {
//		this.codigoAuxiliar500_3 = codigoAuxiliar500_3;
//	}
//
//	public String getTextoAuxiliar500_3() {
//		return textoAuxiliar500_3;
//	}
//
//	public void setTextoAuxiliar500_3(String textoAuxiliar500_3) {
//		this.textoAuxiliar500_3 = textoAuxiliar500_3;
//	}
//
//	public String getCodigoAuxiliar500_4() {
//		return codigoAuxiliar500_4;
//	}
//
//	public void setCodigoAuxiliar500_4(String codigoAuxiliar500_4) {
//		this.codigoAuxiliar500_4 = codigoAuxiliar500_4;
//	}
//
//	public String getTextoAuxiliar500_4() {
//		return textoAuxiliar500_4;
//	}
//
//	public void setTextoAuxiliar500_4(String textoAuxiliar500_4) {
//		this.textoAuxiliar500_4 = textoAuxiliar500_4;
//	}
//
//	public String getCodigoAuxiliar500_5() {
//		return codigoAuxiliar500_5;
//	}
//
//	public void setCodigoAuxiliar500_5(String codigoAuxiliar500_5) {
//		this.codigoAuxiliar500_5 = codigoAuxiliar500_5;
//	}
//
//	public String getTextoAuxiliar500_5() {
//		return textoAuxiliar500_5;
//	}
//
//	public void setTextoAuxiliar500_5(String textoAuxiliar500_5) {
//		this.textoAuxiliar500_5 = textoAuxiliar500_5;
//	}

//	public String getCodigoAuxiliar500_6() {
//		return codigoAuxiliar500_6;
//	}
//
//	public void setCodigoAuxiliar500_6(String codigoAuxiliar500_6) {
//		this.codigoAuxiliar500_6 = codigoAuxiliar500_6;
//	}
//
//	public String getTextoAuxiliar500_6() {
//		return textoAuxiliar500_6;
//	}
//
//	public void setTextoAuxiliar500_6(String textoAuxiliar500_6) {
//		this.textoAuxiliar500_6 = textoAuxiliar500_6;
//	}
//
//	public String getCodigoAuxiliar500_7() {
//		return codigoAuxiliar500_7;
//	}
//
//	public void setCodigoAuxiliar500_7(String codigoAuxiliar500_7) {
//		this.codigoAuxiliar500_7 = codigoAuxiliar500_7;
//	}
//
//	public String getTextoAuxiliar500_7() {
//		return textoAuxiliar500_7;
//	}
//
//	public void setTextoAuxiliar500_7(String textoAuxiliar500_7) {
//		this.textoAuxiliar500_7 = textoAuxiliar500_7;
//	}
//
//	public String getCodigoAuxiliar500_8() {
//		return codigoAuxiliar500_8;
//	}
//
//	public void setCodigoAuxiliar500_8(String codigoAuxiliar500_8) {
//		this.codigoAuxiliar500_8 = codigoAuxiliar500_8;
//	}
//
//	public String getTextoAuxiliar500_8() {
//		return textoAuxiliar500_8;
//	}
//
//	public void setTextoAuxiliar500_8(String textoAuxiliar500_8) {
//		this.textoAuxiliar500_8 = textoAuxiliar500_8;
//	}
//
//	public String getCodigoAuxiliar500_9() {
//		return codigoAuxiliar500_9;
//	}
//
//	public void setCodigoAuxiliar500_9(String codigoAuxiliar500_9) {
//		this.codigoAuxiliar500_9 = codigoAuxiliar500_9;
//	}
//
//	public String getTextoAuxiliar500_9() {
//		return textoAuxiliar500_9;
//	}
//
//	public void setTextoAuxiliar500_9(String textoAuxiliar500_9) {
//		this.textoAuxiliar500_9 = textoAuxiliar500_9;
//	}
//
//	public String getCodigoAuxiliar500_10() {
//		return codigoAuxiliar500_10;
//	}
//
//	public void setCodigoAuxiliar500_10(String codigoAuxiliar500_10) {
//		this.codigoAuxiliar500_10 = codigoAuxiliar500_10;
//	}
//
//	public String getTextoAuxiliar500_10() {
//		return textoAuxiliar500_10;
//	}
//
//	public void setTextoAuxiliar500_10(String textoAuxiliar500_10) {
//		this.textoAuxiliar500_10 = textoAuxiliar500_10;
//	}
//
//	public String getCodigoAuxiliar250_1() {
//		return codigoAuxiliar250_1;
//	}
//
//	public void setCodigoAuxiliar250_1(String codigoAuxiliar250_1) {
//		this.codigoAuxiliar250_1 = codigoAuxiliar250_1;
//	}
//
//	public String getTextoAuxiliar250_1() {
//		return textoAuxiliar250_1;
//	}
//
//	public void setTextoAuxiliar250_1(String textoAuxiliar250_1) {
//		this.textoAuxiliar250_1 = textoAuxiliar250_1;
//	}
//
//	public String getCodigoAuxiliar250_2() {
//		return codigoAuxiliar250_2;
//	}
//
//	public void setCodigoAuxiliar250_2(String codigoAuxiliar250_2) {
//		this.codigoAuxiliar250_2 = codigoAuxiliar250_2;
//	}
//
//	public String getTextoAuxiliar250_2() {
//		return textoAuxiliar250_2;
//	}
//
//	public void setTextoAuxiliar250_2(String textoAuxiliar250_2) {
//		this.textoAuxiliar250_2 = textoAuxiliar250_2;
//	}
//
//	public String getCodigoAuxiliar250_3() {
//		return codigoAuxiliar250_3;
//	}
//
//	public void setCodigoAuxiliar250_3(String codigoAuxiliar250_3) {
//		this.codigoAuxiliar250_3 = codigoAuxiliar250_3;
//	}
//
//	public String getTextoAuxiliar250_3() {
//		return textoAuxiliar250_3;
//	}
//
//	public void setTextoAuxiliar250_3(String textoAuxiliar250_3) {
//		this.textoAuxiliar250_3 = textoAuxiliar250_3;
//	}
//
//	public String getCodigoAuxiliar250_4() {
//		return codigoAuxiliar250_4;
//	}
//
//	public void setCodigoAuxiliar250_4(String codigoAuxiliar250_4) {
//		this.codigoAuxiliar250_4 = codigoAuxiliar250_4;
//	}
//
//	public String getTextoAuxiliar250_4() {
//		return textoAuxiliar250_4;
//	}
//
//	public void setTextoAuxiliar250_4(String textoAuxiliar250_4) {
//		this.textoAuxiliar250_4 = textoAuxiliar250_4;
//	}
//
//	public String getCodigoAuxiliar250_5() {
//		return codigoAuxiliar250_5;
//	}
//
//	public void setCodigoAuxiliar250_5(String codigoAuxiliar250_5) {
//		this.codigoAuxiliar250_5 = codigoAuxiliar250_5;
//	}
//
//	public String getTextoAuxiliar250_5() {
//		return textoAuxiliar250_5;
//	}
//
//	public void setTextoAuxiliar250_5(String textoAuxiliar250_5) {
//		this.textoAuxiliar250_5 = textoAuxiliar250_5;
//	}
//
//	public String getCodigoAuxiliar250_6() {
//		return codigoAuxiliar250_6;
//	}
//
//	public void setCodigoAuxiliar250_6(String codigoAuxiliar250_6) {
//		this.codigoAuxiliar250_6 = codigoAuxiliar250_6;
//	}
//
//	public String getTextoAuxiliar250_6() {
//		return textoAuxiliar250_6;
//	}
//
//	public void setTextoAuxiliar250_6(String textoAuxiliar250_6) {
//		this.textoAuxiliar250_6 = textoAuxiliar250_6;
//	}
//
//	public String getCodigoAuxiliar250_7() {
//		return codigoAuxiliar250_7;
//	}
//
//	public void setCodigoAuxiliar250_7(String codigoAuxiliar250_7) {
//		this.codigoAuxiliar250_7 = codigoAuxiliar250_7;
//	}
//
//	public String getTextoAuxiliar250_7() {
//		return textoAuxiliar250_7;
//	}
//
//	public void setTextoAuxiliar250_7(String textoAuxiliar250_7) {
//		this.textoAuxiliar250_7 = textoAuxiliar250_7;
//	}
//
//	public String getCodigoAuxiliar250_8() {
//		return codigoAuxiliar250_8;
//	}
//
//	public void setCodigoAuxiliar250_8(String codigoAuxiliar250_8) {
//		this.codigoAuxiliar250_8 = codigoAuxiliar250_8;
//	}
//
//	public String getTextoAuxiliar250_8() {
//		return textoAuxiliar250_8;
//	}
//
//	public void setTextoAuxiliar250_8(String textoAuxiliar250_8) {
//		this.textoAuxiliar250_8 = textoAuxiliar250_8;
//	}
//
//	public String getCodigoAuxiliar250_9() {
//		return codigoAuxiliar250_9;
//	}
//
//	public void setCodigoAuxiliar250_9(String codigoAuxiliar250_9) {
//		this.codigoAuxiliar250_9 = codigoAuxiliar250_9;
//	}
//
//	public String getTextoAuxiliar250_9() {
//		return textoAuxiliar250_9;
//	}
//
//	public void setTextoAuxiliar250_9(String textoAuxiliar250_9) {
//		this.textoAuxiliar250_9 = textoAuxiliar250_9;
//	}
//
//	public String getCodigoAuxiliar250_10() {
//		return codigoAuxiliar250_10;
//	}
//
//	public void setCodigoAuxiliar250_10(String codigoAuxiliar250_10) {
//		this.codigoAuxiliar250_10 = codigoAuxiliar250_10;
//	}
//
//	public String getTextoAuxiliar250_10() {
//		return textoAuxiliar250_10;
//	}
//
//	public void setTextoAuxiliar250_10(String textoAuxiliar250_10) {
//		this.textoAuxiliar250_10 = textoAuxiliar250_10;
//	}	
//	
//	public String getAuxiliar100_1() {
//        if (StringUtils.isBlank(codigoAuxiliar100_1) || StringUtils.isBlank(textoAuxiliar100_1)) return null;
//        return codigoAuxiliar100_1 + "|" + textoAuxiliar100_1;
//    }
//	
//    public String getAuxiliar100_2() {
//        if (StringUtils.isBlank(codigoAuxiliar100_2) || StringUtils.isBlank(textoAuxiliar100_2)) return null;
//        return codigoAuxiliar100_2 + "|" + textoAuxiliar100_2;
//    }
//
//    public String getAuxiliar100_3() {
//        if (StringUtils.isBlank(codigoAuxiliar100_3) || StringUtils.isBlank(textoAuxiliar100_3)) return null;
//        return codigoAuxiliar100_3 + "|" + textoAuxiliar100_3;
//    }
//
//    public String getAuxiliar100_4() {
//        if (StringUtils.isBlank(codigoAuxiliar100_4) || StringUtils.isBlank(textoAuxiliar100_4)) return null;
//        return codigoAuxiliar100_4 + "|" + textoAuxiliar100_4;
//    }
//
//    public String getAuxiliar100_5() {
//        if (StringUtils.isBlank(codigoAuxiliar100_5) || StringUtils.isBlank(textoAuxiliar100_5)) return null;
//        return codigoAuxiliar100_5 + "|" + textoAuxiliar100_5;
//    }
//
//    public String getAuxiliar100_6() {
//        if (StringUtils.isBlank(codigoAuxiliar100_6) || StringUtils.isBlank(textoAuxiliar100_6)) return null;
//        return codigoAuxiliar100_6 + "|" + textoAuxiliar100_6;
//    }
//
//    public String getAuxiliar100_7() {
//        if (StringUtils.isBlank(codigoAuxiliar100_7) || StringUtils.isBlank(textoAuxiliar100_7)) return null;
//        return codigoAuxiliar100_7 + "|" + textoAuxiliar100_7;
//    }
//
//    public String getAuxiliar100_8() {
//        if (StringUtils.isBlank(codigoAuxiliar100_8) || StringUtils.isBlank(textoAuxiliar100_8)) return null;
//        return codigoAuxiliar100_8 + "|" + textoAuxiliar100_8;
//    }
//
//    public String getAuxiliar100_9() {
//        if (StringUtils.isBlank(codigoAuxiliar100_9) || StringUtils.isBlank(textoAuxiliar100_9)) return null;
//        return codigoAuxiliar100_9 + "|" + textoAuxiliar100_9;
//    }
//
//    public String getAuxiliar100_10() {
//        if (StringUtils.isBlank(codigoAuxiliar100_10) || StringUtils.isBlank(textoAuxiliar100_10)) return null;
//        return codigoAuxiliar100_10 + "|" + textoAuxiliar100_10;
//    }
//    
//    public String getAuxiliar40_1() {
//        if (StringUtils.isBlank(codigoAuxiliar40_1) || StringUtils.isBlank(textoAuxiliar40_1)) return null;
//        return codigoAuxiliar40_1 + "|" + textoAuxiliar40_1;
//    }
//
//    public String getAuxiliar40_2() {
//        if (StringUtils.isBlank(codigoAuxiliar40_2) || StringUtils.isBlank(textoAuxiliar40_2)) return null;
//        return codigoAuxiliar40_2 + "|" + textoAuxiliar40_2;
//    }
//
//    public String getAuxiliar40_3() {
//        if (StringUtils.isBlank(codigoAuxiliar40_3) || StringUtils.isBlank(textoAuxiliar40_3)) return null;
//        return codigoAuxiliar40_3 + "|" + textoAuxiliar40_3;
//    }
//
//    public String getAuxiliar40_4() {
//        if (StringUtils.isBlank(codigoAuxiliar40_4) || StringUtils.isBlank(textoAuxiliar40_4)) return null;
//        return codigoAuxiliar40_4 + "|" + textoAuxiliar40_4;
//    }
//
//    public String getAuxiliar40_5() {
//        if (StringUtils.isBlank(codigoAuxiliar40_5) || StringUtils.isBlank(textoAuxiliar40_5)) return null;
//        return codigoAuxiliar40_5 + "|" + textoAuxiliar40_5;
//    }
//
//    public String getAuxiliar40_6() {
//        if (StringUtils.isBlank(codigoAuxiliar40_6) || StringUtils.isBlank(textoAuxiliar40_6)) return null;
//        return codigoAuxiliar40_6 + "|" + textoAuxiliar40_6;
//    }
//
//    public String getAuxiliar40_7() {
//        if (StringUtils.isBlank(codigoAuxiliar40_7) || StringUtils.isBlank(textoAuxiliar40_7)) return null;
//        return codigoAuxiliar40_7 + "|" + textoAuxiliar40_7;
//    }
//
//    public String getAuxiliar40_8() {
//        if (StringUtils.isBlank(codigoAuxiliar40_8) || StringUtils.isBlank(textoAuxiliar40_8)) return null;
//        return codigoAuxiliar40_8 + "|" + textoAuxiliar40_8;
//    }
//
//    public String getAuxiliar40_9() {
//        if (StringUtils.isBlank(codigoAuxiliar40_9) || StringUtils.isBlank(textoAuxiliar40_9)) return null;
//        return codigoAuxiliar40_9 + "|" + textoAuxiliar40_9;
//    }
//
//    public String getAuxiliar40_10() {
//        if (StringUtils.isBlank(codigoAuxiliar40_10) || StringUtils.isBlank(textoAuxiliar40_10)) return null;
//        return codigoAuxiliar40_10 + "|" + textoAuxiliar40_10;
//    }
//    
//    public String getAuxiliar500_1() {
//        if (StringUtils.isBlank(codigoAuxiliar500_1) || StringUtils.isBlank(textoAuxiliar500_1)) return null;
//        return codigoAuxiliar500_1 + "|" + textoAuxiliar500_1;
//    }
//
//    public String getAuxiliar500_2() {
//        if (StringUtils.isBlank(codigoAuxiliar500_2) || StringUtils.isBlank(textoAuxiliar500_2)) return null;
//        return codigoAuxiliar500_2 + "|" + textoAuxiliar500_2;
//    }
//
//    public String getAuxiliar500_3() {
//        if (StringUtils.isBlank(codigoAuxiliar500_3) || StringUtils.isBlank(textoAuxiliar500_3)) return null;
//        return codigoAuxiliar500_3 + "|" + textoAuxiliar500_3;
//    }
//
//    public String getAuxiliar500_4() {
//        if (StringUtils.isBlank(codigoAuxiliar500_4) || StringUtils.isBlank(textoAuxiliar500_4)) return null;
//        return codigoAuxiliar500_4 + "|" + textoAuxiliar500_4;
//    }
//
//    public String getAuxiliar500_5() {
//        if (StringUtils.isBlank(codigoAuxiliar500_5) || StringUtils.isBlank(textoAuxiliar500_5)) return null;
//        return codigoAuxiliar500_5 + "|" + textoAuxiliar500_5;
//    }
//    
//    public String getAuxiliar500_6() {
//        if (StringUtils.isBlank(codigoAuxiliar500_6) || StringUtils.isBlank(textoAuxiliar500_6)) return null;
//        return codigoAuxiliar500_6 + "|" + textoAuxiliar500_6;
//    }
//
//    public String getAuxiliar500_7() {
//        if (StringUtils.isBlank(codigoAuxiliar500_7) || StringUtils.isBlank(textoAuxiliar500_7)) return null;
//        return codigoAuxiliar500_7 + "|" + textoAuxiliar500_7;
//    }
//
//    public String getAuxiliar500_8() {
//        if (StringUtils.isBlank(codigoAuxiliar500_8) || StringUtils.isBlank(textoAuxiliar500_8)) return null;
//        return codigoAuxiliar500_8 + "|" + textoAuxiliar500_8;
//    }
//
//    public String getAuxiliar500_9() {
//        if (StringUtils.isBlank(codigoAuxiliar500_9) || StringUtils.isBlank(textoAuxiliar500_9)) return null;
//        return codigoAuxiliar500_9 + "|" + textoAuxiliar500_9;
//    }
//
//    public String getAuxiliar500_10() {
//        if (StringUtils.isBlank(codigoAuxiliar500_10) || StringUtils.isBlank(textoAuxiliar500_10)) return null;
//        return codigoAuxiliar500_10 + "|" + textoAuxiliar500_10;
//    }
//    
//    public String getAuxiliar250_1() {
//        if (StringUtils.isBlank(codigoAuxiliar250_1) || StringUtils.isBlank(textoAuxiliar250_1)) return null;
//        return codigoAuxiliar250_1 + "|" + textoAuxiliar250_1;
//    }
//
//    public String getAuxiliar250_2() {
//        if (StringUtils.isBlank(codigoAuxiliar250_2) || StringUtils.isBlank(textoAuxiliar250_2)) return null;
//        return codigoAuxiliar250_2 + "|" + textoAuxiliar250_2;
//    }
//
//    public String getAuxiliar250_3() {
//        if (StringUtils.isBlank(codigoAuxiliar250_3) || StringUtils.isBlank(textoAuxiliar250_3)) return null;
//        return codigoAuxiliar250_3 + "|" + textoAuxiliar250_3;
//    }
//
//    public String getAuxiliar250_4() {
//        if (StringUtils.isBlank(codigoAuxiliar250_4) || StringUtils.isBlank(textoAuxiliar250_4)) return null;
//        return codigoAuxiliar250_4 + "|" + textoAuxiliar250_4;
//    }
//
//    public String getAuxiliar250_5() {
//        if (StringUtils.isBlank(codigoAuxiliar250_5) || StringUtils.isBlank(textoAuxiliar250_5)) return null;
//        return codigoAuxiliar250_5 + "|" + textoAuxiliar250_5;
//    }
//
//    public String getAuxiliar250_6() {
//        if (StringUtils.isBlank(codigoAuxiliar250_6) || StringUtils.isBlank(textoAuxiliar250_6)) return null;
//        return codigoAuxiliar250_6 + "|" + textoAuxiliar250_6;
//    }
//
//    public String getAuxiliar250_7() {
//        if (StringUtils.isBlank(codigoAuxiliar250_7) || StringUtils.isBlank(textoAuxiliar250_7)) return null;
//        return codigoAuxiliar250_7 + "|" + textoAuxiliar250_7;
//    }
//
//    public String getAuxiliar250_8() {
//        if (StringUtils.isBlank(codigoAuxiliar250_8) || StringUtils.isBlank(textoAuxiliar250_8)) return null;
//        return codigoAuxiliar250_8 + "|" + textoAuxiliar250_8;
//    }
//
//    public String getAuxiliar250_9() {
//        if (StringUtils.isBlank(codigoAuxiliar250_9) || StringUtils.isBlank(textoAuxiliar250_9)) return null;
//        return codigoAuxiliar250_9 + "|" + textoAuxiliar250_9;
//    }
//
//    public String getAuxiliar250_10() {
//        if (StringUtils.isBlank(codigoAuxiliar250_10) || StringUtils.isBlank(textoAuxiliar250_10)) return null;
//        return codigoAuxiliar250_10 + "|" + textoAuxiliar250_10;
//    }	

    public static void main (String args[]) {
	    DatatypeFactory dtf = null;
	    try {
	        dtf = DatatypeFactory.newInstance();
	    } catch (DatatypeConfigurationException e) {
	        e.printStackTrace();
	    }
	    Date date = new Date();
	    
	    GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(date);
	    
	    XMLGregorianCalendar xgc = dtf.newXMLGregorianCalendar();
	    xgc.setHour(gc.get(Calendar.HOUR_OF_DAY));
	    xgc.setMinute(gc.get(Calendar.MINUTE));
	    xgc.setSecond(gc.get(Calendar.SECOND));
	    System.out.println(xgc);
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

	public TypeOrderReference getTypeOrderReference() {
		return typeOrderReference;
	}

	public void setTypeOrderReference(TypeOrderReference typeOrderReference) {
		this.typeOrderReference = typeOrderReference;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
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
	@Override
	public String toString() {
		return "EGuiaDocumento [serieNumeroGuia=" + serieNumeroGuia + ", tipoDocumentoGuia=" + tipoDocumentoGuia
				+ ", rucRazonSocialRemitente=" + rucRazonSocialRemitente+ ", tipoDocumentoRemitente="+ tipoDocumentoRemitente			
				+ ", numeroDocumentoRemitente=" + numeroDocumentoRemitente+ "]";
	}
	
}
