package com.tdsecurities.common.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import com.tdsecurities.cvr.batch.service.IBatchService;
import com.tdsecurities.cvr.model.BatchJobVO;

public class FlowDecider implements JobExecutionDecider {
	IBatchService batchService;
	
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution,
			StepExecution stepExecution) {
		boolean result=(Boolean)jobExecution.getExecutionContext().get("success");
		if (!result) {
			if (batchService != null) {
				String batchFile = stepExecution.getJobParameters().getString("batchFile");
				batchService.updateStatus(new BatchJobVO(batchFile, "FAIL"));
			}
			return new FlowExecutionStatus("failure");
		} else {
			return new FlowExecutionStatus(jobExecution.getExitStatus()
					.toString());
		}
	}

	public IBatchService getBatchService() {
		return batchService;
	}

	public void setBatchService(IBatchService batchService) {
		this.batchService = batchService;
	}


}