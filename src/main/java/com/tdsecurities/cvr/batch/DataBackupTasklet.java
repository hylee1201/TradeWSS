package com.tdsecurities.cvr.batch;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import com.tdsecurities.cvr.batch.service.IBatchService;
 
/**
 * Tasklet for downloading files
 * @author wangp4
 *
 */
public class DataBackupTasklet implements Tasklet, InitializingBean {
 
    private String valuationDateSTR;
    private IBatchService batchService;
    private static final Log logger = LogFactory.getLog(DataBackupTasklet.class);
 
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    	Date date=batchService.getValuationDate(valuationDateSTR);
    	batchService.postProcessData(date);
    	logger.info("Data backuped successfully.");
        return RepeatStatus.FINISHED;
    }
 
    public void afterPropertiesSet() throws Exception {
        //Assert.notNull(directory, "directory must be set");
    }

}
