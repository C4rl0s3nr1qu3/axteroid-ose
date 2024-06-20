package com.axteroid.ose.server.apirest.mail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EnviaEmail {

	private static InternetAddress[] llena() {
		String[] to = {"programador.ddee1@dataimagenes.pe"};

		InternetAddress[] addressTo = new InternetAddress[to.length];
		try {
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return addressTo;
	}		

	public void postProceso(String correo, String sSubject) {
		final String username = "scpro@dataimagenes.pe";
		final String password = "Password2014";

		InternetAddress[] direcciones = llena();

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			MimeMessage message = new MimeMessage(session);
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
            
			message.setFrom(new InternetAddress("scpro@dataimagenes.pe"));
			message.setRecipients(Message.RecipientType.TO, direcciones);
			message.setSubject("ERROR : "+sSubject);
		
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			//Cuerpo en texto
	        messageBodyPart = new MimeBodyPart();
	        Multipart multipart = new MimeMultipart("related");

            messageBodyPart.setContent(correo, "text/html");
            multipart.addBodyPart(messageBodyPart);
	        
	        message.setContent(multipart);	        
			Transport.send(message);
			System.out.println("Done");			
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void postProcesoFile(String correo, String filename, String filePath) {
		final String username = "scpro@dataimagenes.pe";
		final String password = "Password2014";

		InternetAddress[] direcciones = llena();

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			MimeMessage message = new MimeMessage(session);
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
            
			message.setFrom(new InternetAddress("scpro@dataimagenes.pe"));
			message.setRecipients(Message.RecipientType.TO, direcciones);
			message.setSubject("Reporte BBVA");
		
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			//Cuerpo en texto
	        messageBodyPart = new MimeBodyPart();
	        Multipart multipart = new MimeMultipart("related");

            messageBodyPart.setContent(correo, "text/html");
            multipart.addBodyPart(messageBodyPart);

//	        Cuerpo de archivo adjunto

            messageBodyPart = new MimeBodyPart();
	        String file = filePath;
	        String fileName =  filename;
	        FileDataSource source = new FileDataSource(file);
	        messageBodyPart.setDataHandler(new DataHandler((javax.activation.DataSource) source));
	        messageBodyPart.setFileName(fileName);
	        multipart.addBodyPart(messageBodyPart);
	        
	        message.setContent(multipart);	        
			Transport.send(message);
			System.out.println("Done");
			File arch = new File(file);
			arch.delete();
			
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void enviaEmailVacio() throws IOException {
		final String username = "scpro@dataimagenes.pe";
		final String password = "Password2014";

		InternetAddress[] direcciones = llena();

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			MimeMessage message = new MimeMessage(session);
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
            
			message.setFrom(new InternetAddress("scpro@dataimagenes.pe"));
			message.setRecipients(Message.RecipientType.TO, direcciones);
			message.setSubject("Reporte BBVA");
		
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			//Cuerpo en texto
	        messageBodyPart = new MimeBodyPart();
	        Multipart multipart = new MimeMultipart("related");

            messageBodyPart.setContent("<H1>No se encuentran nuevos registros<H1>", "text/html");
            multipart.addBodyPart(messageBodyPart);

          
	        
	        message.setContent(multipart);
			Transport.send(message);

			System.out.println("Done");
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static InternetAddress[] llenaFTP() {
		String[] to = {"programador.ddee1@dataimagenes.pe","operaciones@dataimagenes.pe","recuperador@dataimagenes.pe", 
				"Roger.Rodriguez@dataimagenes.pe","CdP@dataimagenes.pe", "Desarrollo@dataimagenes.pe"};

		InternetAddress[] addressTo = new InternetAddress[to.length];
		try {
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return addressTo;
	}
	
	public void postProcesoFTP(String correo, String sSubject) {
		final String username = "scpro@dataimagenes.pe";
		final String password = "Password2014";

		InternetAddress[] direcciones = llenaFTP();

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			MimeMessage message = new MimeMessage(session);
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
            
			message.setFrom(new InternetAddress("scpro@dataimagenes.pe"));
			message.setRecipients(Message.RecipientType.TO, direcciones);
			message.setSubject("SCDIS ERROR : "+sSubject);
		
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			//Cuerpo en texto
	        messageBodyPart = new MimeBodyPart();
	        Multipart multipart = new MimeMultipart("related");

            messageBodyPart.setContent(correo, "text/html");
            multipart.addBodyPart(messageBodyPart);
	        
	        message.setContent(multipart);	        
			Transport.send(message);
			System.out.println("Done");			
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	private static InternetAddress[] llenaSCDIS() {
		String[] to = {"programador.ddee1@dataimagenes.pe","operaciones@dataimagenes.pe",
				"Roger.Rodriguez@dataimagenes.pe","CdP@dataimagenes.pe","recuperador@dataimagenes.pe"};

		InternetAddress[] addressTo = new InternetAddress[to.length];
		try {
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return addressTo;
	}
	
	public void postProcesoSCDIS(String correo, String sSubject) {
		final String username = "scpro@dataimagenes.pe";
		final String password = "Password2014";

		InternetAddress[] direcciones = llenaSCDIS();

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			MimeMessage message = new MimeMessage(session);
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
            
			message.setFrom(new InternetAddress("scpro@dataimagenes.pe"));
			message.setRecipients(Message.RecipientType.TO, direcciones);
			message.setSubject("SCDIS GRABO : "+sSubject);
		
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			//Cuerpo en texto
	        messageBodyPart = new MimeBodyPart();
	        Multipart multipart = new MimeMultipart("related");

            messageBodyPart.setContent(correo, "text/html");
            multipart.addBodyPart(messageBodyPart);
	        
	        message.setContent(multipart);	        
			Transport.send(message);
			System.out.println("Done");			
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static InternetAddress[] llenaVIG() {
		String[] to = {"programador.ddee1@dataimagenes.pe","Claudio.Obregon@dataimagenes.pe"};

		InternetAddress[] addressTo = new InternetAddress[to.length];
		try {
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return addressTo;
	}	
	
	public void postProcesoVIG(String correo, String sSubject) {
		final String username = "scpro@dataimagenes.pe";
		final String password = "Password2014";

		InternetAddress[] direcciones = llenaVIG();

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			MimeMessage message = new MimeMessage(session);
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
            
			message.setFrom(new InternetAddress("scpro@dataimagenes.pe"));
			message.setRecipients(Message.RecipientType.TO, direcciones);
			message.setSubject("Monitoreo ERROR : "+sSubject);
		
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			//Cuerpo en texto
	        messageBodyPart = new MimeBodyPart();
	        Multipart multipart = new MimeMultipart("related");

            messageBodyPart.setContent(correo, "text/html");
            multipart.addBodyPart(messageBodyPart);
	        
	        message.setContent(multipart);	        
			Transport.send(message);
			System.out.println("Done");			
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
