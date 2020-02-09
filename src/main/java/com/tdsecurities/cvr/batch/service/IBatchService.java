package com.tdsecurities.cvr.batch.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.tdsecurities.cvr.model.BatchJobVO;

public interface IBatchService {

	/**
	 * read from DB config first, If not set get latest date from DB;
	 * @param dateSTR
	 * @return valuation date
	 */
	
	public void updateStatus(BatchJobVO bj);
	
	public void updateNotified(BatchJobVO bj);
	
	public Date getValuationDate(String dateSTR);

	public int createTransactionRecord(String groupType, Date valuationDate, boolean status,boolean warning,String message);

	/**
	 * remove batch flag and remove valuation Date
	 */
	public void updateBatchFlags(Map<String, String> paraMap);

	public String getFileName(Date date, String fileFormat);

	public Date getValuationDateFromDB();

	public void postProcessData(Date date);
	
	public List<String> exsistInDB(Date date,List<String> keys);

	public int[] processData(List<? extends Object> items,String Query,NamedParameterJdbcTemplate template);
	
	public int[] processData(List<? extends Object> items);

}