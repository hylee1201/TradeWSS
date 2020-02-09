package com.tdsecurities.common.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import com.tdsecurities.cvr.util.BatchUtils;
import com.tdsecurities.cvr.util.ExceptionHandler;

/**
 * Tasklet to cleanup all logs
 * 
 * @author storoa4
 *
 */

public abstract class CleanupTasklet implements Tasklet, InitializingBean {
	private int batchLogDaysToKeep = 0;
	private String logPath;
	private static final Logger logger = Logger.getLogger(CleanupTasklet.class);
	protected ExceptionHandler handler;
	

	public final RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info("Cleaning up logPath: " + logPath + ", for files exceeding: " + batchLogDaysToKeep + " days.");

		int logsDeletedAmt = BatchUtils.purgeLogs(batchLogDaysToKeep, logPath);
		logger.info(logsDeletedAmt + " logs deleted.");
		cleanup();
		ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution()
				.getExecutionContext();
		jobExecutionContext.put("success", true);
		
		return RepeatStatus.FINISHED;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		// Assert.notNull(directory, "directory must be set");

	}
	
	// Override this cleanup for any additional cleanup
	public void cleanup(){}

	public int getBatchLogDaysToKeep() {
		return batchLogDaysToKeep;
	}

	public void setBatchLogDaysToKeep(int batchLogDaysToKeep) {
		this.batchLogDaysToKeep = batchLogDaysToKeep;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	
	public ExceptionHandler getHandler() {
		return handler;
	}

	public void setHandler(ExceptionHandler handler) {
		this.handler = handler;
	}
	
}
