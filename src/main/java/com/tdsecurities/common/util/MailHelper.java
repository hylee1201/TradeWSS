package com.tdsecurities.common.util;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Iterator;
import com.tdsecurities.common.util.ByteArrayDataSource;

/**
 * @author Florin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MailHelper {
	private static final Logger _logger = Logger.getLogger(MailHelper.class);

	
	/**
	 * 
	 * @param resources
	 * @param fromAddress
	 * @param toAddress
	 * @param ccAddress
	 * @param subject
	 * @param bodyText
	 * @return
	 * @throws MessagingException
	 */
	public static synchronized boolean sendEmailNow(String host, String fromAddress,
			String toAddress, String subject, String bodyText,
			String attachmentName, byte[] attachment) throws MessagingException {
		String _logger_method="sendEmailNow";
		if (_logger.isDebugEnabled()) {
			_logger.debug("> "+_logger_method);
		}

		boolean sentSuccessfully = true; 
		
		try{
			// Get system properties
			Properties props = System.getProperties();
			
			String auth = "false";
	
			//System.out.print(longFilename);
			if (_logger.isDebugEnabled()) {
				_logger.debug("*** Got the following properties");
				_logger.debug("*** mail.smtp.host = "+host);
				_logger.debug("*** mail.smtp.auth = "+auth);
				//_logger.debug("*** mail.smtp.username = "+username);
				//_logger.debug("*** mail.smtp.password = "+password);
				_logger.debug("*** fromAddress = "+fromAddress);
				_logger.debug("*** toAddress = "+toAddress);
				_logger.debug("*** mail.subject = "+subject);
				_logger.debug("*** mail.body = "+bodyText);
			}
	
			// Setup mail server
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", auth);
	
			// Get session
//			Session session = Session.getInstance(props, new SmtpAuthenticator(username,password));
			Session session = Session.getDefaultInstance(props, null);
			if (_logger.isDebugEnabled()) {
				_logger.debug("*** Got the mail session");
			}
	
			// Define message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			StringTokenizer st = new StringTokenizer(toAddress, ";");
			while(st.hasMoreElements()){
				String tokenAddr = st.nextToken();
				message.addRecipient(Message.RecipientType.TO,new InternetAddress(tokenAddr));
			}
			//set the subject
			message.setSubject(subject, "utf-8");
		
			// Create the message part 
			MimeBodyPart messageBodyPart = new MimeBodyPart();
		
			// Fill the message
			messageBodyPart.setText(bodyText, "utf-8");
		
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
		
			// Part two is attachment
			if(attachment!=null){
				messageBodyPart = new MimeBodyPart();
				javax.activation.DataSource source = new ByteArrayDataSource(attachment, "application/pdf");
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(attachmentName);
				multipart.addBodyPart(messageBodyPart);
			}
		
			// Put parts in message
			message.setContent(multipart);
	
			if (_logger.isDebugEnabled()) {
				_logger.debug("*** About to Transport.send(message) ");
			}
			// Send the message
			Transport.send(message);
		}
		catch(Exception e){
			_logger.error("Exception while sending e-mail: " + e, e);
			sentSuccessfully = false;
			throw new RuntimeException(e);
		}
		
		if (_logger.isDebugEnabled()) {
			if(sentSuccessfully)
				_logger.debug("*** E-mail was sent successfully ***");
			else
				_logger.debug("*** Failed to send the e-mail ***");
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("< "+_logger_method);
		}
		return sentSuccessfully;
	}	
	
	
	public static synchronized boolean sendEmailWithMultiAttach(String host, String fromAddress,
			String toAddress,  String subject, List<String> bodyTexts,
			Map<String, byte[]> attachments, String ccAddress, String bccAddress) throws MessagingException {
		String _logger_method="sendEmailNow";
		if (_logger.isDebugEnabled()) {
			_logger.debug("> "+_logger_method);
		}

		boolean sentSuccessfully = true; 
		
		try{
			// Get system properties
			Properties props = System.getProperties();
			
			String auth = "false";
	
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("*** Got the following properties");
				_logger.debug("*** mail.smtp.host = "+host);
				_logger.debug("*** mail.smtp.auth = "+auth);
				_logger.debug("*** fromAddress = "+fromAddress);
				_logger.debug("*** toAddress = "+toAddress);
				_logger.debug("*** mail.subject = "+subject);
				_logger.debug("*** ccAddress = "+ccAddress);
				_logger.debug("*** bccAddress = "+bccAddress);
			}
	
			java.security.Security
			.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			// Setup mail server
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", auth);
			props.put("mail.smtp.starttls.enable", "true");  


			// Get session

			Session session = Session.getDefaultInstance(props, null);
			if (_logger.isDebugEnabled()) {
				_logger.debug("*** Got the mail session");
			}
	
			// Define message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			StringTokenizer st = new StringTokenizer(toAddress, ";");
			while(st.hasMoreElements()){
				String tokenAddr = st.nextToken();
				message.addRecipient(Message.RecipientType.TO,new InternetAddress(tokenAddr));
			}
			//set the subject
			message.setSubject(subject, "utf-8");
		
			//add cc
			if(!StringUtils.isEmpty(ccAddress)){
				StringTokenizer ccSt = new StringTokenizer(ccAddress, ";");
				while (ccSt.hasMoreElements()){
					String ccTokenAddr = ccSt.nextToken();
					if (ccTokenAddr != null && !ccTokenAddr.equals("")) {
						message.addRecipient(Message.RecipientType.CC,new InternetAddress(ccTokenAddr));
					}
				}	
			}
			
			//add bcc
			if(!StringUtils.isEmpty(bccAddress)){
				message.addRecipient(Message.RecipientType.BCC,new InternetAddress(bccAddress));
			}
		
			Multipart multipart = new MimeMultipart();
			StringBuffer msgBody = new StringBuffer();
			
			if(bodyTexts!=null){
				msgBody.append(bodyTexts.get(0));
				for(int i=1; i<bodyTexts.size();i++){
					msgBody.append("\n\n");
					msgBody.append("_______________________________________________________________");
					msgBody.append("\n\n");
					msgBody.append(bodyTexts.get(i));
				}
			}
			//System.out.println(msgBody.toString());
			// Create the message part 
			MimeBodyPart bodyPart = new MimeBodyPart();

			// Fill the message
			bodyPart.setText(msgBody.toString(), "utf-8");
			multipart.addBodyPart(bodyPart);
			
			if(attachments!=null){
				Set<String> names = attachments.keySet();
				for(Iterator<String> itr=names.iterator(); itr.hasNext();){
					String fileName = itr.next();
					BodyPart messageBodyPart = new MimeBodyPart();
					DataSource source = new ByteArrayDataSource(attachments.get(fileName), getContentType(fileName));
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(fileName);
					multipart.addBodyPart(messageBodyPart);
				}
			}
		
			// Put parts in message
			message.setContent(multipart);
	
			if (_logger.isDebugEnabled()) {
				_logger.debug("*** About to Transport.send(message) ");
			}
			// Send the message
			Transport.send(message);
		}
		catch(Exception e){
			_logger.error("Exception while sending e-mail: " + e, e);
			sentSuccessfully = false;
		}
		
		if (_logger.isDebugEnabled()) {
			if(sentSuccessfully)
				_logger.debug("*** E-mail was sent successfully ***");
			else
				_logger.debug("*** Failed to send the e-mail ***");
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("< "+_logger_method);
		}
		return sentSuccessfully;
	}	
	
	public static String getContentType(String name){
		int extIndex = name.indexOf(".");
		String ext=null;
		String contentType = null;
		if(extIndex>0){
			ext = name.substring(extIndex+1);
		}
		
		if("pdf".equals(ext)){
			contentType = "application/pdf";
		}else if("xls".equals(ext)){
			contentType = "application/vnd.ms-excel";
		}else if("doc".equals(ext)){
			contentType = "application/msword"; 
		}else if("xml".equals(ext)){
			contentType = "application/xml";
		}else if("csv".equals(ext)){
			contentType = "text/csv";
		}else if("txt".equals(ext)){
			contentType = "text/plain"; 
		}
		
		return contentType;
		
	}
}
