package com.axteroid.ose.server.avatar.task;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Map;
public class ShellRemote {
	static String username;	
	static String password;	
	static Robot bot;
	static Map<Character, Integer> mapStrokes;
	static Process p;
	static Runtime r;
	//String cadena;
	public void ejecutarCambioFecha(String cadena, String ip, String usuario, String clave)  {		
		try {
	}
	private void CorrerPrograma(String ip) {
			r = Runtime.getRuntime(); 
			String s = "D:\\ebiz\\ide\\PuTTY\\putty.exe -ssh -l "+username+" -pw "+password+" "+ip;
			p = r.exec(s);
		} catch (Exception e) {		
			log.info("No encuentra el programa instalado.");
		}
	}
	private void ImprimirCadena(String cadena) {	
			bot = new Robot();		
			for (char value : cadena.toCharArray()) {
				if (Character.isLetter(value)) {
					if (Character.isUpperCase(value)) {
				        bot.keyPress(KeyEvent.VK_SHIFT);
				    }				
				    int keyStroke = mapStrokes.get(Character.toLowerCase(value));			    
				    bot.keyPress(keyStroke);
				    bot.keyRelease(keyStroke);
				    if (Character.isUpperCase(value)) {
				        bot.keyRelease(KeyEvent.VK_SHIFT);
				    }
				}else {
					int keyStroke = mapStrokes.get(value);
						bot.keyPress(keyStroke);
						bot.keyRelease(keyStroke);
				}		    
			}
			bot.keyRelease(KeyEvent.VK_ENTER);
	private void ColocarPassword() {
				if (Character.isLetter(value)) {
				        bot.keyPress(KeyEvent.VK_SHIFT);
				    }
				    bot.keyRelease(keyStroke);
				    if (Character.isUpperCase(value)) {
				        bot.keyRelease(KeyEvent.VK_SHIFT);
				    }
				}else {
					int keyStroke = mapStrokes.get(value);
					bot.keyPress(keyStroke);
					bot.keyRelease(keyStroke);
				}
			bot.keyRelease(KeyEvent.VK_ENTER);
		}
	}
		try {
			r.exec("taskkill /F /IM putty.exe");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();				
		}
	}
	private void MapearCaracteres() throws IOException {
		mapStrokes = new HashMap<Character, Integer>();
		Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, false); 		
		mapStrokes.put('a', KeyEvent.VK_A);
		mapStrokes.put('e', KeyEvent.VK_E);
		mapStrokes.put('k', KeyEvent.VK_K);
		mapStrokes.put('l', KeyEvent.VK_L);
		mapStrokes.put('n', KeyEvent.VK_N);
		mapStrokes.put('p', KeyEvent.VK_P);
		mapStrokes.put('q', KeyEvent.VK_Q);
		mapStrokes.put('1', KeyEvent.VK_1);
		mapStrokes.put('2', KeyEvent.VK_2);
		mapStrokes.put('3', KeyEvent.VK_3);
		mapStrokes.put('4', KeyEvent.VK_4);
		mapStrokes.put('5', KeyEvent.VK_5);
		mapStrokes.put('6', KeyEvent.VK_6);
		mapStrokes.put('7', KeyEvent.VK_7);
		mapStrokes.put('8', KeyEvent.VK_8);
		mapStrokes.put('9', KeyEvent.VK_9);
		mapStrokes.put('.', KeyEvent.VK_PERIOD);
	}
}