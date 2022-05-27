package Recursos;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EnviaMail {
			
	final String usuario ="tu_email";
			
	final String password = "tu_password";
	
	public EnviaMail() {
		
	}
	
	Comodin cmd = new Comodin();

	//---------------------------------------------------> M�todo que env�a un mail con gmail utilizando protocolo TLS <--------------------------------------------------------------
	
	public void EnviaMailTLS(String destinatario, String asunto, String mensaje) {
		
		Properties propiedades = new Properties();
		
		propiedades.put("mail.smtp.host", "smtp.gmail.com");
		
		propiedades.put("mail.smtp.port", "587");
		
		propiedades.put("mail.smtp.auth", "true");
		
		propiedades.put("mail.smtp.starttls.enable", "true");
		
		Session session = Session.getInstance(propiedades, new javax.mail.Authenticator() {
			
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(usuario, password);
			}
			
		});
		
		try {
			
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(usuario));  //!!!!
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
			
			message.setSubject(asunto);
			
			message.setText(mensaje);
			
			Transport.send(message);
			
			cmd.ventanaConfirmaci�n("Confirmaci�n", "E-mail enviado", "El c�digo de recuperaci�n se ha enviado a su correo electr�nico");
			
			System.out.println("Enviado");
			
		}catch(MessagingException e) {
			
			cmd.ventanaError("Error", "Error al enviar el E-mail", "No se ha podido enviar el c�digo de recuperaci�n." + "\nInt�ntalo en unos minutos");
			
			System.out.println("Error al enviar el mensaje");
			
			e.printStackTrace();
			
		}

	}
	
	//---------------------------------------------------> M�todo que env�a un mail con gmail utilizando protocolo TLS <--------------------------------------------------------------
	
	public void EnviaMailTLSFicheroAdjunto(String destinatario, String asunto, String user, String busqueda) {
		
		Properties propiedades = new Properties();
		
		propiedades.put("mail.smtp.host", "smtp.gmail.com");
		
		propiedades.put("mail.smtp.port", "587");
		
		propiedades.put("mail.smtp.auth", "true");
		
		propiedades.put("mail.smtp.starttls.enable", "true");
		
		Session session = Session.getInstance(propiedades, new javax.mail.Authenticator() {
			
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(usuario, password);
			}
			
		});
		
		try {
			
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(usuario));  //!!!!
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
			
			message.setSubject(asunto);
			
			//Creo el contenido del mensaje
			
			MimeBodyPart contenido = new MimeBodyPart();
			
			contenido.setText(usuario + ", \n" + "Adjuntamos archivo con la informaci�n solicitada \n" + "El equipo de Retweeching");
			
			//Inserto del archivo que quiero adjuntar (el nombre del archivo es usuario_busqueda.csv)
			
			MimeBodyPart archivoAdjunto = new MimeBodyPart();
			
			FileDataSource ruta = new FileDataSource("C:/Users/Usuario/eclipse-workspace-ProyectoTwitter/Retweetching/Archivos/" + user + "_" + busqueda + ".csv");
			
			DataHandler gestorDatos = new DataHandler(ruta);
			
			archivoAdjunto.setDataHandler(gestorDatos);
			
			//Le doy nombre al archivo adjunto
			
			archivoAdjunto.setFileName(user + "_" + busqueda + ".csv");
			
			//A�ado al mensaje tanto el contendio como el archivo adjunto utilizando MimeMultipart
			
			MimeMultipart multiPart = new MimeMultipart();
			
			multiPart.addBodyPart(contenido);
			
			multiPart.addBodyPart(archivoAdjunto);
			
			message.setContent(multiPart);
			
			//Ejecuto el env�o
			
			Transport.send(message);
			
			cmd.ventanaConfirmaci�n("Confirmaci�n", "E-mail enviado", "Consulte su bandeja de entrada");
			
			System.out.println("Enviado");
			
		}catch(MessagingException e) {
			
			cmd.ventanaError("Error", "Error al enviar el E-mail", "No se ha podido enviar el archivo solicitado." + "\nInt�ntalo en unos minutos" + "\nEl equipo de Retweeching");
			
			System.out.println("Error al enviar el mensaje");
			
			e.printStackTrace();
			
		}

	}
	
	//---------------------------------------------------> M�todo que env�a un mail con gmail utilizando protocolo TLS <--------------------------------------------------------------
	
	public void enviaMailSSL(String destinatario, String asunto, String mensaje) {
		
		Properties propiedades = new Properties();
		
		propiedades.put("mail.smtp.host", "smtp.gmail.com");
		
		propiedades.put("mail.smtp.port", "465");
		
		propiedades.put("mail.smtp.auth", "true");
		
		propiedades.put("mail.smtp.socketFactory.port", "465");
		
		propiedades.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
		Session session = Session.getInstance(propiedades, new javax.mail.Authenticator() {
			
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(usuario, password);
			}
			
		});
		
		try {
			
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(usuario));  //!!!!
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
			
			message.setSubject(asunto);
			
			message.setText(mensaje);
			
			Transport.send(message);
			
			cmd.ventanaConfirmaci�n("Confirmaci�n", "E-mail enviado", "El c�digo de recuperaci�n se ha enviado a tu correo electr�nico");
			
			System.out.println("Enviado");
			
		}catch(MessagingException e) {
			
			cmd.ventanaError("Error", "Error al enviar el E-mail", "No se ha podido enviar el c�digo de recuperaci�n." + "\nInt�ntalo en unos minutos");
			
			System.out.println("Error al enviar el mensaje");
			
			e.printStackTrace();
			
		}
		
	}
	
	//---------------------------------------------------> M�todo que env�a un mail con archivo adjunto utilizando gmail utilizando protocolo SSL <-----------------------------------
	
	public void EnviaMailSSLFicheroAdjunto(String destinatario, String asunto, String user, String busqueda) {		
	
	Properties propiedades = new Properties();
	
	propiedades.put("mail.smtp.host", "smtp.gmail.com");
	
	propiedades.put("mail.smtp.port", "465");
	
	propiedades.put("mail.smtp.auth", "true");
	
	propiedades.put("mail.smtp.socketFactory.port", "465");
	
	propiedades.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	
		Session session = Session.getInstance(propiedades, new javax.mail.Authenticator() {
			
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(usuario, password);
			}
			
		});
		
		try {
			
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(usuario));  //!!!!
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
			
			message.setSubject(asunto);
			
			MimeBodyPart contenido = new MimeBodyPart();
			
			contenido.setText(usuario + ", \n" + "Adjuntamos archivo con la informaci�n solicitada \n" + "El equipo de Retweeching");
			
			MimeBodyPart archivoAdjunto = new MimeBodyPart();
			
			FileDataSource ruta = new FileDataSource("C:/Users/Usuario/eclipse-workspace-ProyectoTwitter/Retweetching/Archivos/" + user + "_" + busqueda + ".csv");
			
			DataHandler gestorDatos = new DataHandler(ruta);
			
			archivoAdjunto.setDataHandler(gestorDatos);
			
			MimeMultipart multiPart = new MimeMultipart();
			
			multiPart.addBodyPart(contenido);
			
			multiPart.addBodyPart(archivoAdjunto);
			
			message.setContent(multiPart);
			
			Transport.send(message);
			
			cmd.ventanaConfirmaci�n("Confirmaci�n", "E-mail enviado", "Consulte su bandeja de entrada");
			
			System.out.println("Enviado");
			
		}catch(MessagingException e) {
			
			cmd.ventanaError("Error", "Error al enviar el E-mail", "No se ha podido enviar el c�digo de recuperaci�n." + "\nInt�ntalo en unos minutos");
			
			System.out.println("Error al enviar el mensaje");
			
			e.printStackTrace();
			
		}

	}
		
}
	
	
	
	


