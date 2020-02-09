package com.tdsecurities.cvr.batch;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tdsecurities.cvr.batch.wss.WSSBatch;

public class App {
	private static final Logger logger = Logger.getLogger(App.class);
	
	public static void main(String[] args) {

		String[] springConfig  = 
			{	
				"spring/cvr/batch/jobs/job-VSTradeMMB.xml" 
			};
		
		ApplicationContext context = 
				new ClassPathXmlApplicationContext(springConfig);
		
		Properties bean= (Properties) context.getBean("commonsConfigurationFactoryBean");
		logger.info(bean.get("VSTR_USER_NAME"));
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("wsstradeMMB");

		try {

			JobExecution execution = jobLauncher.run(job, new JobParameters());
			logger.info("Exit Status : " + execution.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Done");

	}
}
