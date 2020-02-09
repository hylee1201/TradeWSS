package com.tdsecurities.common.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import com.tdsecurities.cvr.model.Report;

public class CustomItemProcessor implements ItemProcessor<Report, Report> {

	private static final Logger logger = Logger.getLogger(ItemProcessor.class);
	@Override
	public Report process(Report item) throws Exception {
		
		logger.info("Processing..." + item);
		return item;
	}

}