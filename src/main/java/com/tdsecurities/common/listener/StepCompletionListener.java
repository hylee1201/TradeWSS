package com.tdsecurities.common.listener;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepCompletionListener implements StepExecutionListener {
	private static final Logger logger = Logger.getLogger(StepCompletionListener.class);
	private int taskNumber = 0;
	private String task = "";

	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.info("----------------------------------------------------------------");
		if (taskNumber > 0) {
			logger.info("START OF TASK " + taskNumber + ":		" + task);
		} else {
			logger.info("START OF TASK:		" + task);
			if (taskNumber == 0) {
				logger.error("Task number is not set or set to 0 (invalid).");
			}
		}
		logger.info(""); // Blank line
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.info(""); // Blank line
		if (taskNumber > 0) {
			logger.info("END OF TASK " + taskNumber + ":		" + task);
		} else {
			logger.info("END OF TASK:		" + task);
		}
		logger.info("----------------------------------------------------------------");
		return null;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public int getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

}
