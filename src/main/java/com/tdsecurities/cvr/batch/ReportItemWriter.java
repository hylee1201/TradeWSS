/**
 * 
 */
package com.tdsecurities.cvr.batch;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.tdsecurities.cvr.batch.service.IBatchService;


/**
 * @author wangp4
 *
 */
public class ReportItemWriter implements ItemWriter<Object> {
	
	
	//private static final String UPDATE_WSS_OIS_RECORD = "update fxfwd_deals set deal_number=?, mtm_cad=?, mtm_usd=? where deal_number in ( ?)  and report_date= ?";
	IBatchService batchService;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(final List<? extends Object> items) throws Exception {
        
		batchService.processData(items);
//        jdbcTemplate.batchUpdate(UPDATE_WSS_OIS_RECORD, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i)
//                    throws SQLException {
//            	OISReport report = items.get(i);
//                ps.setString(1, report.getDealNumber());
//                ps.setDouble(2, report.getMtmCad());
//                ps.setDouble(3, report.getMtmUsd());
//                ps.setString(5, report.getDate());
//                ps.setLong(4, report.getId());
//            }
//
//            @Override
//            public int getBatchSize() {
//                return items.size();
//            }
//        });
//                int[] updateCounts = jdbcTemplate.batchUpdate(
//        		UPDATE_WSS_OIS_RECORD, batch);
//		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
//		MapSqlParameterSource parameters = new MapSqlParameterSource();
//		parameters.addValue("names", Arrays.asList("foo1", "foo2"));
//		List Foos = namedParameterJdbcTemplate.query("select * from foo where name in (:names)",
//		    parameters,
//		    new FooMapper()
//		);
		//logger.info("Total commit:"+items.size()+",It takes:"+totalTime);
	}

	public IBatchService getBatchService() {
		return batchService;
	}

	public void setBatchService(IBatchService batchService) {
		this.batchService = batchService;
	}

}
