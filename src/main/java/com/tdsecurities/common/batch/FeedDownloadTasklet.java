package com.tdsecurities.common.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import com.tdsecurities.common.service.IFileService;
import com.tdsecurities.cvr.batch.service.BatchService;
import com.tdsecurities.cvr.util.ExceptionHandler;

/**
 * Tasklet for downloading files
 * 
 * @author wangp4
 *
 */
public class FeedDownloadTasklet implements Tasklet, InitializingBean {

	private IFileService filedownloadService;
	private String remotePath;
	private String localPath;
	private String filename;

	private static final Logger logger = Logger.getLogger(BatchService.class);
	private ExceptionHandler handler;

	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution()
				.getExecutionContext();

		try {
			logger.info("Attempting to download file: " + filename);
			logger.info("From path: " + remotePath);
			logger.info("To local path: " + localPath);
			filedownloadService.downloadFile(remotePath, filename, localPath);
		} catch (Exception e) {
			logger.error("Download failed with exception:", e);
			jobExecutionContext.put("errorMessage", "No Feed found.");
			jobExecutionContext.put("success", false);
			handler.handleCriticalException(e);
		}
		jobExecutionContext.put("success", true);
		return RepeatStatus.FINISHED;
	}

	public void afterPropertiesSet() throws Exception {
		// Assert.notNull(directory, "directory must be set");
	}

	public void setFiledownloadService(IFileService filedownloadService) {
		this.filedownloadService = filedownloadService;
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

	public IFileService getFiledownloadService() {
		return filedownloadService;
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

	public ExceptionHandler getHandler() {
		return handler;
	}

	public void setHandler(ExceptionHandler handler) {
		this.handler = handler;
	}

}
