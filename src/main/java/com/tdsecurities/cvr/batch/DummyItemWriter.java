/**
 * 
 */
package com.tdsecurities.cvr.batch;
import java.util.List;

import org.springframework.batch.item.ItemWriter;


/**
 * @author wangp4
 *
 */
public class DummyItemWriter implements ItemWriter<Object> {
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends Object> items) throws Exception {
	}

}
