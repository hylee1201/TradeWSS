package com.tdsecurities.cvr.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.tdsecurities.common.batch.FeedUploadTasklet;

public class BatchUtils {
	private static final Logger logger = Logger.getLogger(BatchUtils.class);
	
	public static Date parseDateStr(String dateStr) throws ParseException {
		Date dt = null;
		if ((dateStr != null) && (!dateStr.trim().equals(""))) {
			dateStr = dateStr.trim();

			// attempt to accomodate various date styles
			SimpleDateFormat dateFrm = new SimpleDateFormat("MM-dd-yyyy");
			String delim = "";
			if (dateStr.indexOf('/') != -1) {
				delim = "/";
			}
			else if (dateStr.indexOf('\\') != -1) {
				delim = "\\";
			}
			else if (dateStr.indexOf('-') != -1) {
				delim = "-";
			}
			else if (dateStr.indexOf('.') != -1) {
				delim = ".";
			}

			if (dateStr.length() == 6) {
				dateFrm = new SimpleDateFormat("yyMMdd");
			}
			else if (dateStr.length() == 8) {
				if (delim.equals("")) {
					dateFrm = new SimpleDateFormat("yyyyMMdd");
				}
				else {
					dateFrm = new SimpleDateFormat("MM"+delim+"dd"+delim+"yy");
				}
			}
			else if (dateStr.length() == 10) {
				dateFrm = new SimpleDateFormat("MM"+delim+"dd"+delim+"yyyy");
			}
			dateFrm.setLenient(false);

			dt = dateFrm.parse(dateStr);
		}
		return dt;
	}
	
    public static String formatDateToStr(Date date, String pattern) {
        // Format the current time.
        String dateString;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        dateString = formatter.format(date);
        
        return dateString;
    }
    
	public static int purgeLogs(int maxBackupLogDays, String logPath) {
		int numberOfPurgedLogs = 0;
		File file = new File(logPath);
		
		if (file.isDirectory()) {
            for (File f : file.listFiles()) {
            	long diff = (new Date()).getTime() - f.lastModified();
            	if (diff > maxBackupLogDays * 24 * 60 * 60 * 1000L) {
            		logger.info("Deleting :" + f.getName());
            	    f.delete();
            	    numberOfPurgedLogs++;
            	}
            }
		}
		return numberOfPurgedLogs;
	}
}
