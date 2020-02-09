package com.tdsecurities.common.service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public abstract class IFileService {

	public static final Logger logger = Logger.getLogger(IFileService.class);
	protected String serverHost;
	protected String userName;
	protected String password;
	protected int port;

	public IFileService() {
		super();
	}

	public void cleanUpFile(String dir) {
		cleanUpFile(dir, 0); // deletes all files
	}

	public void cleanUpFile(String dir, int daysToKeep) {
		int filesDeletedCount = 0;
		try {
			File fileDir = new File(dir);
			
			if (!fileDir.exists()) {
				fileDir.mkdir();
			}
			
			File[] files = fileDir.listFiles();

			long todayMs = Calendar.getInstance().getTimeInMillis();
			long daysToKeepMs = daysToKeep * 24 * 60 * 60 * 100L;

			for (File f : files) {
				if (f.isFile() && f.exists() && (daysToKeepMs <= todayMs - f.lastModified())) {
					if (f.delete()) {
						logger.debug(f.getName() + " is deleted!");
						++filesDeletedCount;
					} else {
						logger.error("Delete operation failed in directory: " + dir + ", on file: " + f.getName()
								+ ". Missing permsisions, or file in use?");
					}
				}
			}
			logger.info(filesDeletedCount + " files deleted.");
			logger.info("Feeds in download folder cleaned up succesfully.");
		} catch (Exception e) {
			logger.error("Cleanup of files in directory: " + dir + " failed.", e);
		}
		
	}

	public void cleanupData(String fileNameFormat, int daystoKeep, String remotePath) {

	}

	public abstract boolean downloadFile(String path, String fileName, String localPath) throws Exception;

	public abstract boolean uploadFile(String path, String fileName, String localPath) throws Exception;

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {

		this.password = password; // if password is encoded: new String(Base64.decodeBase64(password));
	}

	public void setPort(int port) {
		this.port = port;
	}

}