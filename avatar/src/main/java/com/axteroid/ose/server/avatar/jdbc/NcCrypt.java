package com.axteroid.ose.server.avatar.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.DESedeParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class NcCrypt { 
	
	public static String encriptarPassword(String strpassword) {
        String strkey= "7C91DE760CCDFEEF".toLowerCase();
        String cadena = NcCrypt.encrypt(strpassword, strkey).trim();

        String inicio="x\'";
        String hexa = inicio;
        for (int i=0; i<cadena.length(); i++) {
            char caracter = cadena.charAt(i);
            String hex = Integer.toHexString(caracter).toUpperCase();
            hexa+=hex;
        }
        for (int i=0; hexa.length()/2<=32; i++) {
            hexa+="20";
        }
        hexa+="\'";

        return hexa;
    }

    public static String desencriptarPassword(String strpassword) {
        String strkey= "7C91DE760CCDFEEF".toLowerCase();
        if(strpassword == null || strpassword.equals("")){
            return "";
        }
        //borro la x'
        strpassword = strpassword.substring(2);
        strpassword = strpassword.substring(0,strpassword.length()-1);

        byte [] b = NcCrypt.hex2byte(strpassword);

        String strcadena = "";
        for (int i=0; i<b.length; i++) {
            char c = (char)b[i];
            strcadena+=c;
        }

        String ascii = NcCrypt.decrypt(strcadena.trim(), strkey).trim();

        return ascii;
    }

    public static String desencriptarPassword(String concepto ,String strpassword) {
    	//log.info("Desencriptando concepto: " + concepto);
        String strkey = "7C91DE760CCDFEEF".toLowerCase();
        //borro la x'
        strpassword = strpassword.substring(2);
        strpassword = strpassword.substring(0, strpassword.length() - 1);
        byte[] b = NcCrypt.hex2byte(strpassword);
        String strcadena = "";
        for (int i = 0; i < b.length; i++) {
            char c = (char) b[i];
            strcadena += c;
        }
        //log.info("Desencriptando FIN strcadena: " + strcadena);

        String result = NcCrypt.decrypt(strcadena.trim(), strkey);
        //log.info("Desencriptando FIN  result: " + result);
        if (StringUtils.isBlank(result)) {
            throw new IllegalArgumentException("problemas al usar NcCrypt.decrypt()");
        }
        String ascii = result.trim();
        //log.info("Desencriptando FIN  " + concepto);
        return ascii;
    }
    
    public static byte[] createKey(byte abyte0[])
    {
        byte abyte1[] = new byte[24];
        nc_decode_key(abyte0, abyte1);
        byte abyte2[] = new byte[16];
        gen2(abyte2);
        byte abyte3[] = new byte[16];
        nc_decode_key(abyte2, abyte3);
        memcpy(abyte1, 8, abyte3, 0, 8);
        memset(abyte2, 0, 8);
        byte abyte4[] = new byte[16];
        gen3(abyte4);
        byte abyte5[] = new byte[16];
        nc_decode_key(abyte4, abyte5);
        memcpy(abyte1, 16, abyte5, 0, 8);
        memset(abyte4, 0, 8);
        return abyte1;
    }

    public static synchronized String decrypt(String s, String s1){
        byte abyte0[] = null;
        byte abyte2[] = {
            -2, -36, -70, 33, -112, 67, -121, 101
        };
        byte abyte3[] = new byte[16];
        if(s1 != null)
            abyte3 = stringToBytes(s1);
        else
            gen1(abyte3);
               
        byte[] out = null;        
        try{        	
        	abyte0 = Base64.decodeBase64(s.getBytes());        	
        }catch(Exception exception) {}
        try{
            byte abyte4[] = createKey(abyte3);       	      	
	       	PKCS7Padding padding = new PKCS7Padding();
	       	BufferedBlockCipher bcif = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()), padding);	       	
	       	bcif.reset();
	       	bcif.init(false, new ParametersWithIV(new DESedeParameters(abyte4), abyte2));	       
	       	byte[] buf = new byte[bcif.getOutputSize(abyte0.length)];	       	
	       	int len = bcif.processBytes(abyte0, 0, abyte0.length, buf, 0);	       	
	       	len += bcif.doFinal(buf, len);       	
	       	out = new byte[len];
	       	System.arraycopy(buf, 0, out, 0, len);                        
        }catch(Exception exception1){
            System.err.println("%3DES-F-CIPHERINIT; Exception caught while initializing the cipher object.");
            System.err.println("; " + exception1.toString());
            return null;
        }

        for(int j = 0; j < abyte0.length; j++)
            abyte0[j] = 0;

        String s2 = new String(out);
        return s2.trim();
    }

    public static synchronized String encrypt(String s, String s1) {
        byte abyte0[] = null;
     
        byte abyte2[] = {
            -2, -36, -70, 33, -112, 67, -121, 101
        };
        byte abyte3[] = new byte[16];
        if(s1 != null)
            abyte3 = stringToBytes(s1);
        else
            gen1(abyte3);
        abyte0 = stringToBytes(s);
        
        byte[] out = null;
        try {       	
        	byte abyte4[] = createKey(abyte3);
        	
        	PKCS7Padding padding = new PKCS7Padding();
        	BufferedBlockCipher bcif = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()), padding);
        	
        	bcif.reset();
        	bcif.init(true, new ParametersWithIV(new DESedeParameters(abyte4), abyte2));
        
        	byte[] buf = new byte[bcif.getOutputSize(abyte0.length)];
        	
        	int len = bcif.processBytes(abyte0, 0, abyte0.length, buf, 0);
        	
        	len += bcif.doFinal(buf, len);
        	
            out = new byte[len];
            System.arraycopy(buf, 0, out, 0, len);                                
        }catch(Exception exception){
            System.err.println("%3DES-F-CIPHERINIT; Exception caught while initializing the cipher object.");
            System.err.println("; " + exception.toString());
            return null;
        }
        String s2 = null;
        s2 = new String(Base64.encodeBase64(out));     	
        return s2;
    }

    static void gen1(byte abyte0[]){
        abyte0[0] = code[8];
        abyte0[1] = code[13];
        abyte0[2] = code[7];
        abyte0[3] = code[2];
        abyte0[4] = code[15];
        abyte0[5] = code[6];
        abyte0[6] = code[1];
        abyte0[7] = code[11];
        abyte0[8] = code[0];
        abyte0[9] = code[3];
        abyte0[10] = code[9];
        abyte0[11] = code[15];
        abyte0[12] = code[1];
        abyte0[13] = code[10];
        abyte0[14] = code[10];
        abyte0[15] = code[12];
    }

    static void gen2(byte abyte0[]) {
        abyte0[0] = code[3];
        abyte0[1] = code[11];
        abyte0[2] = code[10];
        abyte0[3] = code[3];
        abyte0[4] = code[15];
        abyte0[5] = code[1];
        abyte0[6] = code[9];
        abyte0[7] = code[5];
        abyte0[8] = code[1];
        abyte0[9] = code[13];
        abyte0[10] = code[2];
        abyte0[11] = code[11];
        abyte0[12] = code[5];
        abyte0[13] = code[13];
        abyte0[14] = code[15];
        abyte0[15] = code[3];
    }

    static void gen3(byte abyte0[]){
        abyte0[0] = code[11];
        abyte0[1] = code[1];
        abyte0[2] = code[4];
        abyte0[3] = code[8];
        abyte0[4] = code[6];
        abyte0[5] = code[15];
        abyte0[6] = code[2];
        abyte0[7] = code[11];
        abyte0[8] = code[0];
        abyte0[9] = code[13];
        abyte0[10] = code[2];
        abyte0[11] = code[11];
        abyte0[12] = code[15];
        abyte0[13] = code[10];
        abyte0[14] = code[12];
        abyte0[15] = code[0];
    }

    public static byte[] hex2byte(String cadena) {
        int       j = 0;
        int    size = 0;
        String sHex = null;

        size = cadena.length()/2;
        byte[] respuesta = new byte[size];


        for (int i=0; i <= (cadena.length()-2); i=i+2) {
            sHex = cadena.substring(i,i+2);
            respuesta[j] = (byte) ( Integer.parseInt( sHex ,16) );
            j++;
        }
        return respuesta;
    }


    static void memcpy(byte abyte0[], int i, byte abyte1[], int j, int k) {
        for(int l = 0; l < k; l++)
            abyte0[l + i] = abyte1[l + j];
    }

    static void memset(byte abyte0[], int i, int j) {
        for(int k = 0; k < j; k++)
            abyte0[k] = (byte)i;
    }

    static int nc_decode_key(byte abyte0[], byte abyte1[]) {
        int k = 0;
        for(int j = 0; j < CODE_LENGTH;) {
            int i;
            for(i = 0; code[i] != abyte0[j] && i < CODE_LENGTH; i++);

            if(i < CODE_LENGTH)
                abyte1[k] = (byte)(i << 4);
            else
                return 0;
            i = 0;
            for(j++; code[i] != abyte0[j] && i < CODE_LENGTH; i++);
            if(i < CODE_LENGTH)
                abyte1[k] |= (byte)i;
            else
                return 0;
            j++;
            k++;
        }

        return 1;
    }

    public static byte[] stringToBytes(String s) {
        int i = s.length();
        byte abyte0[] = new byte[i];
        try{
            for(int j = 0; j < i; j++)
                abyte0[j] = (byte)s.charAt(j);
        }
        catch(NumberFormatException _ex){

        }
        return abyte0;
    }

    public static final String IBM_COPYRIGHT = "Licensed Materials - Property of IBM\n\n5697-D24\n\n(c) Copyright IBM Corp. 1996 (1998). All Rights Reserved.\n\nUS Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.\n";
    static final int DES_KEY_STRING_LEN = 16;
    static final int DES_KEY_SIZE = 8;
    static final int TRIPLE_DES_KEY_SIZE = 24;
    static int CODE_LENGTH = 16;
    static byte code[] = {
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        97, 98, 99, 100, 101, 102
    };

    public static void main(String[] args) {   
    	//log.info("main ");
    	List<String> listCadena = new ArrayList<String>();
    	listCadena.add("1234");
    	listCadena.add("alignetsfe");
    	listCadena.add("20478005017");
    	listCadena.add("Jksoseb3zl3nks.");
    	listCadena.add("B3zl3nksOs2.");
    	NcCrypt ncCrypt = new NcCrypt();
    	ncCrypt.getListEncript(listCadena);
    }
    
	public void getListEncript(List<String> listCadena) {		
		
		//log.info("getListEncript ");
		for(String cadena : listCadena) {		
			String encriptado = NcCrypt.encriptarPassword(cadena);
			//log.info("Cadena : "+cadena+" - encriptado : "+encriptado);
		}
	}
	
	public static String getWSPasswordProd(String val) {	
		//("getListDesencript_DB ");
		RsetPostGresJDBC documentoJDBCPROD_PostGres = new RsetPostGresJDBC();
		String parametro = "SEC";
		String cadena = documentoJDBCPROD_PostGres.listComprobantes_Parametro(parametro,val);
		String encriptado = NcCrypt.desencriptarPassword(cadena);
		System.out.println("usuario: "+val+" - Cadena: "+cadena+" - desencriptado: "+encriptado);
		return encriptado;
	}	
	
	public static String getWSPasswordTest(String val) {	
		//System.out.println("getWSPasswordTest: "+val);
		RsetPostGresJDBC documentoJDBCPROD_PostGres = new RsetPostGresJDBC();
		String parametro = "SEC";
		String cadena = documentoJDBCPROD_PostGres.listComprobantes_Parametro(parametro,val);
		String encriptado = NcCrypt.desencriptarPassword(cadena);
		//System.out.println("usuario: "+val+" - Cadena: "+cadena+" - desencriptado: "+encriptado);
		return encriptado;
	}	
	
	public static String getWSPasswordDesa(String val) {	
		//("getListDesencript_DB ");
		RsetPostGresJDBC documentoJDBCPROD_PostGres = new RsetPostGresJDBC();
		String parametro = "SEC";
		String cadena = documentoJDBCPROD_PostGres.listComprobantes_Parametro(parametro,val);
		String encriptado = NcCrypt.desencriptarPassword(cadena);
		//System.out.println("usuario: "+val+" - Cadena: "+cadena+" - desencriptado: "+encriptado);
		return encriptado;
	}	
}
