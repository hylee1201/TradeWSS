package com.tdsecurities.cvr.model;

import java.util.Date;

/**
 * @author wangp4
 *
 */
public class BatchJobVO {

	protected int id;
	protected String batchFile;
	protected Date lastExecutedDate;
	protected String status;//valus: SUCCESS:ERROR:FAIL
	protected int notified;
	
	public BatchJobVO() {}
	
	public BatchJobVO(String batchFile, String status) {
		this.batchFile = batchFile;
		this.status = status;
	}
	
	public BatchJobVO(String batchFile, int notified) {
		this.batchFile = batchFile;
		this.notified = notified;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBatchFile() {
		return batchFile;
	}

	public void setBatchFile(String batchFile) {
		this.batchFile = batchFile;
	}

	public Date getLastExecutedDate() {
		return lastExecutedDate;
	}

	public void setLastExecutedDate(Date lastExecutedDate) {
		this.lastExecutedDate = lastExecutedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getNotified() {
		return notified;
	}

	public void setNotified(int notified) {
		this.notified = notified;
	}
}
