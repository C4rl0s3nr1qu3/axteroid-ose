package com.axteroid.ose.server.tools.algoritmo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.exception.DetailException;

public class Json2Object {
	private static final Logger log = LoggerFactory.getLogger(Json2Object.class);	

//	public static Parametro convertJson2Parametros(String stringJsonParametro) {
//		Parametro parametro = new Parametro();
//		JsonParser parser = new JsonParser();
//		JsonElement elementObject = parser.parse(stringJsonParametro);
//		parametro.setId_para(elementObject.getAsJsonObject().get("ID_PARA").getAsString());
//		parametro.setCod_para(elementObject.getAsJsonObject().get("COD_PARA").getAsString());
//		parametro.setNom_para(elementObject.getAsJsonObject().get("NOM_PARA").getAsString());
//		parametro.setTip_para(elementObject.getAsJsonObject().get("TIP_PARA").getAsString());
//		if(elementObject.getAsJsonObject().get("VAL_PARA").getAsString()!=null)
//			parametro.setVal_para(elementObject.getAsJsonObject().get("VAL_PARA").getAsString());
//		parametro.setInd_esta_para(elementObject.getAsJsonObject().get("IND_ESTA_PARA").getAsString());
//		parametro.setUsrCrea(elementObject.getAsJsonObject().get("USRCREA").getAsString());
//		parametro.setFecCrea(
//					LogDateUtil.stringToDate(elementObject.getAsJsonObject().get("FECCREA").getAsString(),
//							LogDateUtil.FORMATO_FECHA_YYYY_MM_DD_TIME));
//		parametro.setUsrModi(elementObject.getAsJsonObject().get("USRMODI").getAsString());
//		parametro.setFecModi(
//					LogDateUtil.stringToDate(elementObject.getAsJsonObject().get("FECMODI").getAsString(),
//							LogDateUtil.FORMATO_FECHA_YYYY_MM_DD_TIME));	
//		return parametro;	
//	}	
	
	public static String convertXM2Json_1(String stringUbl, String fileName){
		String archivoUbl = "/opt/axteroid-ose/file/ubl/"+fileName;
		try {
			Files.write(Paths.get(archivoUbl), stringUbl.getBytes());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("convertXM2Json Exception \n"+errors);
		}
		String archivoDepuraXmlImpresion = "/opt/axteroid-ose/template/jasper/Depura_Xml_Impresion.xsl";
		int indexPoint = fileName.lastIndexOf(".");
		String nameZip = fileName.substring(0, indexPoint); 
		String archivoUblPdf = "/opt/axteroid-ose/file/ublpdf/"+nameZip+"-Depurado"+Constantes.OSE_FILE_XML;
		String ublTrans = transformUbl2UblPdf(archivoUbl, archivoDepuraXmlImpresion, archivoUblPdf);
		//log.info("ublTrans: {}",ublTrans);
		JSONObject jsonObject = XML.toJSONObject(ublTrans);
		//JSONObject jsonObject = XML.toJSONObject(archivoUbl);
		String strJson = jsonObject.toString(4);	
		File fileArchivoUbl = new File(archivoUbl);
		if(fileArchivoUbl.isFile())
			fileArchivoUbl.delete();
		File fileArchivoUblPdf = new File(archivoUblPdf);
		if(fileArchivoUblPdf.isFile())
			fileArchivoUblPdf.delete();					
		log.info("strJson: {}",strJson);
		return strJson;
	}	
	
	public static String transformUbl2UblPdf(String archivoUbl, String archivoDepuraXmlImpresion, String archivoUblPdf) {
		//log.info("archivoUbl: {} | archivoDepuraXmlImpresion: {} | archivoUblPdf: {}",archivoUbl, archivoDepuraXmlImpresion, archivoUblPdf);
		String retorno = "";		
		try {
			File fileDepuraXmlImpresion = new File(archivoDepuraXmlImpresion);
			if (!fileDepuraXmlImpresion.exists()) {
				DetailException exceptionDetail = new DetailException();
				String message = "No existe la plantilla para el tipo documento a validar XML (Archivo XSL).";
				exceptionDetail.setMessage(message);
				return null;
			}
			StreamSource xlsStreamSource = new StreamSource(archivoDepuraXmlImpresion);
			StreamSource xmlStreamSource = new StreamSource(archivoUbl);									  
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(archivoUblPdf);			
				Transformer transformer = transformerFactory.newTransformer(xlsStreamSource);
				transformer.setOutputProperty("encoding", "ISO-8859-1");			
				transformer.transform(xmlStreamSource, new StreamResult(fos));
				fos.close();
			} catch (Exception e) {
				StringWriter errors = new StringWriter();				
				e.printStackTrace(new PrintWriter(errors));
				log.error("1) transformUbl2UblPdf Exception \n"+errors);
				fos.close();
			}
			
			StreamSource streamSourceDepuraXmlImpresion = new StreamSource(archivoDepuraXmlImpresion);
			StreamSource streamSourceArchivoUbl = new StreamSource(archivoUbl);						
			//StreamResult streamResult = new StreamResult(new File(archivoUblPdf));
			//transformer.transform(xmlStreamSource, streamResult);

			StringWriter writer = new StringWriter();
		    StreamResult result = new StreamResult(writer);
		    TransformerFactory tFactory = TransformerFactory.newInstance();
		    Transformer tformer = tFactory.newTransformer(streamSourceDepuraXmlImpresion);
		    tformer.transform(streamSourceArchivoUbl, result);
		    retorno = writer.toString();
												
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("2) transformUbl2UblPdf Exception \n"+errors);
	  	}    
	  	return retorno;
  	}
}
