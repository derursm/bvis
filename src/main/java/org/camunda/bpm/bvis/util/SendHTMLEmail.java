package org.camunda.bpm.bvis.util;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
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
import javax.mail.util.ByteArrayDataSource;

public class SendHTMLEmail 
{

	public static void main(String subject, String content, String from, String to)
	{
		
		final String username 	= "bvis.car.rental@gmail.com";
		final String password 	= "workflownervt";
		
		String host 			= "smtp.gmail.com"; 
			
		Properties props 		= new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587"); 

		// Get the Session object.
		Session session 		= Session.getInstance(props, new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(username, password);
			}
		});

		try 
		{
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject(subject);
	        
			// Send the complete message parts
			message.setContent( content, "text/html");
			
			// Send message
			Transport.send(message);

			//System.out.println("Sent message successfully....");

		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void mainAtt(String subject, String content, String from, String to, ByteArrayOutputStream att, String filename)
	{
		
		final String username 	= "bvis.car.rental@gmail.com";
		final String password 	= "workflownervt";
		
		String host 			= "smtp.gmail.com"; 
			
		Properties props 		= new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587"); 

		// Get the Session object.
		Session session 		= Session.getInstance(props, new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(username, password);
			}
		});

		try 
		{
			// Create a default MimeMessage object.
			Message message 	= new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject(subject);

			// Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setContent(content, "text/html");

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);
	       	         
	         //Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         ByteArrayDataSource bds = new ByteArrayDataSource(att.toByteArray(), "application/pdf"); 
	         messageBodyPart.setDataHandler(new DataHandler(bds)); 
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart);
	         		
			// Send message
			Transport.send(message);

			//System.out.println("Sent message successfully....");

		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}