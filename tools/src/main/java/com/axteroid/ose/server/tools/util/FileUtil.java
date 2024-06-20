package com.axteroid.ose.server.tools.util;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.axteroid.ose.server.tools.constantes.Constantes;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FileUtil {
	//private final static Logger log = Logger.getLogger(FileUtil.class);
	private final static Logger log = LoggerFactory.getLogger(FileUtil.class);

    public static void forceDelete(File file) {
        if (file == null) return;
        try {
            FileDeleteStrategy.FORCE.delete(file);
        } catch (IOException e) {
        	log.error("Error al eliminar el archivo " + file.getAbsolutePath(), e);
        }
    }

    public static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
            	log.error("Error al cerrar archivo ", e);
            }
        }
    }

    public static void close(InputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
            	log.error("Error al cerrar archivo ", e);
            }
        }
    }

    public static void writeToFile(File file, byte[] bytes) throws Exception {
        if (bytes == null) return;
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
    }

    public static File writeToFile(String sufix, byte[] bytes) throws Exception {    	
        File result = FileUtil.createTempFile(sufix);
        FileOutputStream fos = new FileOutputStream(result);
        fos.write(bytes);
        fos.close();
        return result;
    }

    public static File writeToFile(String prefix, String sufix, byte[] bytes) throws Exception {    	
    	String dirSendFile = Constantes.DIR_AXTEROID_OSE+Constantes.DIR_FILE+File.separator+prefix+sufix;
    	File oldfile = File.createTempFile(prefix, sufix);
		FileUtils.writeByteArrayToFile(oldfile, bytes);  				
		File newfile = new File(dirSendFile);
		if(newfile.exists())
			newfile.delete();
		if(oldfile.renameTo(newfile))
			log.info("Rename succesful");
		else
			log.info("Rename failed");
        return newfile;
    }
    
    public static File writeToFilefromBytes(String prefix, String sufix, byte[] bytes) throws Exception {    
    	try {
			String dirSendFile = (new StringBuilder(String.valueOf(Constantes.DIR_AXTEROID_OSE))).append(Constantes.DIR_FILE).append(File.separator).append(prefix).append(sufix).toString();                
			File oldfile = new File(dirSendFile);
			if(oldfile.exists())
				oldfile.delete();
			File newfile = FileUtil.fromBytes(oldfile, bytes) ;
	        return newfile;
    	}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("selectComprobante Exception \n"+errors);
    	}
    	return null;
    }
    
    public static File writeToFilefromBytesRobot(String prefix, String sufix, byte[] bytes) throws Exception {    
		//String dirSendFile = DirUtil.getRutaDirectory()+OseConstantes.DIR_ROBOT_FILE+File.separator+prefix+sufix;
		String dirSendFile = (new StringBuilder(String.valueOf(Constantes.DIR_AXTEROID_OSE)))
				.append(Constantes.DIR_AVATAR_FILE)
				.append(File.separator)
				.append(prefix)
				.append(sufix).toString();                
		//log.info("dirSendFileRobot "+dirSendFile);
		//System.out.println("dirSendFileRobot "+dirSendFile);
		File oldfile = new File(dirSendFile);
		if(oldfile.exists())
			oldfile.delete();
		File newfile = FileUtil.fromBytes(oldfile, bytes) ;
        return newfile;
    }
    
    public static File writeToFileOld(String prefix, String sufix, byte[] bytes) throws Exception {    	
    	File oldfile = File.createTempFile(prefix, sufix);
		FileUtils.writeByteArrayToFile(oldfile, bytes);  
		log.info("oldfile.getName() ... "+oldfile.getName());
        return oldfile;
    }
    
    public static File createTempFile(String suffix) throws IOException {
        File file = File.createTempFile("OseServer", suffix, new File(Constantes.DIR_TMP));
        return file;
    }

    public static String getFileName(String fileFullName) throws Exception {
        int indexStart1 = fileFullName.lastIndexOf("/");
        int indexStart2 = fileFullName.lastIndexOf("\\");
        int indexStart = Math.max(indexStart1, indexStart2);
        String fileName = fileFullName.substring(indexStart + 1);
        indexStart = fileName.lastIndexOf(".");
        fileName = fileName.substring(0, indexStart);
        return fileName;
    }

    public static String getFileExt(String fileFullName) {
        int indexStart1 = fileFullName.lastIndexOf("/");
        int indexStart2 = fileFullName.lastIndexOf("\\");

        int indexStart = Math.max(indexStart1, indexStart2);
        String fileName = fileFullName.substring(indexStart + 1);
        indexStart = fileName.lastIndexOf(".");
        if (indexStart == -1) return null;
        fileName = fileName.substring(indexStart, fileName.length());
        return fileName;
    }

    public static void mostrarArchivoTemporal(File archivo) {
        if (Desktop.isDesktopSupported())
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(archivo);
            } catch (IOException e) {
                log.error("Error en el sistema", e);
            }

    }

    public static byte[] toByte(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int c = 0;
            while ((c = fis.read()) != -1) {
                baos.write(c);
            }
            byte[] byteReturn = baos.toByteArray();
            return byteReturn;
        } finally {
            fis.close();
            baos.close();
        }
    }

    public static File fromBytes(File file, byte[] bytes) throws IOException {
        FileOutputStream fis = new FileOutputStream(file);
        try {
            fis.write(bytes);           
            return file;
        } finally {
            fis.close();
        }
    }
    
    public static File fromBytes2File(File file, byte[] bytes) throws IOException {
        try {
        	OutputStream os = new FileOutputStream(file); 
        	// Starts writing the bytes in it 
        	os.write(bytes); 
        	log.info("Successfully byte inserted"); 
        	os.close(); 
        	return file;
        } finally {
            
        }
    }

    public static void makeWritable(File dir) {
        if (dir.exists()) {
            File[] ficheros = dir.listFiles();
            for (File fichero : ficheros) {
                makeReadableOthers(fichero);
                makeWritableOthers(fichero);
            }
        }
    }

    private static void makeWritableOthers(File fichero) {
        try {
            fichero.setWritable(true, false);
        } catch (SecurityException e) {
        	log.warn("No se pudo asignar permiso WRITABLE a " + fichero, e);
        }
    }

    private static void makeReadableOthers(File fichero) {
        try {
            fichero.setReadable(true, false);
        } catch (SecurityException e) {
        	log.warn("No se pudo asignar permiso READABLE a " + fichero, e);
        }
    }

    public static boolean nameContains(String fullPath, String extract) {
        return unix(fullPath).contains(unix(extract));
    }

    public static String unix(String fileName) {
        return fileName.replace('\\', '/');
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static String buildDir(String idEmisor, String tipoDocumento, String idDocumento, String carpetaBase) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String carpetaFecha = StringUtils.replace(sdf.format(new Date()), "-", File.separator);
        String carpetaDocumento = tipoDocumento + "-" + idDocumento;
        String carpeta = carpetaBase + File.separator + idEmisor + File.separator + carpetaFecha + File.separator + carpetaDocumento;
        File dir = new File(carpeta);
        if (dir.exists()) {
            if (!FileUtil.deleteDir(dir)) {
                throw new IllegalArgumentException("Error al borrar la carpeta " + carpeta);
            }
        }
        if (!dir.mkdirs()) {
            throw new IllegalArgumentException("Error al crear la carpeta " + carpeta);
        }
        return carpeta;
    }

    public static File crearSignatureFile(String directorio, String fileName) {
        File result = null;
        try {
            result = new File(directorio, fileName);
            if (!result.createNewFile()) {
                throw new IllegalArgumentException("Error al crear el archivo " + result.getAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Error en el sistema", e);
            throw new IllegalArgumentException("Error al crear el archivo " + result.getAbsolutePath());
        }
        return result;
    }

    public static void outputDocToFile(Document doc, File file) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter out = new OutputStreamWriter(fos, Charset.forName("UTF-8"));

        TransformerFactory factory = TransformerFactory.newInstance("org.apache.xalan.processor.TransformerFactoryImpl", null);
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "" +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Value " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}StreetName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CityName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CitySubdivisionName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Description " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}RegistrationName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CityName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CountrySubentity " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}District " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Name");
        transformer.transform(new DOMSource(doc), new StreamResult(out));

        out.close();
        fos.close();
    }
    
    public static String outputDoc2File(Document doc, File file) throws Exception {
        ByteArrayOutputStream out=new ByteArrayOutputStream();       
        TransformerFactory factory = TransformerFactory.newInstance("org.apache.xalan.processor.TransformerFactoryImpl", null);
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
/*        transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "" +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Value " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}StreetName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CityName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CitySubdivisionName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Description " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}RegistrationName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CityName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CountrySubentity " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}District " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Name");*/
        transformer.transform(new DOMSource(doc), new StreamResult(out));        
        out.close();
        return out.toString("UTF-8");
    }    

    public static String outputDoc2String(Document doc) throws Exception {
        ByteArrayOutputStream out=new ByteArrayOutputStream();       
        TransformerFactory factory = TransformerFactory.newInstance("org.apache.xalan.processor.TransformerFactoryImpl", null);
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
/*        transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "" +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Value " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}StreetName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CityName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CitySubdivisionName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Description " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}RegistrationName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CityName " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}CountrySubentity " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}District " +
                "{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Name");*/
        transformer.transform(new DOMSource(doc), new StreamResult(out));        
        out.close();
        return out.toString("UTF-8");
    }  
    
    public static void changeTagName(Document doc) {
        NodeList nodes = doc.getElementsByTagName("ext:UBLExtension");

        Node currentNode = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            Node nodeId = node.getChildNodes().item(0);
            if ("EBIZ".equals(nodeId.getTextContent())) {
                currentNode = node;
            }
        }
        if (currentNode == null) return;
        Node extensionContent = currentNode.getLastChild();
        Node additionalInformation = extensionContent.getFirstChild();
        NodeList nodeList = ((Element) additionalInformation).getElementsByTagName("sac:AdditionalProperty");
        List<Node> nuevo = new ArrayList<Node>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            nuevo.add(nodeList.item(i));
        }
        Element cbcAdditionalInformation = doc.createElement("cbc:AdditionalInformation");
        for (Iterator<Node> iterator = nuevo.iterator(); iterator.hasNext(); ) {
            Node node = iterator.next();
            cbcAdditionalInformation.appendChild(node);
        }
        extensionContent.appendChild(cbcAdditionalInformation);
        extensionContent.removeChild(additionalInformation);
    }

    public static void notChangeTagName(Document doc) {
        NodeList nodes = doc.getElementsByTagName("ext:UBLExtension");
        Node currentNode = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            NodeList nodeList = node.getChildNodes();
            for (int j = 0; j < nodeList.getLength(); j++) {
                Node nodeId = nodeList.item(j);
                if (nodeId != null && "EBIZ".equals(nodeId.getTextContent())) {
                    currentNode = node;
                    break;
                }
            }
        }
        if (currentNode == null) return;
        Node extensionContent = currentNode.getLastChild();
        if (!"ExtensionContent".equals(extensionContent.getNodeName()) && !"ext:ExtensionContent".equals(extensionContent.getNodeName())) {
            extensionContent = extensionContent.getPreviousSibling();
        }

        Node additionalInformation = extensionContent.getFirstChild();
        if (!"AdditionalInformation".equals(additionalInformation.getNodeName())) {
            additionalInformation = extensionContent.getNextSibling();
        }

        NodeList nodeList = ((Element) currentNode).getElementsByTagName("sac:AdditionalProperty");
        List<Node> nuevo = new ArrayList<Node>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            nuevo.add(nodeList.item(i));
        }
        Element cbcAdditionalInformation = doc.createElement("sac:AdditionalInformation");
        for (Node node : nuevo) {
            cbcAdditionalInformation.appendChild(node);
        }
//        extensionContent.removeChild(additionalInformation);
        extensionContent.appendChild(cbcAdditionalInformation);
    }
        
    public File byteToFile(byte[] bytes, String name, String extension) throws IOException {
        File file = File.createTempFile(name, extension);
        file.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.flush();
        fos.close();
        return file;
    }
    
    public static void createDirectory(String dir) {
		//Path path = Paths.get(dir);
        //if (!Files.exists(path)) {
        if (!FileUtil.tobeDirectory(dir)) {
            try {
            	Path path = Paths.get(dir);
                Files.createDirectories(path);
            } catch (Exception e) {
            	log.error(e.toString());
            }
        }
    }
    
    public static boolean tobeDirectory(String dir) {
		Path path = Paths.get(dir);
        if (Files.exists(path)) 
        	return true;
        return false;
    }
    
    public static boolean tobeFile(String rutaFile) {
    	boolean b = false;
        File f = new File(rutaFile);
        if (f.exists()) 
        	b = true;
        return b;
    }    
}