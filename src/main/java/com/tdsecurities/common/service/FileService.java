package com.tdsecurities.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.tdsecurities.cvr.util.BatchUtils;

public class FileService extends IFileService {
	private static final Logger logger = Logger.getLogger(FileService.class);

	@Override
	public void cleanupData(String fileNameFormat, int daystoKeep, String remotePath) {
		try {
			File folder = new File("remotePath");
			File[] listOfFiles = folder.listFiles();
			if (listOfFiles != null && listOfFiles.length > 0) {
				for (File file : listOfFiles) {
					String name = file.getName();
					Pattern pattern = Pattern.compile(fileNameFormat);
					Matcher m = pattern.matcher(name);
					if (m.matches()) {
						String valuationDateStr = m.group(1);
						Date fileDate = BatchUtils.parseDateStr(valuationDateStr);
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DAY_OF_YEAR, -daystoKeep);
						if (cal.getTime().after(fileDate)) {
							logger.info("File " + name + " was deleted since it is older than " + daystoKeep + " days.");
							file.delete();
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public boolean downloadFile(String path, String fileName, String localPath) {
		logger.info("Starting download process...");
		boolean result = false;
		InputStream inStream = null;
		OutputStream outStream = null;

		logger.info(
				"Attempting to download file: " + fileName + " from path: " + path + " to local path: " + localPath);
		File file1 = new File(getFileName(path, fileName));
		File file2 = new File(getFileName(localPath, fileName));

		try {
			inStream = new FileInputStream(file1);
			outStream = new FileOutputStream(file2); // for override file
														// content

			byte[] buffer = new byte[1024];

			int length;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			result = true;
			
		} catch (Exception e) {
			logger.error("File download unsuccesful: ", e);
		} finally {
			if (inStream != null)
				try {
					inStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
		}

		logger.info("Download process complete.");
		return result;
	}

	private String getFileName(String path, String fileName) {
		String name = null;
		if (path != null && path.endsWith("\\")) {
			name = path + fileName;
		} else {
			name = path + "\\" + fileName;
		}
		return name;
	}

	@Override
	public boolean uploadFile(String path, String fileName, String localPath) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
