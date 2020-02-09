package com.tdsecurities.common.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.tdsecurities.cvr.batch.service.IBatchService;
import com.tdsecurities.cvr.model.BatchJobVO;

/**
 * Tasklet for clean up feed files
 * 
 * @author wangp4
 *
 */
public class EmailerTasklet implements Tasklet, InitializingBean {

	private MailSender mailSender;
	private SimpleMailMessage preConfiguredMessage;
	private static final Logger logger = Logger.getLogger(EmailerTasklet.class);
	private IBatchService batchService;
	private String batchJobName;

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		logger.info("job batch file: " + batchJobName);
		
		try {
			mailSender.send(preConfiguredMessage);
			logger.info("email sent successfully.");
			batchService.updateNotified(new BatchJobVO(batchJobName, 1));
		} catch (Exception e) {
			logger.error("email sent failed.", e);
			batchService.updateNotified(new BatchJobVO(batchJobName, 0));
		}
		return RepeatStatus.FINISHED;
	}

	public void afterPropertiesSet() throws Exception {
		// Assert.notNull(directory, "directory must be set");
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setPreConfiguredMessage(SimpleMailMessage preConfiguredMessage) {
		this.preConfiguredMessage = preConfiguredMessage;
	}

	public IBatchService getBatchService() {
		return batchService;
	}

	public void setBatchService(IBatchService batchService) {
		this.batchService = batchService;
	}

	public String getBatchJobName() {
		return batchJobName;
	}

	public void setBatchJobName(String batchJobName) {
		this.batchJobName = batchJobName;
	}
}
