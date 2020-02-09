package com.tdsecurities.common.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import com.tdsecurities.cvr.batch.service.BatchService;
import com.tdsecurities.cvr.util.ExceptionHandler;

public class ExceptionsEmailingTasklet implements Tasklet, InitializingBean {
	private static final Logger logger = Logger.getLogger(BatchService.class);
	private ExceptionHandler handler;
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		if(handler.sendNonCritExceptions()){
			logger.info("Job finished with non-critical exceptions, email sent.");
		} else {
			logger.info("Job finished error free.");
		}
		return RepeatStatus.FINISHED;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	public ExceptionHandler getHandler() {
		return handler;
	}

	public void setHandler(ExceptionHandler handler) {
		this.handler = handler;
	}
	
}
