package com.axteroid.ose.server.avatar.task;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.bean.TmDetalleResumenes;
import com.axteroid.ose.server.avatar.dao.SunatDetalleResumenesDao;
import com.axteroid.ose.server.avatar.dao.impl.SunatDetalleResumenesDaoImpl;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.edocu.EDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumentoItem;

public class DetalleResumenesCrud {
	private static final Logger log = LoggerFactory.getLogger(DetalleResumenesCrud.class);
	
	public void cargaDetalleDocumento(Documento tbComprobante, EDocumento eDocumento) {
		
	}
	public void cargaDetalleResumenes_RA_RC(Documento tbComprobante, EResumenDocumento eDocumento) {		
		log.info("cargaDetalleResumenes_RA_RC Archivo: " + tbComprobante.getNombre());
		
		for(EResumenDocumentoItem rdi : eDocumento.getItems()){	
			try {
				TmDetalleResumenes tmDetalleResumenes = new TmDetalleResumenes();
				tmDetalleResumenes.setNumRuc(tbComprobante.getRucEmisor());												
				if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
					log.info("rdi.getDocumentTypeCode(): "+rdi.getDocumentTypeCode()+" rdi.getId(): "+rdi.getId());
					tmDetalleResumenes.setCodCpe(rdi.getDocumentTypeCode());					
					if(rdi.getId()!=null) {
						String [] docRefPri = rdi.getId().split("-");
						tmDetalleResumenes.setNumSerieCpe(docRefPri[0]);
						tmDetalleResumenes.setNumCpe(String.valueOf(Integer.parseInt(docRefPri[1])));
					}else {
						tmDetalleResumenes.setNumSerieCpe(tbComprobante.getSerie());
						tmDetalleResumenes.setNumCpe(rdi.getLineID());
					}					
					if(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Adicionar)
						tmDetalleResumenes.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Acept)));
					if(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Anulado)
						tmDetalleResumenes.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));
					if(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Anulado_Dia)
						tmDetalleResumenes.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));	
				}
				if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) {
					log.info("rdi.getTipoDocumento(): "+rdi.getTipoDocumento()+" rdi.getSerieDocumentoBaja(): "+rdi.getSerieDocumentoBaja()+" rdi.getNumeroDocumentoBaja(): "+rdi.getNumeroDocumentoBaja());
					tmDetalleResumenes.setCodCpe(rdi.getTipoDocumento());
					tmDetalleResumenes.setNumSerieCpe(rdi.getSerieDocumentoBaja());
					tmDetalleResumenes.setNumCpe(rdi.getNumeroDocumentoBaja());
					tmDetalleResumenes.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));
				}
				tmDetalleResumenes.setFecEmisionCpe(eDocumento.getFechaEmisionComprobante());				
				tmDetalleResumenes.setMtoImporteCpe(rdi.getTotalVenta());
				tmDetalleResumenes.setCodMonedaCpe("");
				if(rdi.getTipoMoneda()!=null)					
					tmDetalleResumenes.setCodMonedaCpe(rdi.getTipoMoneda());
				tmDetalleResumenes.setUserCrea(tbComprobante.getUserCrea());
				tmDetalleResumenes.setFechaCrea(tbComprobante.getFechaCrea());
				tmDetalleResumenes.setIdComprobanteRc(String.valueOf(tbComprobante.getId()));
				tmDetalleResumenes.setErrorComprobante(tbComprobante.getErrorComprobante());
				SunatDetalleResumenesDao tmDetalleResumenesDao = new SunatDetalleResumenesDaoImpl();
				tmDetalleResumenesDao.insertDetalleResumenes(tmDetalleResumenes);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();				
				e.printStackTrace(new PrintWriter(errors));
				log.info("cargaDetalleResumenes_RA_RC Exception \n"+errors);
			}
		}
	}	
	
	public void cargaDetalleResumenes_RR(Documento tbComprobante, EReversionDocumento eDocumento) {		
		log.info("cargaDetalleResumenes_RR Archivo: " + tbComprobante.getNombre());
		try {
			for(EReversionDocumentoItem rdi : eDocumento.getItems()){	
				TmDetalleResumenes tmDetalleResumenes = new TmDetalleResumenes();
				tmDetalleResumenes.setNumRuc(tbComprobante.getRucEmisor());				
				tmDetalleResumenes.setCodCpe(rdi.getTipoDocumentoRevertido());
				//log.info("rdi.getTipoDocumentoRevertido(): "+rdi.getTipoDocumentoRevertido()+" rdi.getSerieNumeroDocRevertido(): "+rdi.getSerieNumeroDocRevertido());
				String [] docRefPri = rdi.getSerieNumeroDocRevertido().split("-");
				tmDetalleResumenes.setNumSerieCpe(docRefPri[0]);
				tmDetalleResumenes.setNumCpe(String.valueOf(Integer.parseInt(docRefPri[1])));
				tmDetalleResumenes.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));									
				tmDetalleResumenes.setFecEmisionCpe(eDocumento.getFechaDocumentoRevertido());				
				tmDetalleResumenes.setMtoImporteCpe(new BigDecimal(0));
				tmDetalleResumenes.setCodMonedaCpe("");
				tmDetalleResumenes.setUserCrea(tbComprobante.getUserCrea());
				tmDetalleResumenes.setFechaCrea(tbComprobante.getFechaCrea());
				tmDetalleResumenes.setIdComprobanteRc(String.valueOf(tbComprobante.getId()));
				tmDetalleResumenes.setErrorComprobante(tbComprobante.getErrorComprobante());	
				SunatDetalleResumenesDao tmDetalleResumenesDao = new SunatDetalleResumenesDaoImpl();
				tmDetalleResumenesDao.insertDetalleResumenes(tmDetalleResumenes);
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaDetalleResumenes_RR Exception \n"+errors);
		}
	}	
	
	public void cargaListaDetalleResumenes_RA_RC(Documento tbComprobante, EResumenDocumento eDocumento) {		
		log.info("cargaListaDetalleResumenes_RA_RC Archivo: " + tbComprobante.getNombre());
		try {
			List<TmDetalleResumenes> listTmDetalleResumenes = new ArrayList<TmDetalleResumenes>();
			for(EResumenDocumentoItem rdi : eDocumento.getItems()){	
				TmDetalleResumenes tmDetalleResumenes = new TmDetalleResumenes();
				tmDetalleResumenes.setNumRuc(tbComprobante.getRucEmisor());												
				if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO)) {
					log.info("rdi.getDocumentTypeCode(): "+rdi.getDocumentTypeCode()+" rdi.getId(): "+rdi.getId());
					tmDetalleResumenes.setCodCpe(rdi.getDocumentTypeCode());					
					if(rdi.getId()!=null) {
						String [] docRefPri = rdi.getId().split("-");
						tmDetalleResumenes.setNumSerieCpe(docRefPri[0]);
						tmDetalleResumenes.setNumCpe(String.valueOf(Integer.parseInt(docRefPri[1])));
					}else {
						tmDetalleResumenes.setNumSerieCpe(tbComprobante.getSerie());
						tmDetalleResumenes.setNumCpe(rdi.getLineID());
					}					
					if(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Adicionar)
						tmDetalleResumenes.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Acept)));
					if(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Anulado)
						tmDetalleResumenes.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));
					if(rdi.getStatus()==Constantes.SUNAT_CodigoOperacion_Anulado_Dia)
						tmDetalleResumenes.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));	
				}
				if(tbComprobante.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS)) {
					log.info("rdi.getTipoDocumento(): "+rdi.getTipoDocumento()+" rdi.getSerieDocumentoBaja(): "+rdi.getSerieDocumentoBaja()+" rdi.getNumeroDocumentoBaja(): "+rdi.getNumeroDocumentoBaja());
					tmDetalleResumenes.setCodCpe(rdi.getTipoDocumento());
					tmDetalleResumenes.setNumSerieCpe(rdi.getSerieDocumentoBaja());
					tmDetalleResumenes.setNumCpe(rdi.getNumeroDocumentoBaja());
					tmDetalleResumenes.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));
				}
				tmDetalleResumenes.setFecEmisionCpe(eDocumento.getFechaEmisionComprobante());				
				tmDetalleResumenes.setMtoImporteCpe(rdi.getTotalVenta());
				tmDetalleResumenes.setCodMonedaCpe("");
				if(rdi.getTipoMoneda()!=null)					
					tmDetalleResumenes.setCodMonedaCpe(rdi.getTipoMoneda());
				tmDetalleResumenes.setUserCrea(tbComprobante.getUserCrea());
				tmDetalleResumenes.setFechaCrea(tbComprobante.getFechaCrea());
				tmDetalleResumenes.setIdComprobanteRc(String.valueOf(tbComprobante.getId()));
				tmDetalleResumenes.setErrorComprobante(tbComprobante.getErrorComprobante());
				listTmDetalleResumenes.add(tmDetalleResumenes);
			}
			SunatDetalleResumenesDao tmDetalleResumenesDao = new SunatDetalleResumenesDaoImpl();
			tmDetalleResumenesDao.insertListaDetalleResumenes(listTmDetalleResumenes);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaDetalleResumenes_RA_RC Exception \n"+errors);
		}
	}	
	
	public void cargaListaDetalleResumenes_RR(Documento tbComprobante, EReversionDocumento eDocumento) {		
		log.info("cargaDetalleResumenes_RA_RC Archivo: " + tbComprobante.getNombre());
		try {
			List<TmDetalleResumenes> listTmDetalleResumenes = new ArrayList<TmDetalleResumenes>();
			for(EReversionDocumentoItem rdi : eDocumento.getItems()){	
				TmDetalleResumenes tmDetalleResumenes = new TmDetalleResumenes();
				tmDetalleResumenes.setNumRuc(tbComprobante.getRucEmisor());				
				tmDetalleResumenes.setCodCpe(rdi.getTipoDocumentoRevertido());
				log.info("rdi.getTipoDocumentoRevertido(): "+rdi.getTipoDocumentoRevertido()+" rdi.getSerieNumeroDocRevertido(): "+rdi.getSerieNumeroDocRevertido());
				String [] docRefPri = rdi.getSerieNumeroDocRevertido().split("-");
				tmDetalleResumenes.setNumSerieCpe(docRefPri[0]);
				tmDetalleResumenes.setNumCpe(String.valueOf(Integer.parseInt(docRefPri[1])));
				tmDetalleResumenes.setIndEstadoCpe(Short.parseShort(String.valueOf(Constantes.SUNAT_IndEstCpe_Anula)));									
				tmDetalleResumenes.setFecEmisionCpe(eDocumento.getFechaDocumentoRevertido());				
				tmDetalleResumenes.setMtoImporteCpe(new BigDecimal(0));
				tmDetalleResumenes.setCodMonedaCpe("");
				tmDetalleResumenes.setUserCrea(tbComprobante.getUserCrea());
				tmDetalleResumenes.setFechaCrea(tbComprobante.getFechaCrea());
				tmDetalleResumenes.setIdComprobanteRc(String.valueOf(tbComprobante.getId()));
				tmDetalleResumenes.setErrorComprobante(tbComprobante.getErrorComprobante());	
				listTmDetalleResumenes.add(tmDetalleResumenes);
			}
			SunatDetalleResumenesDao tmDetalleResumenesDao = new SunatDetalleResumenesDaoImpl();
			tmDetalleResumenesDao.insertListaDetalleResumenes(listTmDetalleResumenes);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("cargaListaCPE Exception \n"+errors);
		}
	}	
}
