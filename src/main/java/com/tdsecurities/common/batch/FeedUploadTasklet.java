package com.tdsecurities.common.batch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import com.tdsecurities.common.service.IFileService;
import com.tdsecurities.cvr.batch.service.BatchService;

/**
 * Tasklet for downloading files
 * 
 * @author wangp4
 *
 */

public class FeedUploadTasklet implements Tasklet, InitializingBean {

	private IFileService fileUploadService;
	private String remotePath;
	private String localPath;
	private String filename;

	private static final Logger logger = Logger.getLogger(FeedUploadTasklet.class);

	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info("----------------------------------------------------------------");
		logger.info("START OF TASK: UPLOADING");
		logger.info(""); // Blank line
		
		boolean result = false;
		try {
			logger.debug("Uploading file: " + filename + " at remote path: " + remotePath + "to local path" + localPath);
			result = fileUploadService.uploadFile(remotePath, filename, localPath);
		} catch (Exception e) {
			logger.error("Feed upload exception.", e);
		}

		ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution()
				.getExecutionContext();
		jobExecutionContext.put("success", result);
		
		if (result) {
			logger.info("Feed upload successfull.");
		} else {
			String errorMessage = "Feed upload unsucessful.";
			logger.error(errorMessage);
			jobExecutionContext.put("errorMessage", errorMessage);
		}
		
		logger.info(""); // Blank line
		logger.info("FINISHED TASK: UPLOADING");
		return RepeatStatus.FINISHED;
	}

	public void afterPropertiesSet() throws Exception {
		// Assert.notNull(directory, "directory must be set");
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public void setFileUploadService(IFileService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}

	public IFileService getFileUploadService() {
		return fileUploadService;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public String getLocalPath() {
		return localPath;
	}

	public String getFilename() {
		return filename;
	}
}
