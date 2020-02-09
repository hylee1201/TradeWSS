/**
 * 
 */
package com.tdsecurities.common.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.tdsecurities.cvr.util.BatchUtils;

/**
 * @author storoa4
 *
 */

public class FTPService extends IFileService {
	private static final Logger logger = Logger.getLogger(FTPService.class);

	private FTPClient login() throws FTPException, IOException {
		logger.info("Attempting login to FTP: " + serverHost + " at port: " + port);
		FTPClient ftp = new FTPClient();

		try {
			ftp.setRemoteHost(serverHost);
			ftp.setRemotePort(port);
		} catch (FTPException e) {
			logger.error("Client already connected to the server.", e);
			throw e;
		} catch (IOException e) {
			logger.error("RemoteHost IO exception.", e);
			throw e;
		}

		try {
			ftp.connect();
			logger.info("Connection to FTP host established.");
		} catch (Exception e) {
			logger.error("Connection failed using serverHost: " + serverHost + ", and port: " + port + ".", e);
			throw e;
		}

		try {
			logger.info("Logging in, using username: " + userName);
			ftp.login(userName, password);
			logger.info("Login to FTP host successful.");
		} catch (Exception e) {
			logger.error("Login exception.", e);
			throw e;
		}

		ftp.setConnectMode(FTPConnectMode.PASV);
		ftp.setType(FTPTransferType.ASCII);
		return ftp;
	}

	@Override
	public void cleanupData(String fileNameFormat, int daystoKeep, String remotePath) {
		logger.info("Starting data cleanup process...");
		FTPClient ftp = null;

		try {
			ftp = login();
			logger.info("Connection and login to FTP: " + serverHost + " complete.");
		} catch (Exception e) {
			logger.error("Connection and login to FTP: " + serverHost + " failed.");
		}

		if (null != ftp) {
			try {
				String[] files = ftp.dir(remotePath);
				if (files != null && files.length > 0) {
					for (String name : files) {
						Pattern pattern = Pattern.compile(fileNameFormat);
						Matcher m = pattern.matcher(name);
						if (m.matches()) {
							String valuationDateStr = m.group(1);
							Date fileDate = BatchUtils.parseDateStr(valuationDateStr);
							Calendar cal = Calendar.getInstance();
							cal.add(Calendar.DAY_OF_YEAR, -daystoKeep);
							if (cal.getTime().after(fileDate)) {
								logger.info("File " + name + " was deleted since it is older than " + daystoKeep
										+ " days.");
								ftp.delete(name);
							}
						}
					}
				}

			} catch (IOException | FTPException e) {
				logger.error("FTP operation failed.", e);
			} catch (ParseException e) {
				logger.error("Valuation date parsing failure.", e);
			} finally {
				try {
					ftp.quit();
				} catch (IOException | FTPException e) {
					logger.error("FTP quit failed", e);
				}
			}
		}

		logger.info("Data cleanup complete.");
	}

	@Override
	public boolean downloadFile(String path, String fileName, String localPath) throws Exception {
		logger.info("Starting download process...");
		boolean result = false;
		FTPClient ftp = null;

		try {
			ftp = login();
			logger.info("Connection and login to FTP: " + serverHost + " complete.");
		} catch (Exception e) {
			logger.error("Connection and login to FTP: " + serverHost + " failed.");
		}

		if (null != ftp) {
			try {
				if (ftp.existsFile(path + fileName)) {
					ftp.get(localPath + fileName, path + fileName);
					result = true;
				}
			} catch (Exception e) {
				logger.error("Failed to get file", e);
			} finally {
				try {
					ftp.quit();
				} catch (IOException | FTPException e) {
					logger.error("FTP quit failed", e);
				}
			}
		}

		logger.info("Download process complete...");
		return result;
	}

	@Override
	public boolean uploadFile(String path, String fileName, String localPath) throws Exception {
		logger.info("Starting: upload by FTP.");
		boolean result = false;
		FTPClient ftp = null;
		
		try {
			ftp = login();
			logger.info("Connection and login to FTP: " + serverHost + " complete.");
		} catch (Exception e) {
			logger.error("Connection and login to FTP: " + serverHost + " failed.");
		}

		if (null != ftp) {
			try {
				logger.info("Uploading file from local location: " + localPath + fileName);
				logger.info("Uploading file to remote location:  " + path + fileName);
				ftp.put(localPath + fileName, path + fileName);
				result = true;
				logger.info("File upload completed successfully.");
			} catch (Exception e) {
				logger.error("File upload transfer failed.", e);
			}

			finally {
				try {
					ftp.quit();
				} catch (IOException | FTPException e) {
					logger.error("FTP quit failed", e);
				}
			}
		}

		logger.info("Finished: upload by FTP.");
		return result;
	}
}
