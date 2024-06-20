package com.axteroid.ose.server.builder.content.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.builder.content.ContentValidate;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoDocumentoEnum;
import com.axteroid.ose.server.tools.constantes.TipoDocumentoSummaryEnum;


public class ContentValidateImpl implements ContentValidate{
	private static final Logger log = LoggerFactory.getLogger(ContentValidateImpl.class);
	
	private CatalogoErrores catalogoErrores = new CatalogoErrores();
	private ContentFileUnzip readContentFile = new ContentFileUnzip();
	
	public ContentValidateImpl(){}
	
	public void validarContentContenido(Documento documento){		
		//log.info("validarContentContenido - tbContent.getContentfile():"+tbContent.getContentfile());		
		if(documento.getNombre().isEmpty()){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}		
		byte[] content = (byte[]) documento.getUbl();
		if( content.length ==0){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0155);
			return;
		}
	}
	
	public void validarContentFileName(Documento documento){
		//log.info("validarContentFileName -> "+tbContent.getNombre());
		documento.setLongitudNombre(documento.getNombre().length());		
		if(documento.getLongitudNombre()==0){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}		
		String [] extName = documento.getNombre().split("\\.");
		if (extName.length !=2) {
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}		
		//documento.setExtension(extName[1].toUpperCase());
		String extension = extName[1].toUpperCase();
		String fileNameNew = extName[0]+"."+extension;
		documento.setNombre(fileNameNew);
		
		if(!extension.toUpperCase().equals(Constantes.OSE_ZIP)){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}
		
		String [] fileName = extName[0].split("-");	
		if (fileName.length !=4) {
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}
		documento.setTipoDocumento(fileName[1]);	
		validarTipoDocumento(documento);		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
		
		documento.setRucEmisor(Long.parseLong(fileName[0]));			
		documento.setSerie(fileName[2]);
		documento.setNumeroCorrelativo(fileName[3]);	
		long numcpe = Long.valueOf(fileName[3]);
		documento.setCorrelativo(numcpe);
	}
	
	private void validarTipoDocumento(Documento documento){
		String enumTipoDocumento = "TD"+documento.getTipoDocumento();		
		List<String> listTipoDocumento = Stream.of(TipoDocumentoEnum.values())
                .map(TipoDocumentoEnum::name).collect(Collectors.toList());		
		
		String sTipoDoc = listTipoDocumento.stream().filter(x ->  enumTipoDocumento.equals(x)).findAny().orElse("");
		
		if(!enumTipoDocumento.equals(sTipoDoc)){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}		
	}
	
	public void validarContentContentFile(Documento documento){
		//log.info("validarContentContentFile");				
		String dirZip = Constantes.DIR_AXTEROID_OSE+Constantes.DIR_FILE;
		readContentFile.unZipSave(documento, dirZip, catalogoErrores);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;	
		
		if(documento.getLongitudNombre()==0){			
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0155);
			return;
		}
		
		if(documento.getLongitudNombre()==1)
			validarNombreZipXml(documento);		
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;
	}
	
	private void validarNombreZipXml(Documento documento){
		//log.info("validarNombreZipXml");
//		int tamano = documento.getLongitudNombre();
//		if(!documento.getNombre().substring(0,tamano-4).equals(
//				documento.getDocumentList().get(0).getNombre().substring(0,tamano-4))){			
//			catalogoErrores.buscarError(documento, ConstantesOse.SUNAT_ERROR_0161);
//			return;
//		}		
	}

	public CatalogoErrores getCatalogoErrores() {
		return catalogoErrores;
	}

	public void setCatalogoErrores(CatalogoErrores catalogoErrores) {
		this.catalogoErrores = catalogoErrores;
	}	

//	public void validarContentFileNamePack(Documento documento){
//		//log.info("1) validarContentFileNamePack: "+tbContent.getNombre());
//		tbContent.setLongitudFilename(documento.getNombre().length());
//		
//		if(tbContent.getLongitudFilename()==0){
//			catalogoErrores.buscarError(tbContent, ConstantesOse.SUNAT_ERROR_0151);
//			return;
//		}
//		//log.info("2) validarContentFileNamePack: "+tbContent.getNombre());
//		tbContent.setExtension(tbContent.getNombre().substring(
//				tbContent.getLongitudFilename()-3, tbContent.getLongitudFilename()));
//		if(!tbContent.getExtension().toUpperCase().equals(ConstantesOse.OSE_ZIP)){
//			catalogoErrores.buscarError(tbContent, ConstantesOse.SUNAT_ERROR_0151);
//			return;
//		}
//		//log.info("3) validarContentFileNamePack: "+tbContent.getNombre());
//		tbContent.setTipoDocumento(tbContent.getNombre().substring(12, 14));	
//		this.validarTipoDocumentoPack(tbContent);
//		if(tbContent.getErrorComprobante().equals(ConstantesOse.CONTENT_FALSE.charAt(0)))
//			return;
//		//log.info("4) validarContentFileNamePack: "+tbContent.getNombre());
//		tbContent.setSerie(tbContent.getNombre().substring(15, 23));
//		//this.validarFecha(tbContent);
//		if(tbContent.getErrorComprobante().equals(ConstantesOse.CONTENT_FALSE.charAt(0)))
//			return;
//		//log.info("5) validarContentFileNamePack: "+tbContent.getNombre());
//		if(!tbContent.getNombre().substring(11, 12).equals("-")){
//			catalogoErrores.buscarError(tbContent, ConstantesOse.SUNAT_ERROR_0151);
//			return;
//		}
//		//log.info("6) validarContentFileNamePack: "+tbContent.getNombre());
//		if(!tbContent.getNombre().substring(14, 15).equals("-")){
//			catalogoErrores.buscarError(tbContent, ConstantesOse.SUNAT_ERROR_0151);
//			return;
//		}
//		//log.info("7) validarContentFileNamePack: "+tbContent.getNombre());
//		if(!tbContent.getNombre().substring(23, 24).equals("-")){
//			catalogoErrores.buscarError(tbContent, ConstantesOse.SUNAT_ERROR_0151);
//			return;
//		}						
//		//log.info("8) validarContentFileNamePack: "+tbContent.getNombre());
//		if(!tbContent.getNombre().substring(tbContent.getLongitudFilename()-4, 
//				tbContent.getLongitudFilename()-3).equals(".")){
//			catalogoErrores.buscarError(tbContent, ConstantesOse.SUNAT_ERROR_0151);
//			return;
//		}			
//		//log.info("9) validarContentFileNamePack: "+tbContent.getNombre());
//		tbContent.setRucEmisor(Long.parseLong(tbContent.getNombre().substring(0, 11)));			
//		tbContent.setNumeroCorrelativo(tbContent.getNombre().substring(24, 
//				tbContent.getLongitudFilename()-4));		
//		//log.info("10) validarContentFileNamePack: "+tbContent.getNombre());	
//	}
//	
//	private void validarTipoDocumentoPack(Documento documento){		
//		if(!tbContent.getTipoDocumento().equals(ConstantesOse.OSE_LT)){
//			//tbContent.setErrorContent(OseConstantes.CONTENT_FALSE.charAt(0));
//			catalogoErrores.buscarError(tbContent, ConstantesOse.SUNAT_ERROR_0151);
//			return;
//		}		
//	}
	
	private void validarFecha(Documento documento){			
		GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(new Date());
    	//log.info("1) validarFecha: "+tbContent.getSerie()+" | "+tbContent.getSerie().substring(0, 4)+" |"+gc.get(Calendar.YEAR));
    	if(!(Integer.parseInt(documento.getSerie().substring(0, 4)) == gc.get(Calendar.YEAR))){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}	
    	//log.info("2) validarFecha: "+tbContent.getSerie()+" | "+tbContent.getSerie().substring(4, 6)+" |"+gc.get(Calendar.MONTH));
    	if(!(Integer.parseInt(documento.getSerie().substring(4, 6)) == gc.get(Calendar.MONTH)+1)){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}
    	//log.info("3) validarFecha: "+tbContent.getSerie()+" | "+tbContent.getSerie().substring(6, 8)+" |"+gc.get(Calendar.DAY_OF_MONTH));
    	if(!(Integer.parseInt(documento.getSerie().substring(6, 8)) == gc.get(Calendar.DAY_OF_MONTH))){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}		
	}
	
	public void validarContentFileNameSummary(Documento documento){
		//log.info("validarContentFileNameSummary "+tbContent.getNombre());
		documento.setLongitudNombre(documento.getNombre().length());
		
		if(documento.getLongitudNombre()==0){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}
		
		String [] extName = documento.getNombre().split("\\.");
		if (extName.length !=2) {
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}
		
//		documento.setExtension(extName[1].toUpperCase());
//		String fileNameNew = extName[0]+"."+documento.getExtension();
//		documento.setNombre(fileNameNew);
//		
//		//tbContent.setExtension(tbContent.getNombre().substring(tbContent.getLongitudFilename()-3, tbContent.getLongitudFilename()));
//		if(!documento.getExtension().toUpperCase().equals(ConstantesOse.OSE_ZIP)){
//			catalogoErrores.buscarError(documento, ConstantesOse.SUNAT_ERROR_0151);
//			return;
//		}

		documento.setTipoDocumento(documento.getNombre().substring(12, 14));	
		validarTipoDocumentoSummary(documento);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;

		documento.setSerie(documento.getNombre().substring(15, 23));
		// HABILITAR EN PRODUCCION
		//validarFecha(tbContent);
		if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)))
			return;

		if(!documento.getNombre().substring(11, 12).equals("-")){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}

		if(!documento.getNombre().substring(14, 15).equals("-")){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}

		if(!documento.getNombre().substring(23, 24).equals("-")){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}						

		if(!documento.getNombre().substring(documento.getLongitudNombre()-4, 
				documento.getLongitudNombre()-3).equals(".")){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}			

		documento.setRucEmisor(Long.parseLong(documento.getNombre().substring(0, 11)));			
		documento.setNumeroCorrelativo(documento.getNombre().substring(24, 
				documento.getLongitudNombre()-4));	
		long numcpe = Long.valueOf(documento.getNombre().substring(24, 
				documento.getLongitudNombre()-4));
		documento.setCorrelativo(numcpe);		
	}
	
	private void validarTipoDocumentoSummary(Documento documento){
		String enumTipoDocumento = "TD"+documento.getTipoDocumento();		
		List<String> listTipoDocumento = Stream.of(TipoDocumentoSummaryEnum.values())
                .map(TipoDocumentoSummaryEnum::name).collect(Collectors.toList());		
		
		String sTipoDoc = listTipoDocumento.stream().filter(x ->  enumTipoDocumento.equals(x)).findAny().orElse("");
		
		if(!enumTipoDocumento.equals(sTipoDoc)){
			catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0151);
			return;
		}
		
	}
}
