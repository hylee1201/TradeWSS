package com.tdsecurities.cvr.batch.wss;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.tdsecurities.cvr.batch.constants.Consts;
import com.tdsecurities.cvr.util.ExceptionHandler;

public class WSSBatch {
	private static final Logger logger = Logger.getLogger(WSSBatch.class);
	public static void main(String[] args) {
		if (args == null) {
			logger.error("Please enter a batch file name with a full path.");
			return;
		}
		
		logger.info("Starting running VS Trade WSS batch job...");
		String[] springConfig = { "spring/cvr/batch/jobs/job-VSTradeWSS.xml" };
		ApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(springConfig);
		} catch (Exception e){
			logger.error("Failed to get Spring job XML.", e);
			System.exit(1);
		}
		
		try {
			
			JobLauncher jobLauncher = (JobLauncher) context.getBean(Consts.JOB_LAUNCHER);
			Job job = (Job) context.getBean(Consts.JOB_TRADE_WSS);

			//This is used to update status and notified as a DB key.
			JobParameters param = new JobParametersBuilder().addString("batchFile", args[0]).toJobParameters();

			JobExecution execution = jobLauncher.run(job, param);
			logger.info("Trades WSS batchjob has finished with exit status: " + execution.getStatus());
			
		} catch (Exception e) {
			logger.error("Unhandled exception: ", e);
			ExceptionHandler handler = (ExceptionHandler) context.getBean("exceptionHandler");
			handler.handleCriticalException(e);
		}
	}
}
