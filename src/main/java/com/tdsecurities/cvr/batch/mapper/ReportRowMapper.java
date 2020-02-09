package com.tdsecurities.cvr.batch.mapper;  
  
import java.sql.ResultSet;  
import java.sql.SQLException;  
  



import org.springframework.jdbc.core.RowMapper;

import com.tdsecurities.cvr.model.Report;
  
/** 
 * @author wangp4
 * 
 */  
public class ReportRowMapper implements RowMapper<Report>{  
  
 @Override  
 public Report mapRow(ResultSet resultSet, int rowNum) throws SQLException {  
	 Report report = new Report();  
	 report.setId(resultSet.getInt("id"));  
	 report.setStaffName(resultSet.getString("staffName"));  
	 report.setSales(resultSet.getBigDecimal("SALES"));  
	 report.setDate(resultSet.getDate("DATE"));  
	 report.setQty(resultSet.getInt("qty"));  
  return report;  
 }  
  
}