package com.tdsecurities.cvr.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * @author storoa4
 *
 */
public class ExceptionHandler implements InitializingBean {
	private MailSender mailSender;
	private SimpleMailMessage preConfiguredMessage;
	private static final Logger logger = Logger.getLogger(ExceptionHandler.class);
	private Stack<Exception> nonCritExcep;

	public void handleCriticalException(Exception e) {
		logger.info("Sending failure email for critical job exception...");
		try {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			preConfiguredMessage.setText("Critical failure with the following exception:\n" + exceptionAsString);
			mailSender.send(preConfiguredMessage);
		} catch (Exception e2) {
			logger.error("Sending failure message failed!", e2);
		}
		logger.info("Sending critical failure message complete, exiting with exit code 1.");
		System.exit(1);
	}

	public void handleException(Exception e) {
		logger.info("Adding exception to list of failures to be sent out.");
		nonCritExcep.push(e);
	}

	public Boolean sendNonCritExceptions(){
		if (nonCritExcep.empty()){
			return false;
		} else {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			for(int i = 0; i <nonCritExcep.size(); ++i){
				nonCritExcep.pop().printStackTrace(pw);
			}
			preConfiguredMessage.setText("The job finsihed with the following non critical exceptions:\n" + sw.toString());
			mailSender.send(preConfiguredMessage);
			return true;
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		nonCritExcep = new Stack<Exception>();
	}
	
	public MailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public SimpleMailMessage getPreConfiguredMessage() {
		return preConfiguredMessage;
	}

	public void setPreConfiguredMessage(SimpleMailMessage preConfiguredMessage) {
		this.preConfiguredMessage = preConfiguredMessage;
	}

}
