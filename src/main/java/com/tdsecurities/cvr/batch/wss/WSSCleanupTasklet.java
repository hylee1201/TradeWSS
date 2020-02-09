package com.tdsecurities.cvr.batch.wss;

import org.apache.log4j.Logger;

import com.tdsecurities.common.batch.CleanupTasklet;
import com.tdsecurities.common.service.IFileService;
import com.tdsecurities.cvr.batch.service.TradeWssBatchService;
import com.tdsecurities.cvr.util.ExceptionHandler;

public class WSSCleanupTasklet extends CleanupTasklet {
	private String downloadfolder;
	private int daysKeep = 0;
	private String fileNameFormat;
	private String remotePath;
	private IFileService fileService;
	private static final Logger logger = Logger.getLogger(CleanupTasklet.class);
	private TradeWssBatchService batchService;

	// Called at the end of CleanupTasklet superclass execute()
	@Override
	public void cleanup() {
		fileService.cleanUpFile(downloadfolder);
		logger.info("Feeds in download folder cleaned up succesfully.");

		if (daysKeep > 0) {
			try {
				logger.info("Deleting files in remote path: " + remotePath + " that are older than " + daysKeep + " days.");
				logger.info("Based on pattern: " + fileNameFormat);
				fileService.cleanupData(fileNameFormat, daysKeep, remotePath);
				logger.info("Cleaned up successfully.");
			} catch (Exception e) {
				logger.error("Cleanup exception: ", e);
				handler.handleException(e);
			}
		} else {
			Exception e = new Exception("Retention days for WSS not set above 0.");
			logger.error(e);
			handler.handleException(e);
		}

		batchService.clearValuationDate();

	}

	public String getDownloadfolder() {
		return downloadfolder;
	}

	public void setDownloadfolder(String downloadfolder) {
		this.downloadfolder = downloadfolder;
	}

	public int getDaysKeep() {
		return daysKeep;
	}

	public void setDaysKeep(int daysKeep) {
		this.daysKeep = daysKeep;
	}

	public String getFileNameFormat() {
		return fileNameFormat;
	}

	public void setFileNameFormat(String fileNameFormat) {
		this.fileNameFormat = fileNameFormat;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public IFileService getFileService() {
		return fileService;
	}

	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	public TradeWssBatchService getBatchService() {
		return batchService;
	}

	public void setBatchService(TradeWssBatchService batchService) {
		this.batchService = batchService;
	}
	

}
