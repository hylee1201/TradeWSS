package com.tdsecurities.cvr.batch;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import com.tdsecurities.cvr.batch.service.TradeWssBatchService;
import com.tdsecurities.cvr.util.ExceptionHandler;

/**
 * Tasklet for clean up feed files
 * 
 * @author wangp4
 *
 */
public class PreTasklet implements Tasklet, InitializingBean {

	private String remotePath;
	private String fileNameFormat;
	private String valuationDateSTR;
	private TradeWssBatchService batchService;
	private static final Logger logger = Logger.getLogger(PreTasklet.class);
	private ExceptionHandler handler;

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		Date date = batchService.getValuationDate(valuationDateSTR);
		String fileName = null;
		String path = null;
		Double rate = null;
		try {
			fileName = batchService.getFileName(date, fileNameFormat);
			path = batchService.getFileName(date, remotePath);
			rate = batchService.getRateByValuationDateFromDB(date);
			ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
			jobExecutionContext.put("date", date);
			logger.info("Valuation Date: " + date);
			jobExecutionContext.put("rate", rate);
			logger.info("Exchange Rate on valuation day: " + rate);
			jobExecutionContext.putString("fileName", fileName);
			logger.info("File Name : " + fileName);
			jobExecutionContext.putString("path", path);
		} catch (Exception e) {
			if (null == date) {
				logger.error("Valuation Date not set. Please make sure the cpg deal data is loaded.");
			}
			logger.error("File name could not be set with exception: ", e);
			handler.handleCriticalException(e);
		}

		return RepeatStatus.FINISHED;
	}

	public void afterPropertiesSet() throws Exception {
		// Assert.notNull(directory, "directory must be set");
	}

	public void setFileNameFormat(String fileNameFormat) {
		this.fileNameFormat = fileNameFormat;
	}

	public void setValuationDateSTR(String valuationDateSTR) {
		this.valuationDateSTR = valuationDateSTR;
	}

	public void setBatchService(TradeWssBatchService batchService) {
		this.batchService = batchService;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	
	public ExceptionHandler getHandler() {
		return handler;
	}

	public void setHandler(ExceptionHandler handler) {
		this.handler = handler;
	}
}
