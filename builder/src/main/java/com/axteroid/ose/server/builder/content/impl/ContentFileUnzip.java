package com.axteroid.ose.server.builder.content.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.constantes.Constantes;

public class ContentFileUnzip {
	private static final Logger log = LoggerFactory.getLogger(ContentFileUnzip.class);
    
    public void unZipSave(Documento documento, String output_dirZip, 
    		CatalogoErrores catalogoErrores){
    	log.info("unZipSave "+output_dirZip);
        byte[] buffer = new byte[Constantes.BUFFER_CONTENTFILE];
        try{
        	ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(documento.getUbl()));
        	ZipEntry ze = zis.getNextEntry();        	
        	int j=0;
        	List<Documento> listDocument = new ArrayList<Documento>();
        	//log.info("listTbComprobante.size() "+listTbComprobante.size());
        	while(ze!=null){
        		log.info("documento.getNombre: {} | ze.getName: {} ",documento.getNombre(), ze.getName());
//        		String[] sNameFile = documento.getNombre().split(".");
//        		String[] sNameContent = ze.getName().split(".");        		
        		String nameFile = documento.getNombre().substring(0,documento.getNombre().length()-4);
        		String nameContent = ze.getName().substring(0, ze.getName().length()-4);
        		log.info("nameFile: {} | nameContent: {} ",nameFile, nameContent);
        		if(!nameFile.equals(nameContent)) {
    				documento.setErrorComprobante(Constantes.CONTENT_FALSE.charAt(0));
    				documento.setErrorNumero(Constantes.SUNAT_ERROR_0161);         			        			
        		}       	
        		documento.setNombre(ze.getName());
        		String[] sArchivo = documento.getNombre().split("-");     
        		String dArchivo = sArchivo[3].substring(0, sArchivo[3].length()-4);
        		String idComprobante = sArchivo[0]+sArchivo[1]+sArchivo[2]+dArchivo;
        		documento.setIdComprobante(idComprobante);
        		documento.setDireccion(output_dirZip + File.separator +documento.getNombre());
        		documento.setTipoDocumento(sArchivo[1]);
        		documento.setSerie(sArchivo[2]);
        		documento.setNumeroCorrelativo(dArchivo);
        		long lArchivo = Long.parseLong(dArchivo);
        		documento.setCorrelativo(lArchivo);        		
        		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        		int len;
        		while ((len = zis.read(buffer, 0, Constantes.BUFFER_CONTENTFILE)) != -1) {
        			// Convuerte el ze en byteArray
					byteArrayOutputStream.write(buffer, 0, len);
				}
        		documento.setUbl(byteArrayOutputStream.toByteArray());
				//log.info("tbComprobante.getDireccion() "+tbComprobante.getDireccion());
				byteArrayOutputStream.close();				
        		ze = zis.getNextEntry();
        		listDocument.add(documento);        		
        		j++;
        	}
        	documento.setLongitudNombre(j);
        	//documento.setDocumentList(listDocument);
        	zis.closeEntry();
       		zis.close();       		       	
        }catch(Exception e){
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("unZipSave \n"+errors);
        	catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0156);
			return;
       	}      
    }
        
    public void unZipSave_Old(Documento documento, String output_dirZip, 
    		CatalogoErrores catalogoErrores){
    	log.info("ReadContentFile -- unZipSave_Old ");
        byte[] buffer = new byte[Constantes.BUFFER_CONTENTFILE];
        try{
        	File folder = new File(output_dirZip);
        	if(!folder.exists())
        		folder.mkdir();
        	String nameFile = output_dirZip + File.separator + documento.getNombre();
        	byte[] bFile = (byte[]) documento.getUbl();
        	Path path = Paths.get(nameFile);
            Files.write(path, bFile);
        	ZipInputStream zis = new ZipInputStream(new FileInputStream(nameFile));
        	ZipEntry ze = zis.getNextEntry();        	
        	int j=0;
        	List<Documento> listTbComprobante = new ArrayList<Documento>();
        	while(ze!=null){
        		//Documento tbComprobante = new Documento();
        		documento.setNombre(ze.getName());        		
        		String[] sArchivo = documento.getNombre().split("-");     
        		String dArchivo = sArchivo[3].substring(0, sArchivo[3].length()-4);
        		String idComprobante = sArchivo[0]+sArchivo[1]+sArchivo[2]+dArchivo;
        		documento.setIdComprobante(idComprobante);
        		documento.setDireccion(output_dirZip + File.separator +documento.getNombre());        		
        		File fileDE = new File(documento.getDireccion());
        		new File(fileDE.getParent()).mkdirs();        		
        		FileOutputStream fos = new FileOutputStream(fileDE);
        		int len;
        		while ((len = zis.read(buffer)) > 0) 
        			fos.write(buffer, 0, len);        		
        		fos.close();
        		ze = zis.getNextEntry();
        		listTbComprobante.add(documento);        		
        		j++;
        	}
        	//tbContent.setTotalComprobante(j);
        	//tbContent.setTbComprobanteList(listTbComprobante);
        	zis.closeEntry();
       		zis.close();       		       	
        }catch(IOException ex){
        	StringWriter errors = new StringWriter();				
			ex.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("IOException \n"+errors);
        	catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0156);
			return;
       	}      
    }  
    
    public void unZipSavePack_Old(Documento documento, String output_dirZip, 
    		CatalogoErrores catalogoErrores){
    	log.info("ReadContentFile -- unZipSavePack_Old ");
        byte[] buffer = new byte[Constantes.BUFFER_CONTENTFILE];
        try{
        	File folder = new File(output_dirZip);
        	if(!folder.exists())
        		folder.mkdir();
        	String nameFile = output_dirZip + File.separator + documento.getNombre();
        	byte[] bFile = (byte[]) documento.getUbl();
        	Path path = Paths.get(nameFile);
            Files.write(path, bFile);
        	ZipInputStream zis = new ZipInputStream(new FileInputStream(nameFile));
        	ZipEntry ze = zis.getNextEntry();        	
        	int j=0;
        	List<Documento> listTbComprobante = new ArrayList<Documento>();
        	while(ze!=null){
        		//Documento tbComprobante = new Documento();
        		documento.setNombre(ze.getName());
        		String[] sArchivo = documento.getNombre().split("-");     
        		String dArchivo = sArchivo[3].substring(0, sArchivo[3].length()-4);
        		String idComprobante = sArchivo[0]+sArchivo[1]+sArchivo[2]+dArchivo;
        		documento.setIdComprobante(idComprobante);
        		documento.setDireccion(output_dirZip + File.separator +documento.getNombre());        		
        		File fileDE = new File(documento.getDireccion());
        		new File(fileDE.getParent()).mkdirs();        		
        		FileOutputStream fos = new FileOutputStream(fileDE);
        		int len;
        		while ((len = zis.read(buffer)) > 0) 
        			fos.write(buffer, 0, len);        		
        		fos.close();
        		ze = zis.getNextEntry();
        		listTbComprobante.add(documento);        		
        		j++;
        	}
        	//tbContent.setTotalComprobante(j);
        	//tbContent.setTbComprobanteList(listTbComprobante);
        	zis.closeEntry();
       		zis.close();       		       	
        }catch(IOException ex){
        	StringWriter errors = new StringWriter();				
			ex.printStackTrace(new PrintWriter(errors));
			documento.setErrorLog(errors.toString());
			log.error("IOException \n"+errors);
        	catalogoErrores.buscarError(documento, Constantes.SUNAT_ERROR_0156);
			return;
       	}      
    }
}
