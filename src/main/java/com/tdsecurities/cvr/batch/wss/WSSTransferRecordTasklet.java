package com.tdsecurities.cvr.batch.wss;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import com.tdsecurities.cvr.batch.TransferRecordTasklet;
import com.tdsecurities.cvr.model.BatchJobVO;
import com.tdsecurities.cvr.util.ExceptionHandler;
 
/**
 * Tasklet for downloading files
 * @author wangp4
 *
 */
public class WSSTransferRecordTasklet extends TransferRecordTasklet implements Tasklet, InitializingBean {
 
    private static final Logger logger = Logger.getLogger(TransferRecordTasklet.class);
    
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    	boolean warning=false;
    	StringBuilder stringBuilder=new StringBuilder();
		ExecutionContext jobExecutionContext = chunkContext
				.getStepContext().getStepExecution().getJobExecution()
				.getExecutionContext();
		String desc=(String) jobExecutionContext.get("errorMessage");
		if(desc!=null) stringBuilder.append(desc);
    	List<String> ids=null;
    	if(!warningDataHolder.get().isEmpty()){
    		ids=batchService.exsistInDB(valuationDate, warningDataHolder.get());
    	}
    	if(ids!=null && ids.size()>0){
    		warning=true;
    		stringBuilder.append("Skipped ID: ");
    		for(String id:ids){
    			stringBuilder.append(id);
    			stringBuilder.append(";");
        		logger.warn("Feed with trade number: "+ id +" skipped due to Data issue, Please verify.");
    		}
    	}
    	String successFlag=null;
    	if(success){
        	if(!warning){
        		successFlag="SUCCESS";
        	}else{
        		successFlag="ERROR";
        	}
    		
    	}else{
    		successFlag="FAIL";
    	}
    	batchService.updateStatus(new BatchJobVO(batchJobName,successFlag));
    	batchService.createTransactionRecord(groupType, valuationDate, success,warning, stringBuilder.toString());
    	logger.info("transfer record created successfully.");
        return RepeatStatus.FINISHED;
    }
 
}
