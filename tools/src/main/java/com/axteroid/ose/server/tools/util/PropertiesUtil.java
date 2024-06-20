package com.axteroid.ose.server.tools.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.tools.constantes.Constantes;

public class PropertiesUtil {
	private final static Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
	
	public static void cambiarEstado(String ruta, String lineaActual, String lineaNueva) {
		File file = new File(ruta + Constantes.FILE_AVATAR_PROPER);
		modificar(file, ruta, lineaActual, lineaNueva);
	}
	
	static void escribir(File fFichero,String cadena){
        BufferedWriter bw;
        try{
        	if(!fFichero.exists()){
        		fFichero.createNewFile();
        	}
        	bw = new BufferedWriter(new FileWriter(fFichero,true));
            bw.write(cadena);
            bw.close();
        }catch(Exception e){
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("Exception \n"+errors);
        }
    }
	
	static void modificar(File fAntiguo,String ruta,String lineaActual,String lineaNueva){
		String rutaNueva = ruta + Constantes.FILE_AVATAR_PROPER;
        File fNuevo= new File(rutaNueva);
        // Declaro un nuevo buffer de lectura
        BufferedReader br;
        try{
            if(fAntiguo.exists()){
                br = new BufferedReader(new FileReader(fAntiguo));
                String linea;
                while((linea=br.readLine()) != null) {
                    if(linea.equals(lineaActual)){
                        escribir(fNuevo,lineaNueva + "\n");
                    }else{
                        escribir(fNuevo,linea + "\n");
                    }
                }
                // Cierro el buffer de lectura
                br.close();
                // Capturo el nombre del fichero antiguo
                //String nAntiguo = fAntiguo.getName();
                // Borro el fichero antiguo
                borrar(fAntiguo);
                //Renombro el fichero auxiliar con el nombre del fichero antiguo
                fNuevo.renameTo(fAntiguo);
            }else{
            	log.info("Fichero no Existe");
            }
        }catch(Exception e){
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("Exception \n"+errors);
        }                
    }
	
	static void borrar (File Ffichero){
        try {
           // Comprovamos si el fichero existe  de ser así procedemos a borrar el archivo
            if(Ffichero.exists()){
                Ffichero.delete();
                log.info("Ficherro Borrado");
            }
        }catch(Exception e){
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("Exception \n"+errors);
        }
    }
    
    public static Map<String, String> devolverMapProper(String dirFile){    
    	//log.info("dirFile: "+dirFile);
    	Map<String,String> mapa = new HashMap<String,String>(); 
    	File file = new File(dirFile);
    	try (
			FileReader reader = new FileReader(file);
    		BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
            	//log.info("line: "+line);
            	String[] array = line.split(":=");    
            	if(array.length > 1)
            		mapa.put(array[0], array[1]);
            }               	
//	    	Iterator<String> it = mapa.keySet().iterator();
//			while(it.hasNext()){
//			  String key = it.next();
//			  log.info("Key: " + key + " - Valor: " + mapa.get(key));
//			}
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("devolverFileProper Exception \n"+errors);
		}    	
    	return mapa;
    }
  
    public static List<String> devolverListProper(String dirFile){    
    	//log.info("dirFile: "+dirFile);
    	List<String> mapa = new ArrayList<String>();
    	File file = new File(dirFile);
    	try (
			FileReader reader = new FileReader(file);
    		BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {             
               mapa.add(line);
            }               	
		}catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("devolverListProper Exception \n"+errors);
		}    	
    	return mapa;
    }
    
}
