package com.tdsecurities.common.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
 
public class ApplicationMailer
{
    @Autowired
    private MailSender mailSender;
    
    private String region;
     
    @Autowired
    private SimpleMailMessage preConfiguredMessage;
    
    private static final Logger logger = Logger.getLogger(ApplicationMailer.class);
 
    /**
     * This method will compose and send the message
     * */
    public void sendMail(String to, String subject, String body)
    {
    	logger.info("Sending mail, subject: " + subject + ".");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(region+":"+subject);
        message.setText(body);
        mailSender.send(message);
    }
 
    /**
     * This method will send a pre-configured message
     * */
    public void sendPreConfiguredMail(String message,String title,String region)
    {
    	logger.info("Sending  preconfigured mail, title: " + title + ".");
        SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
        mailMessage.setText(message);
        mailMessage.setSubject(region+":"+title);
        mailSender.send(mailMessage);
    }

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
}