package com.axteroid.ose.server.builder.content.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.UBLListParametroLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.UBLListParametroImpl;
import com.axteroid.ose.server.tools.bean.ErrorBean;
import com.axteroid.ose.server.tools.constantes.Constantes;

public class CatalogoErrores {
	private static final Logger log = LoggerFactory.getLogger(CatalogoErrores.class);
	private List<ErrorBean> listErrores = new ArrayList<>();	
	
	public void readXMLEventReader(InputStream isCatalogo) {
        try {
        	XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            //XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileCatalogo));
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(isCatalogo);
            while(xmlEventReader.hasNext()){
            	//log.info("1 xmlEventReader.hasNext() ");
            	ErrorBean errorBean = new ErrorBean();
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()){
                	StartElement startElement = xmlEvent.asStartElement();
                	//log.info("2 xmlEvent.isStartElement() - startElement - "+startElement.getName().getLocalPart());
                	if(startElement.getName().getLocalPart().equals("error")){                                       		
                		Attribute errorAttr = startElement.getAttributeByName(new QName("numero"));
                		if(errorAttr != null){
                			errorBean.setNumero(errorAttr.getValue().toString());
                    	   //log.info("3 xmlEvent.isStartElement() - error.getNumero() - "+errorBean.getNumero());
                		}
                	}else if(startElement.getName().getLocalPart().equals("")){
                		xmlEvent = xmlEventReader.nextEvent();
                		errorBean.setDescripcion(xmlEvent.asCharacters().getData());
                		//log.info("4 xmlEvent.isStartElement() - error.getDescripcion() - "+errorBean.getDescripcion());
                	}                	
                }
                //if Employee end element is reached, add employee object to list
                if(xmlEvent.isEndElement()){
                	EndElement endElement = xmlEvent.asEndElement();
                	//log.info("5 xmlEvent.isEndElement() - endElement-  "+endElement.getName().getLocalPart());
                	if(endElement.getName().getLocalPart().equals("error")){
                		listErrores.add(errorBean);
                	}
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
	}
	
	public void buscarError(Documento documento, String num_error){	
		//log.info("buscarError: "+num_error);			
		ErrorBean err;
		if(listErrores.size()>0)
			err = listErrores.stream().filter(x -> num_error.equals(x.getNumero()))       
        		.findAny().orElse(null);
		else {
			err = this.getErrorBean(num_error);			
			log.info("num_error: {} | sescripcion(): {} ",num_error,err.getDescripcion());	
		}
		if(err!=null){
			documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));	
			documento.setErrorNumero(err.getNumero());
			documento.setErrorDescripcion(err.getDescripcion());
			return;
		}
		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));	
		documento.setErrorNumero(num_error);
		documento.setErrorDescripcion(Constantes.SUNAT_ERROR_0000_D);	
	}

	public void buscarErrorZip(Documento documento, String num_error, int iDocument){	
		//log.info("CatalogoErrores -- buscarErrorZip(TbContent tbContent, String num_error, int iDocument)");
		ErrorBean err;
		if(listErrores.size()>0)
			err = listErrores.stream().filter(x -> num_error.equals(x.getNumero()))       
        		.findAny().orElse(null);
		else
			err = getErrorBean(num_error);	
		
//		if(err!=null){
//			tbContent.getTbComprobanteList().get(iDocument).setErrorComprobante(ConstantesOse.CONTENT_FALSE.charAt(0));	
//			tbContent.getTbComprobanteList().get(iDocument).setErrorNumero(err.getNumero());
//			tbContent.getTbComprobanteList().get(iDocument).setErrorDescripcion(err.getDescripcion());
//			return;
//		}
//		tbContent.getTbComprobanteList().get(iDocument).setErrorComprobante(ConstantesOse.CONTENT_FALSE.charAt(0));		
//		tbContent.getTbComprobanteList().get(iDocument).setErrorNumero(num_error);
//		tbContent.getTbComprobanteList().get(iDocument).setErrorDescripcion(ConstantesOse.SUNAT_ERROR_0000_D);	
	}
	
	public void buscarErrorComprobante(Documento documento, String num_error){	
		//log.info("buscarErrorComprobante: "+tbComprobante.getNombre()+" | "+num_error);
		ErrorBean err;
		if(listErrores.size()>0)
			err = listErrores.stream().filter(x -> num_error.equals(x.getNumero()))       
        	.findAny().orElse(null);
		else
			err = this.getErrorBean(num_error);	

		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));	
		documento.setEstado(Constantes.SUNAT_CDR_ERROR_PROCESO);
		if(err!=null){			
			documento.setErrorNumero(err.getNumero());
			documento.setErrorDescripcion(err.getDescripcion());		
			log.info("1) ErrorNumero()= "+documento.getErrorNumero()+" Descripcion() --> "+documento.getErrorDescripcion());	
			return;
		}
		documento.setErrorNumero(num_error);
		documento.setErrorDescripcion(Constantes.SUNAT_ERROR_0000_D);
	}

	public void buscarObservaciones(Documento documento){	
		//log.info("buscarObservaciones --> "+tbComprobante.getObservaNumero());	
		//this.getReduceObserva(documento);
		//log.info("buscarObservaciones --> "+tbComprobante.getObservaNumero());	
		String[] observa = documento.getObservaNumero().split(Constantes.OSE_SPLIT_3);
		String descripcion = "";
		for (String code : observa) {
			ErrorBean err;
			if(listErrores.size()>0)
				err = listErrores.stream().filter(x -> code.equals(x.getNumero()))       
        			.findAny().orElse(null);
			else
				err = getErrorBean(code);			
			if(err!=null){
				if(!descripcion.isEmpty())
					descripcion=descripcion+Constantes.OSE_SPLIT_3;
				descripcion=descripcion+err.getDescripcion();				
			}
		}
		if(descripcion.isEmpty())
			documento.setObservaDescripcion(Constantes.SUNAT_ERROR_0000_D);
		else
			documento.setObservaDescripcion(descripcion);					
	}
	
	public void buscarObservaciones2(Documento documento){	
		//log.info("buscarObservaciones "+tbComprobante.getObservaNumero());	
		ErrorBean err;
		if(listErrores.size()>0)
			err = listErrores.stream().filter(x -> documento.getObservaNumero().equals(x.getNumero()))       
        		.findAny().orElse(null);
		else
			err = this.getErrorBean(documento.getObservaNumero());	
		
		if(err!=null){
			documento.setObservaDescripcion(err.getDescripcion());
			return;
		}
		documento.setObservaDescripcion(Constantes.SUNAT_ERROR_0000_D);			
	}	
	
	public void probarListaError(Documento documento){		
		//log.info("probarListaError ");
		ErrorBean err = this.getErrorBean(Constantes.SUNAT_ERROR_0100_C);
		if(err!=null)
			return;		
		
		if (getListErrores()!=null){ 
			if(getListErrores().isEmpty() || getListErrores().size()<5){
				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
				documento.setErrorNumero(Constantes.SUNAT_ERROR_0138);
				documento.setErrorDescripcion(Constantes.SUNAT_ERROR_0138_D);
			}
			return;
		}	
		documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
		documento.setErrorNumero(Constantes.SUNAT_ERROR_0137);
		documento.setErrorDescripcion(Constantes.SUNAT_ERROR_0138_D);
	}
	
	public List<ErrorBean> getListErrores() {
		return listErrores;
	}

	public void setListErrores(List<ErrorBean> listErrores) {
		this.listErrores = listErrores;
	}
	
	private ErrorBean getErrorBean(String num_error) {
		ErrorBean errorBean = new ErrorBean();
		errorBean.setNumero(num_error);
		UBLListParametroLocal ublListParametroLocal = new UBLListParametroImpl();
		String value= ublListParametroLocal.bucarParametro(Constantes.SUNAT_PARAMETRO_CODIGO_RETORNO, num_error);
		log.info("num_error: {} | value: {}",num_error,value);	
		if(value!=null && !value.isEmpty()) {
			errorBean.setDescripcion(value);
			return errorBean;
		}
		return null;
	}
	
	private void getReduceObserva(Documento documento) {
		String[] observa = documento.getObservaNumero().split(Constantes.OSE_SPLIT_3);
		List<String> mapError = new ArrayList<String>();
		String sObserva = "";
		for (String code : observa) {
			//log.info("code: "+code);
			boolean b = false;
			for(String c : mapError) {
				//log.info("c: "+c);
				if(code.equals(c)) {
					b = true;
					break;
				}
			}	
			//log.info("code: "+code+" b: "+b);
			if(!b) {
				mapError.add(code);
				if(!sObserva.isEmpty())
					sObserva = sObserva+Constantes.OSE_SPLIT_3;
				sObserva = sObserva+code;
			}
		}				
		documento.setObservaNumero(sObserva);
	}
}
