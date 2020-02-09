package com.tdsecurities.cvr.batch;

import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import com.tdsecurities.cvr.batch.service.IBatchService;
import com.tdsecurities.cvr.util.ExceptionHandler;
import com.tdsecurities.cvr.util.DataHolder;

/**
 * Tasklet for downloading files
 * 
 * @author wangp4
 *
 */
public class TransferRecordTasklet implements Tasklet, InitializingBean {

	protected Date valuationDate;
	protected boolean success;
	protected String groupType;
	protected IBatchService batchService;
	protected DataHolder warningDataHolder;
	protected String batchJobName;
	
	private static final Logger logger = Logger.getLogger(TransferRecordTasklet.class);
	private ExceptionHandler handler;

	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		boolean warning = false;
		StringBuilder stringBuilder = new StringBuilder();
		ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution()
				.getExecutionContext();
		String desc = (String) jobExecutionContext.get("errorMessage");
		if (desc != null)
			stringBuilder.append(desc);
		try {
			batchService.createTransactionRecord(groupType, valuationDate, success, warning, stringBuilder.toString());
		} catch (Exception e) {
			jobExecutionContext.put("errorMessage", "Creating transaction record failed.");
			jobExecutionContext.put("success", false);
			handler.handleCriticalException(e);
		}
		jobExecutionContext.put("success", true);
		logger.info("transfer record created successfully.");
		return RepeatStatus.FINISHED;
	}

	public void afterPropertiesSet() throws Exception {
		// Assert.notNull(directory, "directory must be set");
	}

	public void setValuationDate(Date valuationDate) {
		this.valuationDate = valuationDate;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setBatchService(IBatchService batchService) {
		this.batchService = batchService;
	}

	public void setWarningDataHolder(DataHolder warningDataHolder) {
		this.warningDataHolder = warningDataHolder;
	}

	public String getBatchJobName() {
		return batchJobName;
	}

	public void setBatchJobName(String batchJobName) {
		this.batchJobName = batchJobName;
	}

	public ExceptionHandler getHandler() {
		return handler;
	}

	public void setHandler(ExceptionHandler handler) {
		this.handler = handler;
	}

}
