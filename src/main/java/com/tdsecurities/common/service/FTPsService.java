package com.tdsecurities.common.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.ssl.SSLFTPClient;
import com.tdsecurities.cvr.util.BatchUtils;

public class FTPsService extends IFileService {

	private boolean implicit;
	private static final Logger logger = Logger.getLogger(FTPsService.class);

	private SSLFTPClient login() throws FTPException, IOException {
		logger.info("Connecting and logging in to FTPsService serverHost: " + serverHost + " ...");
		SSLFTPClient ftp = new SSLFTPClient();

		try {
			ftp.setValidateServer(false);
		} catch (FTPException e) {
			logger.error("FTP Validation exception", e);
			throw e;
		}

		try {
			ftp.setRemoteHost(serverHost);
			ftp.setRemotePort(port);
			ftp.setImplicitFTPS(implicit);
			ftp.setConfigFlags(8);
		} catch (FTPException e) {
			logger.error("Client already connected to the server.", e);
			throw e;
		} catch (IOException e) {
			logger.error("RemoteHost IO exception.", e);
			throw e;
		}

		try {
			ftp.connect();
		} catch (Exception e) {
			logger.error("Connection failed using serverHost: " + serverHost + ", and port: " + port + ".", e);
			throw e;
		}

		try {
			ftp.login(userName, password);
		} catch (Exception e) {
			logger.error("Login exception.", e);
			throw e;
		}

		ftp.setConnectMode(FTPConnectMode.PASV);
		ftp.setType(FTPTransferType.ASCII);

		logger.info("Connected and logged in to FTPs server successfully.");
		return ftp;
	}

	/**
	 * Delete file from FTP if the
	 * 
	 * @param fileNameFormat
	 * @param host
	 * @param user
	 * @param pswd
	 * @param fileNameFormat
	 * @param daystoKeep
	 * @param daystoKeep
	 */
	@Override
	public void cleanupData(String fileNameFormat, int daystoKeep, String remotePath) {
		logger.info("Starting data cleanup process...");
		SSLFTPClient ftp = null;

		try {
			ftp = login();
		} catch (Exception e) {
			logger.error("Connection/login to FTP failed.");
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
	public boolean downloadFile(String path, String fileName, String localPath) {
		logger.info("Starting download process...");
		boolean result = false;
		SSLFTPClient ftp = null;

		try {
			ftp = login();
		} catch (Exception e) {
			logger.error("Connection/login to FTP failed.");
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
	public boolean uploadFile(String path, String fileName, String localPath) {
		logger.info("Starting upload process...");
		boolean result = false;
		SSLFTPClient ftp = null;

		try {
			ftp = login();
		} catch (Exception e) {
			logger.error("Connection/login to FTP failed.");
		}

		if (null != ftp) {
			try {
				ftp.put(localPath + fileName, path + fileName);
				result = true;
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
		
		logger.info("Upload process complete.");
		return result;
	}

	public boolean isImplicit() {
		return implicit;
	}

	public void setImplicit(boolean implicit) {
		this.implicit = implicit;
	}

}
