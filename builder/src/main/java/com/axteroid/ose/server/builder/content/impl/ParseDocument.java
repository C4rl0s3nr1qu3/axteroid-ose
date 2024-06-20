package com.axteroid.ose.server.builder.content.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.bean.Homologa;
import com.axteroid.ose.server.tools.bean.HomologaBean;
import com.axteroid.ose.server.tools.constantes.Constantes;

public class ParseDocument {
	private static final Logger log = LoggerFactory.getLogger(ParseDocument.class);
	
	public static void parseUBL(Documento documento) {

		//// 2325
		//// 3242
		//// 2644
		//// 4019
		String nombre = documento.getNombre().toUpperCase().replace(".XML", "");
		List<Homologa> listHomologa = HomologaBean.getHomologa();		
		for(Homologa h : listHomologa) {
			//log.info("nombre: {} | homologa: {}",nombre,h.getComprobante());
			if(nombre.equals(h.getComprobante())) {
				if(h.getEstado_Esperado().equals("0 - Aceptado")) {
					documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
					documento.setErrorNumero(null);
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(null);	
					documento.setObservaDescripcion(null);
				}
				if(h.getEstado_Esperado().equals("0 - Aceptado con Observaciones")) {
					documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
					documento.setErrorNumero(null);
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(h.getCodigo_Esperado());	
					documento.setObservaDescripcion(null);
				}
				if(h.getEstado_Esperado().equals("99 - Rechazado")) {
					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
					documento.setErrorNumero(h.getCodigo_Esperado());
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(null);	
					documento.setObservaDescripcion(null);
				}
				//log.info("resultado {} ",h.toString());
				return;
			}
		}
		
	}
	
	public static void parseUBLResumen(Documento documento) {
		String nombre_2 = documento.getNombre().toUpperCase().replace(".XML", "");
		String[] nombre_1 = nombre_2.split("-");
		String nombre = nombre_1[0]+"-"+nombre_1[1]+"-"+nombre_1[3];
		List<Homologa> listHomologa = HomologaBean.getHomologaResumen();		
		for(Homologa h : listHomologa) {			
			String[] comprobate_1 = h.getComprobante().split("-");
			String comprobate = comprobate_1[0]+"-"+comprobate_1[1]+"-"+comprobate_1[3];
			//log.info("nombre: {} | nombre_3: {} || homologa: {} | homologa_3: {}",nombre_2,nombre,h.getComprobante(),comprobate);
			if(nombre.equals(comprobate)) {
				if(h.getEstado_Esperado().equals("0 - Aceptado")) {
					documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
					documento.setErrorNumero(null);
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(null);	
					documento.setObservaDescripcion(null);
				}
				if(h.getEstado_Esperado().equals("0 - Aceptado con Observaciones")) {
					documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
					documento.setErrorNumero(null);
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(h.getCodigo_Esperado());	
					documento.setObservaDescripcion(null);
				}
				if(h.getEstado_Esperado().equals("99 - Rechazado")) {
					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
					documento.setErrorNumero(h.getCodigo_Esperado());
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(null);	
					documento.setObservaDescripcion(null);
				}
				//log.info("resultado {} ",h.toString());
				return;
			}
		}
		
	}
	public static void parseUBLDatos(Documento documento) {

		//// 2325
		//// 3242
		//// 2644
		//// 4019
		String nombre = documento.getNombre().toUpperCase().replace(".XML", "");
		List<Homologa> listHomologa = HomologaBean.getHomologaDatos();		
		for(Homologa h : listHomologa) {
			//log.info("nombre: {} | homologa: {}",nombre,h.getComprobante());
			if(nombre.equals(h.getComprobante())) {
				if(h.getEstado_Esperado().equals("0 - Aceptado")) {
					documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
					documento.setErrorNumero(null);
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(null);	
					documento.setObservaDescripcion(null);
				}
				if(h.getEstado_Esperado().equals("0 - Aceptado con Observaciones")) {
					documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
					documento.setErrorNumero(null);
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(h.getCodigo_Esperado());	
					documento.setObservaDescripcion(null);
				}
				if(h.getEstado_Esperado().equals("99 - Rechazado")) {
					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
					documento.setErrorNumero(h.getCodigo_Esperado());
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(null);	
					documento.setObservaDescripcion(null);
				}
				//log.info("resultado {} ",h.toString());
				return;
			}
		}
		
	}
	
	public static void parseUBLResumenDatos(Documento documento) {
		String nombre_2 = documento.getNombre().toUpperCase().replace(".XML", "");
		String[] nombre_1 = nombre_2.split("-");
		String nombre = nombre_1[0]+"-"+nombre_1[1]+"-"+nombre_1[3];
		List<Homologa> listHomologa = HomologaBean.getHomologaResumenDatos();		
		for(Homologa h : listHomologa) {			
			String[] comprobate_1 = h.getComprobante().split("-");
			String comprobate = comprobate_1[0]+"-"+comprobate_1[1]+"-"+comprobate_1[3];
			//log.info("nombre: {} | nombre_3: {} || homologa: {} | homologa_3: {}",nombre_2,nombre,h.getComprobante(),comprobate);
			if(nombre.equals(comprobate)) {
				if(h.getEstado_Esperado().equals("0 - Aceptado")) {
					documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
					documento.setErrorNumero(null);
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(null);	
					documento.setObservaDescripcion(null);
				}
				if(h.getEstado_Esperado().equals("0 - Aceptado con Observaciones")) {
					documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));
					documento.setErrorNumero(null);
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(h.getCodigo_Esperado());	
					documento.setObservaDescripcion(null);
				}
				if(h.getEstado_Esperado().equals("99 - Rechazado")) {
					documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
					documento.setErrorNumero(h.getCodigo_Esperado());
					documento.setErrorDescripcion(null);
					documento.setObservaNumero(null);	
					documento.setObservaDescripcion(null);
				}
				//log.info("resultado {} ",h.toString());
				return;
			}
		}
		
	}
}
