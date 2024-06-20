package com.axteroid.ose.server.tools.util;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;

public class ZipUtil {
    private static int BUFFER = 1014;
    //public static final Logger log = Logger.getLogger(ZipUtil.class);
    private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);
    public static File crearZip(File result, String zipName) {

        try {
            File resultZip = FileUtil.createTempFile(zipName+".ZIP");
            BufferedInputStream origin = null;
            FileOutputStream dest = new
                    FileOutputStream(resultZip);
            ZipOutputStream out = new ZipOutputStream(new
                    BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            FileInputStream fi = new
                    FileInputStream(result);
            origin = new
                    BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(zipName+".XML");
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0,
                    BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            out.close();
            fi.close();
            return resultZip;
        } catch (Exception e) {
            log.error("CREAR ZIP", e);
        }
        return null;
    }

    public static File extractZip(File fileResponse) {
        File result = null;
        BufferedInputStream bis = null;
        FileInputStream fis=null;
        FileOutputStream fos=null;
        BufferedOutputStream dest = null;
        try {
            result = FileUtil.createTempFile(".XML");
            fis = new FileInputStream(fileResponse);
            bis = new BufferedInputStream(fis);
            ZipInputStream zis = new
                    ZipInputStream(bis);
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[BUFFER];
                fos = new
                        FileOutputStream(result);
                dest = new
                        BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
            }
            return result;
        } catch (Exception e) {
            log.error("Error en el sistema", e);
        } finally {
            FileUtil.close(bis);
            FileUtil.close(fis);
            FileUtil.close(dest);
            FileUtil.close(fos);
        }
        return result;
    }
    
    public void unCompressed_2(String zipeado, String output_dirZip){
        try {
        	final int BUFFER = 2048;
        	BufferedOutputStream dest = null;
        	FileInputStream fis = new FileInputStream(zipeado);
        	CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
        	ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
        	ZipEntry entry;
        	while((entry = zis.getNextEntry()) != null) {
        		log.info("Extracting: " +entry);
        		int count;
        		byte data[] = new byte[BUFFER];
        		// write the files to the disk
        		FileOutputStream fos = new FileOutputStream(entry.getName());
        		dest = new BufferedOutputStream(fos, BUFFER);
        		while ((count = zis.read(data, 0, BUFFER)) != -1) {
        			dest.write(data, 0, count);
        		}
        		dest.flush();
        		dest.close();
        	}
        	zis.close();
        	log.info("Checksum:"+  checksum.getChecksum().getValue());
        } catch(Exception e) {
        	e.printStackTrace();
        }
     }
    
	public static byte[] descomprimirArchivoMejorado(byte[] entrada) {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(entrada));
			ZipEntry zipEntry = null;
			byte data[] = new byte[10240];
			int count = 0;			
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (!zipEntry.isDirectory()) {
					while ((count = zipInputStream.read(data, 0, 10240)) != -1) {
						byteArrayOutputStream.write(data, 0, count);
					}
				}
			}
			byte[] bytesArraySalida = new byte[byteArrayOutputStream.toByteArray().length];
			bytesArraySalida = byteArrayOutputStream.toByteArray();
			zipInputStream.close();
			byteArrayOutputStream.close();
			return bytesArraySalida;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

    public byte[] comprimirArchivosAZip(Map<String, byte[]> files) throws Exception {
        ByteArrayInputStream isDocumentoOrigen = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        try {
            Iterator it = files.entrySet().iterator();
           
            while (it.hasNext()) {
                byte[] buf = new byte[2156];
                Map.Entry file = (Map.Entry) it.next();
                isDocumentoOrigen = new ByteArrayInputStream((byte[]) file.getValue());
                zipOutputStream.putNextEntry(new ZipEntry((String) file.getKey()));
                int len;
                while ((len = isDocumentoOrigen.read(buf)) != -1) {
                zipOutputStream.write(buf, 0, len);
                }
            }
            zipOutputStream.closeEntry();
        } catch (Exception excepcion) {
            log.error("ERROR: Se produjo un error al comprimir archivo", excepcion);
            throw new Exception(excepcion);
        } finally {
            zipOutputStream.close();
            byteArrayOutputStream.close();
            isDocumentoOrigen.close();
        }
        return byteArrayOutputStream.toByteArray();
    }


    public byte[] comprimirArchivoAZip(byte[] entrada, String nombreArchivo) throws Exception {
        ByteArrayInputStream isDocumentoOrigen = new ByteArrayInputStream(entrada);
        byte[] salida = null;
        int iTamBuffer = 10240;
        byte[] buf = new byte[iTamBuffer];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
            zipOutputStream.putNextEntry(new ZipEntry(nombreArchivo));
            int len;
            while ((len = isDocumentoOrigen.read(buf)) > 0) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry();
        } catch (Exception excepcion) {
            throw new Exception(excepcion);
        } finally {
            zipOutputStream.close();
            byteArrayOutputStream.close();
            isDocumentoOrigen.close();
        }
        salida = byteArrayOutputStream.toByteArray();
        return salida;
    }

    public byte[] descomprimirArchivo(byte[] entrada) throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipInputStream zipEntradaStream = null;
        byte[] bytesArraySalida = null;
        int count = 0;

        zipEntradaStream = new ZipInputStream(new ByteArrayInputStream(entrada));
        byte data[] = new byte[10240];

        ZipEntry entry = null;
        entry = zipEntradaStream.getNextEntry();

        while ((count = zipEntradaStream.read(data, 0, 10240)) != -1) {

            byteArrayOutputStream.write(data, 0, count);

        }
        bytesArraySalida = new byte[byteArrayOutputStream.toByteArray().length];
        bytesArraySalida = byteArrayOutputStream.toByteArray();

        zipEntradaStream.close();
        byteArrayOutputStream.close();

        return bytesArraySalida;

    }
    
    public ArrayList<byte[]> descomprimirArchivoZip(byte[] bytes) throws Exception {

        final int BUFFER = 10240;
        BufferedOutputStream dest = null;
        FileUtil fileUtil  = new FileUtil();
        FileInputStream fis = new FileInputStream(fileUtil.byteToFile(bytes, "zipTem", ".zip"));
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
        ZipEntry entry;
        ArrayList<byte[]> archosIntoZip = new ArrayList<byte[]>();

        while ((entry = zis.getNextEntry()) != null) {
            int count;
            byte data[] = new byte[BUFFER];
            //write the files to the disk
            FileOutputStream fos = new FileOutputStream(entry.getName());
            dest = new BufferedOutputStream(fos, BUFFER);
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }

            archosIntoZip.add(data);
            dest.flush();
            dest.close();
        }
        zis.close();

        return archosIntoZip;
    }

    public void unZipSave(String input_dirZip, String output_dirZip){
    	log.info("unZipSave input_dirZip: {} |output_dirZip: {}",input_dirZip,output_dirZip);
        byte[] buffer = new byte[Constantes.BUFFER_CONTENTFILE];
        try{        	
        	FileInputStream fis = new FileInputStream(input_dirZip);
        	CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
        	ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
        	ZipEntry ze = zis.getNextEntry();        	
        	int j=0;
        	while(ze!=null){
        		File fileOutput = new File(output_dirZip);        		
        		new File(fileOutput.getParent()).mkdirs();        		
        		FileOutputStream fos = new FileOutputStream(fileOutput);
        		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        		int len;
        		while ((len = zis.read(buffer, 0, Constantes.BUFFER_CONTENTFILE)) != -1) {
        			// Convierte el ze en file y lo graba en el HD como XML 
        			fos.write(buffer, 0, len);
        			// Convuerte el ze en byteArray
					//byteArrayOutputStream.write(buffer, 0, len);
				}
        		fos.close();
				byteArrayOutputStream.close();				
        		ze = zis.getNextEntry();     		
        		j++;
        	}
        	zis.closeEntry();
       		zis.close();       		       	
        }catch(IOException ex){
        	StringWriter errors = new StringWriter();				
			ex.printStackTrace(new PrintWriter(errors));
			log.error("unZipSave IOException \n"+errors);
			return;
       	}      
    }
    
    public static void decompressGzip(Path source, Path target) throws IOException {
        try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(source.toFile()));
             FileOutputStream fos = new FileOutputStream(target.toFile())) {
            // copy GZIPInputStream to FileOutputStream
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

        }catch(IOException ex){
        	StringWriter errors = new StringWriter();				
			ex.printStackTrace(new PrintWriter(errors));
			log.error("decompressGzip IOException \n"+errors);
			return;
       	}   

    }
    
    public static File getFileSUNAT(String nombreFile, byte[] byteOSE) {
    	try {
    		int indexPoint = nombreFile.lastIndexOf(".");
        	String prefix = nombreFile.substring(0, indexPoint);   
    		File file = FileUtil.writeToFilefromBytes(prefix, Constantes.OSE_FILE_XML, byteOSE);
    		return file;
    	}catch(Exception e) {
    		StringWriter errors = new StringWriter();				
    		e.printStackTrace(new PrintWriter(errors));
    		log.error("Exception \n"+errors);
    	}
        return null;
    }   
    
    public static byte[] zipFiles2Byte(List<File> files){
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
        	zipOut = new ZipOutputStream(bos);
            for(File f : files){
                File input = f;
                //log.info("Zipping the file: "+input.getName());
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4*1024];
                int size = 0;
                while((size = fis.read(tmp)) != -1){
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();	            
            //log.info("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("zipFiles2Byte Exception \n"+errors);
        } catch (IOException e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("zipFiles2Byte Exception \n"+errors);
        } finally{
            try{
                if(bos.size()>0) 
                	return bos.toByteArray();
            } catch(Exception e){
				StringWriter errors = new StringWriter();				
				e.printStackTrace(new PrintWriter(errors));
				log.info("zipFiles2Byte Exception \n"+errors);
            }
        }
        return null;
    }
    
    public static void zipFiles(List<File> files, String nameZip){
        
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(nameZip);
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for(File f : files){
                File input = f;
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                //log.info("Zipping the file: "+input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4*1024];
                int size = 0;
                while((size = fis.read(tmp)) != -1){
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            //log.info("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(fos != null) fos.close();
            } catch(Exception e){
            	e.printStackTrace();
            }
        }
    }
    
    public static void zipString(List<String> files, String nameZip){	        
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(nameZip);
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for(String filePath:files){
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                //log.info("Zipping the file: "+input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4*1024];
                int size = 0;
                while((size = fis.read(tmp)) != -1){
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            //log.info("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try{
                if(fos != null) fos.close();
            } catch(Exception ex){
                 
            }
        }
    }
   
}
