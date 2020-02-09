package com.tdsecurities.cvr.batch.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.tdsecurities.cvr.batch.constants.Consts;
import com.tdsecurities.cvr.model.BatchJobVO;
import com.tdsecurities.cvr.model.DataTrasferVO;
import com.tdsecurities.cvr.util.BatchUtils;

public abstract class BatchService implements IBatchService {
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	protected static final Logger logger = Logger.getLogger(BatchService.class);

	private static final String SQL_INSERT_TRANSFER_RECORD = "INSERT INTO data_transfer_status (request_id, group_type, valuation_date, delivery_type, status, transfer_description,description,update_date) VALUES (?,?, ?,0, ?,?,?,GEtDATE())"; 
	private static final String SQL_UPDATE_PARAMETER = "UPDATE env_lookup SET key_value=? WHERE key_name=?"; 
	private static final String SQL_UPDATE_SEQUENCE = "UPDATE seq_number SET sequence=sequence+1"; 
	private static final String SQL_GET_SEQUENCE = "SELECT * FROM seq_number"; 
	private static final String SQL_UPDATE_JOB_STATUS = "UPDATE batch_job set status = ?, notified = null WHERE batch_file = ?";
	private static final String SQL_UPDATE_JOB_NOTIFIED = "UPDATE batch_job set notified = ? WHERE batch_file = ?";

	private final static SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
	private JdbcTemplate jdbcTemplate;
	
	public BatchService(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/* (non-Javadoc)
	 * @see com.tdsecurities.batch.service.IBatchService#getValucationDate(java.lang.String)
	 */
	@Override
	public Date getValuationDate(String dateSTR){
		Date date=null;
		if(!StringUtils.isEmpty(dateSTR)){
			try {
				date=BatchUtils.parseDateStr(dateSTR);
			} catch (ParseException e) {
				logger.error(e);
			}
				
		}
		if(date==null){
			date=getValuationDateFromDB();
		}
		return date;
	}
	
	/* (non-Javadoc)
	 * @see com.tdsecurities.batch.service.IBatchService#createTransactionRecord(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int createTransactionRecord(String groupType, Date valuationDate, boolean status,boolean warning,String message){
	
		int affectedRow = 0;
		try {
			DataTrasferVO dataTrasferVO=new DataTrasferVO(getRequestId(),groupType, valuationDate, status,warning,message);
			affectedRow = this.jdbcTemplate.update(SQL_INSERT_TRANSFER_RECORD, new Object[]{dataTrasferVO.getRequestId(),dataTrasferVO.getGroupType(), dataTrasferVO.getValuationDate(), dataTrasferVO.getStatus(), dataTrasferVO.getTransferDescription(),dataTrasferVO.getDescription()},
			new int[]{java.sql.Types.INTEGER,java.sql.Types.VARCHAR, java.sql.Types.DATE, java.sql.Types.VARCHAR, java.sql.Types.VARCHAR, java.sql.Types.VARCHAR});

			logger.info(affectedRow + " records have been inserted into data_transfer_status.");
			
		} catch (Exception e) {
			logger.error("Update Scheduler record failed: ", e);
		}
		return affectedRow;

	}
	
	/* (non-Javadoc)
	 * @see com.tdsecurities.batch.service.IBatchService#updateBatchFlags(java.util.Map)
	 */
	@Override
	public void updateBatchFlags(Map<String,String> paraMap){
		if(paraMap!= null){
			for (Map.Entry<String, String> entry : paraMap.entrySet()) {
				updateBatchFlag(entry.getKey(),entry.getValue());
			}

		}
	}
	
	@Override
	public int[] processData(List<? extends Object> items,String Query,NamedParameterJdbcTemplate template){
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(items.toArray());
        int[] updateCounts = template.batchUpdate(Query, batch);
        
        return updateCounts;
	}
	
	protected void updateBatchFlag(String key,String value){
		this.jdbcTemplate.update(SQL_UPDATE_PARAMETER, new Object[]{value, key},
				new int[]{java.sql.Types.VARCHAR, java.sql.Types.VARCHAR});
	}

	/* (non-Javadoc)
	 * @see com.tdsecurities.batch.service.IBatchService#getFileName(java.util.Date, java.lang.String)
	 */
	@Override
	public String getFileName(Date date,String fileFormat){
		String fileName=null;
		String timestamp = BatchUtils.formatDateToStr(date, Consts.DATE_FORMAT_3);			
		fileName = fileFormat.replaceAll(Consts.DATE_FORMAT_7, timestamp);
		
		logger.info("The file has been renamed with a datestamp: " + fileName);

		return fileName;
	}
	
	protected int getRequestId(){
		int sequenceId=0;
		this.jdbcTemplate.update(SQL_UPDATE_SEQUENCE);
		sequenceId=this.jdbcTemplate.queryForObject(SQL_GET_SEQUENCE, new Object[]{}, Integer.class);
		return sequenceId;
	}
	
	@Override
	public void updateStatus(BatchJobVO bj) {
		this.getJdbcTemplate().update(SQL_UPDATE_JOB_STATUS, new Object[]{bj.getStatus(), bj.getBatchFile()},
				new int[]{java.sql.Types.VARCHAR, java.sql.Types.VARCHAR});
	}
	
	@Override
	public void updateNotified(BatchJobVO bj) {
		this.getJdbcTemplate().update(SQL_UPDATE_JOB_NOTIFIED, new Object[]{bj.getNotified(), bj.getBatchFile()},
				new int[]{java.sql.Types.INTEGER, java.sql.Types.VARCHAR});
	}

}