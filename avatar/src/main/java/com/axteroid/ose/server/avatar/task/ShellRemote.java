package com.axteroid.ose.server.avatar.task;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;import java.io.BufferedReader;import java.io.IOException;import java.io.InputStreamReader;import java.io.PrintWriter;import java.io.StringWriter;import java.util.HashMap;
import java.util.Map;import java.util.concurrent.TimeUnit;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import com.axteroid.ose.server.avatar.MainOSE;import com.axteroid.ose.server.tools.constantes.Constantes;import com.axteroid.ose.server.tools.util.FileUtil;
public class ShellRemote {	
	static String username;	
	static String password;	
	static Robot bot;
	static Map<Character, Integer> mapStrokes;
	static Process p;
	static Runtime r;
	//String cadena;	static Logger log = LoggerFactory.getLogger(MainOSE.class);	public static void main(String[] args) throws InterruptedException, IOException, AWTException {		ShellRemote shellRemote = new ShellRemote();		//shellRemote.ejecutarCambioFecha("sudo date 072818002019", "172.19.71.137", "OseServer", "OseServer2018,");		shellRemote.ejecutarCambioFecha("sudo curl http://172.18.138.44:6080/ol-ti-itcpe/billService?wsdl", "172.18.138.44", "OseServer", "OseServer2018,");		System.exit(0);	}		public ShellRemote() {} 	
	public void ejecutarCambioFecha(String cadena, String ip, String usuario, String clave)  {		
		try {			username = usuario;			password = clave;					CorrerPrograma(ip);			MapearCaracteres();			ImprimirCadena(cadena);			ColocarPassword();			CerrarPrograma();		} catch (IOException e) {			StringWriter errors = new StringWriter();							e.printStackTrace(new PrintWriter(errors));			log.error("Exception \n"+errors);		}		
	}		public void ejecutarReiniciarJBoss(String ip, String usuario, String clave)  {				try {			username = usuario;			password = clave;					CorrerPrograma(ip);			MapearCaracteres();			ImprimirCadena("sudo systemctl stop jbosseap_73.service ");				ColocarPassword();			Thread.sleep(120000);			ImprimirCadena("sudo systemctl start jbosseap_73.service ");			ColocarPassword();						Thread.sleep(180000);			CerrarPrograma();		} catch (InterruptedException | IOException e) {			StringWriter errors = new StringWriter();							e.printStackTrace(new PrintWriter(errors));			log.error("Exception \n"+errors);		}			}	
	private void CorrerPrograma(String ip) {		try{
			r = Runtime.getRuntime(); 
			String s = "D:\\ebiz\\ide\\PuTTY\\putty.exe -ssh -l "+username+" -pw "+password+" "+ip;			if(FileUtil.tobeDirectory(Constantes.DIR_AXTEROID_OSE))				s = "E:\\ebiz\\ide\\PuTTY\\putty.exe -ssh -l "+username+" -pw "+password+" "+ip;			log.info("shell: "+s);		
			p = r.exec(s);			Thread.sleep(4000);
		} catch (Exception e) {		
			log.info("No encuentra el programa instalado.");			StringWriter errors = new StringWriter();							e.printStackTrace(new PrintWriter(errors));			log.error("Exception \n"+errors);
		}
	}	
	private void ImprimirCadena(String cadena) {			try {			Thread.sleep(5000);			log.info("cadena: "+cadena);	
			bot = new Robot();		
			for (char value : cadena.toCharArray()) {				log.info("1 value: "+value);
				if (Character.isLetter(value)) {					log.info("2 value: "+value);
					if (Character.isUpperCase(value)) {
				        bot.keyPress(KeyEvent.VK_SHIFT);
				    }				
				    int keyStroke = mapStrokes.get(Character.toLowerCase(value));			    
				    bot.keyPress(keyStroke);
				    bot.keyRelease(keyStroke);
				    if (Character.isUpperCase(value)) {
				        bot.keyRelease(KeyEvent.VK_SHIFT);
				    }
				}else {					log.info("3 value: "+value);
					int keyStroke = mapStrokes.get(value);					if(keyStroke == 513) {						bot.keyPress(KeyEvent.VK_SHIFT);  			            bot.keyPress(KeyEvent.VK_SEMICOLON);  			            bot.keyRelease(KeyEvent.VK_SEMICOLON);  			            bot.keyRelease(KeyEvent.VK_SHIFT);					}else {						log.info("3 keyStroke: "+keyStroke);
						bot.keyPress(keyStroke);
						bot.keyRelease(keyStroke);					}
				}		    
			}			bot.keyPress(KeyEvent.VK_ENTER);
			bot.keyRelease(KeyEvent.VK_ENTER);		} catch (IllegalArgumentException | AWTException | InterruptedException e) {			log.info("Error cambiando de fecha en el Putty, por favor, cambiar de fecha manualmente.");			StringWriter errors = new StringWriter();							e.printStackTrace(new PrintWriter(errors));			log.error("Exception \n"+errors);		}	}
	private void ColocarPassword() {		try {			Thread.sleep(4000);			for (char value : password.toCharArray()) {
				if (Character.isLetter(value)) {					if (Character.isUpperCase(value)) {
				        bot.keyPress(KeyEvent.VK_SHIFT);
				    }				    int keyStroke = mapStrokes.get(Character.toLowerCase(value));				    bot.keyPress(keyStroke);
				    bot.keyRelease(keyStroke);
				    if (Character.isUpperCase(value)) {
				        bot.keyRelease(KeyEvent.VK_SHIFT);
				    }
				}else {
					int keyStroke = mapStrokes.get(value);
					bot.keyPress(keyStroke);
					bot.keyRelease(keyStroke);
				}			}			bot.keyPress(KeyEvent.VK_ENTER);
			bot.keyRelease(KeyEvent.VK_ENTER);		} catch (IllegalArgumentException | InterruptedException e) {			log.info("Error cambiando de fecha en el Putty, por favor, cambiar de fecha manualmente.");			StringWriter errors = new StringWriter();							e.printStackTrace(new PrintWriter(errors));			log.error("Exception \n"+errors);
		}
	}	private void CerrarPrograma() throws IOException {
		try {			Thread.sleep(4000);
			r.exec("taskkill /F /IM putty.exe");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();							e.printStackTrace(new PrintWriter(errors));			log.error("Exception \n"+errors);
		}
	}
	private void MapearCaracteres() throws IOException {
		mapStrokes = new HashMap<Character, Integer>();
		Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, false); 		
		mapStrokes.put('a', KeyEvent.VK_A);		mapStrokes.put('b', KeyEvent.VK_B);		mapStrokes.put('c', KeyEvent.VK_C);		mapStrokes.put('d', KeyEvent.VK_D);		
		mapStrokes.put('e', KeyEvent.VK_E);		mapStrokes.put('f', KeyEvent.VK_F);		mapStrokes.put('g', KeyEvent.VK_G);		mapStrokes.put('h', KeyEvent.VK_H);		mapStrokes.put('i', KeyEvent.VK_I);		mapStrokes.put('j', KeyEvent.VK_J);
		mapStrokes.put('k', KeyEvent.VK_K);
		mapStrokes.put('l', KeyEvent.VK_L);		mapStrokes.put('m', KeyEvent.VK_M);
		mapStrokes.put('n', KeyEvent.VK_N);		mapStrokes.put('o', KeyEvent.VK_O);
		mapStrokes.put('p', KeyEvent.VK_P);
		mapStrokes.put('q', KeyEvent.VK_Q);		mapStrokes.put('r', KeyEvent.VK_R);				mapStrokes.put('s', KeyEvent.VK_S);		mapStrokes.put('t', KeyEvent.VK_T);		mapStrokes.put('u', KeyEvent.VK_U);		mapStrokes.put('v', KeyEvent.VK_V);		mapStrokes.put('w', KeyEvent.VK_W);		mapStrokes.put('x', KeyEvent.VK_X);		mapStrokes.put('y', KeyEvent.VK_Y);		mapStrokes.put('z', KeyEvent.VK_Z);		mapStrokes.put('0', KeyEvent.VK_0);				
		mapStrokes.put('1', KeyEvent.VK_1);
		mapStrokes.put('2', KeyEvent.VK_2);
		mapStrokes.put('3', KeyEvent.VK_3);
		mapStrokes.put('4', KeyEvent.VK_4);
		mapStrokes.put('5', KeyEvent.VK_5);
		mapStrokes.put('6', KeyEvent.VK_6);
		mapStrokes.put('7', KeyEvent.VK_7);
		mapStrokes.put('8', KeyEvent.VK_8);
		mapStrokes.put('9', KeyEvent.VK_9);		mapStrokes.put(' ', KeyEvent.VK_SPACE);		mapStrokes.put(',', KeyEvent.VK_COMMA);
		mapStrokes.put('.', KeyEvent.VK_PERIOD);		mapStrokes.put(':', KeyEvent.VK_COLON);		mapStrokes.put('/', KeyEvent.VK_SLASH);		mapStrokes.put('?', KeyEvent.VK_AMPERSAND);
	}	public static String executeCommand(String command, int waitSeconds){		try {			log.info("command: "+command);		    StringBuffer output = new StringBuffer();		    BufferedReader reader = null;		    Process p = Runtime.getRuntime().exec(command);		    p.waitFor(waitSeconds, TimeUnit.SECONDS);		    reader = new BufferedReader(new InputStreamReader(p.getInputStream()));		    		    String line = "";		    while ((line = reader.readLine()) != null) {		      output.append(line + "\n");		    }   		    //log.info("output.toString(): "+output.toString());		    reader.close();    		    return output.toString();		} catch (IOException | InterruptedException e) {			StringWriter errors = new StringWriter();							e.printStackTrace(new PrintWriter(errors));			log.error("executeCommand Exception \n"+errors);		}		return "";	}		public void testJBoss(String ip, String usuario, String clave)  {				try {			username = usuario;			password = clave;					CorrerPrograma(ip);			MapearCaracteres();			ImprimirCadena("sudo systemctl stop jbosseap_73.service ");				ColocarPassword();			Thread.sleep(120000);			//ImprimirCadena("sudo systemctl start jbosseap_73.service ");			//ColocarPassword();						//Thread.sleep(180000);			CerrarPrograma();		} catch (InterruptedException | IOException e) {			StringWriter errors = new StringWriter();							e.printStackTrace(new PrintWriter(errors));			log.error("Exception \n"+errors);		}			}	
}
