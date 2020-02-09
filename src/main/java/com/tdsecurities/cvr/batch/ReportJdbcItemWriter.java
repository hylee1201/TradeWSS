/**
 * 
 */
package com.tdsecurities.cvr.batch;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tdsecurities.cvr.model.Report;


/**
 * @author wangp4
 *
 */
public class ReportJdbcItemWriter implements ItemWriter<Report> {
	
	private static final String INSERT_PRODUCT = "insert into test_report (id,staffName,SALES,qty,date) values(?,?,?,?,?)";
	
	private static final String UPDATE_PRODUCT = "update test_report set staffName=?, SALES=?, qty=?, date=? where id = ?";
	
	private JdbcTemplate jdbcTemplate;
	
	public ReportJdbcItemWriter(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends Report> items) throws Exception {
		for(Report item : items) {
			int updated = jdbcTemplate.update(UPDATE_PRODUCT,
				item.getStaffName(),item.getSales(),item.getQty(),item.getDate(),item.getId()
			);
			if(updated == 0) {
				jdbcTemplate.update(
					INSERT_PRODUCT,
					item.getId(),item.getStaffName(),item.getSales(),item.getQty(),item.getDate()
				);	
			}								
		}
	}

}
